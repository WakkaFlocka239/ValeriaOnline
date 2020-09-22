package me.wakka.valeriaonline.utils;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.ItemClickData;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import me.wakka.valeriaonline.framework.exceptions.InvalidInputException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

import static me.wakka.valeriaonline.utils.ItemBuilder.loreize;
import static me.wakka.valeriaonline.utils.StringUtils.colorize;

public abstract class MenuUtils {

	protected ItemStack addGlowing(ItemStack itemStack) {
		return Utils.addGlowing(itemStack);
	}

	public int getRows(int items) {
		return (int) Math.ceil((double) items / 9);
	}

	protected ItemStack nameItem(Material material, String name) {
		return nameItem(new ItemStack(material), name, null);
	}

	protected ItemStack nameItem(Material material, String name, String lore) {
		return nameItem(new ItemStack(material), name, lore);
	}

	protected ItemStack nameItem(ItemStack item, String name) {
		return nameItem(item, name, null);
	}

	protected ItemStack nameItem(ItemStack item, String name, String lore) {
		ItemMeta meta = item.getItemMeta();
		if (name != null)
			meta.setDisplayName(colorize(name));
		if (lore != null)
			meta.setLore(Arrays.asList(loreize(colorize(lore)).split("\\|\\|")));

		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}

	protected void warp(Player player, String warp) {
		Utils.runCommand(player, "warp " + warp);
	}

	public void command(Player player, String command) {
		Utils.runCommand(player, command);
	}

	public static String getLocationLore(Location location) {
		if (location == null) return null;
		return "&7X:&d " + (int) location.getX() + "||&7Y:&d " + (int) location.getY() + "||&7Z:&d " + (int) location.getZ();
	}

	protected void addBackItem(InventoryContents contents, Consumer<ItemClickData> consumer) {
		contents.set(0, 0, ClickableItem.from(backItem(), consumer));
	}

	protected void addCloseItem(InventoryContents contents) {
		contents.set(0, 0, ClickableItem.from(closeItem(), e -> e.getPlayer().closeInventory()));
	}

	protected ItemStack backItem() {
		return new ItemBuilder(Material.BARRIER).name("&cBack").build();
	}

	protected ItemStack closeItem() {
		return new ItemBuilder(Material.BARRIER).name("&cClose").build();
	}

	public static void centerItems(ClickableItem[] items, InventoryContents contents, int row) {
		centerItems(items, contents, row, true);
	}

	public static void centerItems(ClickableItem[] items, InventoryContents contents, int row, boolean space) {
		if (items.length > 9)
			throw new InvalidInputException("Cannot center more than 9 items on one row");
		int[] even = {3, 5, 1, 7};
		int[] odd = {4, 2, 6, 0, 8};
		int[] noSpace = {4, 3, 5, 2, 6, 1, 7, 0, 8};
		if (items.length < 5 && space)
			if (items.length % 2 == 0)
				for (int i = 0; i < items.length; i++)
					contents.set(row, even[i], items[i]);
			else
				for (int i = 0; i < items.length; i++)
					contents.set(row, odd[i], items[i]);
		else
			for (int i = 0; i < items.length; i++)
				contents.set(row, noSpace[i], items[i]);
	}

	@Builder(buildMethodName = "_build")
	@AllArgsConstructor
	public static class ConfirmationMenu extends MenuUtils implements InventoryProvider {
		@Getter
		@Builder.Default
		private String title = "&4Are you sure?";
		@Builder.Default
		private String cancelText = "&cNo";
		private String cancelLore;
		@Builder.Default
		private String confirmText = "&aYes";
		private String confirmLore;
		@Builder.Default
		private Consumer<ItemClickData> onCancel = (e) -> e.getPlayer().closeInventory();
		@NonNull
		private Consumer<ItemClickData> onConfirm;

		public static class ConfirmationMenuBuilder {

			public void open(Player player) {
				ConfirmationMenu build = _build();
				SmartInventory.builder()
						.title(colorize(build.getTitle()))
						.provider(build)
						.size(3, 9)
						.build()
						.open(player);
			}

			@Deprecated
			public ConfirmationMenu build() {
				throw new UnsupportedOperationException("Use open(player)");
			}

		}

		@Override
		public void init(Player player, InventoryContents contents) {
			ItemStack cancelItem = nameItem(Material.RED_CONCRETE, cancelText, cancelLore);
			ItemStack confirmItem = nameItem(Material.LIME_CONCRETE, confirmText, confirmLore);

			contents.set(1, 2, ClickableItem.from(cancelItem, (e) -> {
				if (onCancel != null)
					onCancel.accept(e);

				if (title.equals(e.getPlayer().getOpenInventory().getTitle()))
					e.getPlayer().closeInventory();
			}));

			contents.set(1, 6, ClickableItem.from(confirmItem, e -> {
				onConfirm.accept(e);

				if (colorize(title).equals(e.getPlayer().getOpenInventory().getTitle()))
					e.getPlayer().closeInventory();
			}));
		}

		@Override
		public void update(Player player, InventoryContents inventoryContents) {
		}

	}

}
