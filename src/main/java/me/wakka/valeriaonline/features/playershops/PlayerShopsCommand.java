package me.wakka.valeriaonline.features.playershops;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.menus.MenuUtils;
import me.wakka.valeriaonline.features.playershops.menu.PlayerShopsMenu;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.playershop.PlayerShop;
import me.wakka.valeriaonline.models.playershop.PlayerShopService;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.OfflinePlayer;

@Aliases({"playershop", "shops", "pshop", "pshops"})
public class PlayerShopsCommand extends CustomCommand {
	PlayerShopService service = new PlayerShopService();
	PlayerShop shop;

	public PlayerShopsCommand(CommandEvent event) {
		super(event);
	}

	@Path
	void shops() {
		list();
	}

	@Path("list")
	@Description("Opens the menu")
	void list() {
		PlayerShopsMenu.open().open(player());
	}

	@Path("tp")
	@Description("Teleport to your shop")
	void teleport() {
		teleportToShop(player());
	}

	@Path("<player>")
	@Description("Teleport to the player's shop")
	void teleportToShop(OfflinePlayer player) {
		boolean self = false;
		if (player.equals(Utils.getPlayer(player().getUniqueId())))
			self = true;

		shop = service.get(player);
		if (shop == null) {
			if (self)
				error("You don't have a shop");
			error(player.getName() + "doesn't have a shop");
		}

		if (shop.getLocation() == null) {
			if (self)
				error("Your shop is not set");
			error(player.getName() + "'s shop is not set");
		}

		player().teleport(shop.getLocation());
	}

	@Path("set")
	@Description("Sets your shop location")
	void setLoc() {
		if (!PlayerShops.allowedWorlds.contains(player().getWorld()))
			error("You can't set a shop in this world");

		shop = service.get(player());

		boolean newShop = false;
		if (shop == null) {
			double setupCost = PlayerShops.setupCost;
			if (!ValeriaOnline.getEcon().has(player(), setupCost))
				error("You need " + setupCost + " Crowns to setup your shop!");

			newShop = true;
		}

		if (!newShop && !ValeriaOnline.getEcon().has(player(), PlayerShops.resetCost))
			error("You need " + PlayerShops.resetCost + " Crowns to reset your shop!");

		boolean createNewShop = newShop;
		String confirmLore = "&cCosts: &6" + PlayerShops.resetCost + " Crowns";
		if (createNewShop) {
			confirmLore = "&cCosts: &6" + PlayerShops.setupCost + " Crowns";
		}

		MenuUtils.ConfirmationMenu.builder()
				.confirmLore(confirmLore)
				.onConfirm(e -> {
					if (createNewShop) {
						shop = new PlayerShop(player(), null, "");
						Utils.withdraw(player(), PlayerShops.setupCost, PREFIX);
					} else {
						Utils.withdraw(player(), PlayerShops.resetCost, PREFIX);
					}

					shop.setLocation(player().getLocation());
					service.save(shop);

					send(PREFIX + "Shop location set!");
				})
				.open(player());
	}

	@Path("remove")
	@Description("Removes your shop location")
	void remove() {
		shop = service.get(player());
		shop.setLocation(null);
		service.save(shop);
		send(PREFIX + "Shop removed!");
	}

	@Path("description [string...]")
	@Description("150 character limit, use || for new lines")
	void setDesc(String description) {
		shop = service.get(player());

		if (shop != null) {
			if (isNullOrEmpty(description))
				description = "";

			if (description.length() > 150)
				error("description cannot be greater than 150 characters");

			shop.setDescription(description);
			service.save(shop);

			send(PREFIX + "Shop description set!");
		} else {
			error("You don't have a shop set!");
		}
	}

	@Path("remove <player>")
	@Permission("group.mod")
	@Description("Removes the player's shop location")
	void removeShop(OfflinePlayer player) {
		shop = service.get(player);
		shop.setLocation(null);
		service.save(shop);

		send(PREFIX + "Removed " + player.getName() + "'s shop");
	}

	@Path("delete <player>")
	@Permission("group.admin")
	@Description("Deletes the player's shop")
	void deleteShop(OfflinePlayer player) {
		shop = service.get(player);
		service.delete(shop);

		send(PREFIX + "Deleted " + player.getName() + "'s shop");
	}

}
