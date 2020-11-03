package me.wakka.valeriaonline.features.playershops.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.wakka.valeriaonline.features.menus.MenuUtils;
import me.wakka.valeriaonline.models.playershop.PlayerShop;
import me.wakka.valeriaonline.models.playershop.PlayerShopService;
import me.wakka.valeriaonline.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerShopsProvider extends MenuUtils implements InventoryProvider {
	PlayerShopService service = new PlayerShopService();

	@Override
	public void init(Player player, InventoryContents contents) {
		addCloseItem(contents);

		contents.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.BOOK).name("&dInfo")
				.lore("&7Click a shop")
				.lore("&7to visit it.")
				.build()));

		Pagination page = contents.pagination();

		List<ClickableItem> shopItems = new ArrayList<>();
		List<PlayerShop> playerShops;
		try {
			playerShops = service.getAll().stream()
					.filter(playerShop -> playerShop.getLocation() != null)
					.sorted(Comparator.comparing(playerShop -> playerShop.getPlayer().getName()))
					.collect(Collectors.toList());
		} catch (Exception ignored) {
			playerShops = service.getAll().stream()
					.filter(playerShop -> playerShop.getLocation() != null)
					.collect(Collectors.toList());
		}

		for (PlayerShop playerShop : playerShops) {
			OfflinePlayer shopPlayer = playerShop.getPlayer();
			String playerName = shopPlayer.getName();
			String description = playerShop.getDescription();

			ItemStack skull = new ItemBuilder(Material.PLAYER_HEAD)
					.skullOwner(shopPlayer)
					.name("&b" + playerName + "'s Shop")
					.lore(description)
					.loreize(true)
					.build();

			ClickableItem clickableSkull = ClickableItem.from(skull, e -> player.teleport(playerShop.getLocation()));

			shopItems.add(clickableSkull);
		}

		page.setItemsPerPage(36);
		page.setItems(shopItems.toArray(new ClickableItem[0]));
		page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

		if (!page.isFirst())
			contents.set(5, 0, ClickableItem.from(new ItemBuilder(Material.ARROW).name("<-- Back").build(),
					e -> PlayerShopsMenu.open().open(player, page.previous().getPage())));
		if (!page.isLast())
			contents.set(5, 8, ClickableItem.from(new ItemBuilder(Material.ARROW).name("Next -->").build(),
					e -> PlayerShopsMenu.open().open(player, page.next().getPage())));

	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {
	}
}
