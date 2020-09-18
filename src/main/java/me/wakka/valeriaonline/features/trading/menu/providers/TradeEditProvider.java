package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.Utils.ItemBuilder;
import me.wakka.valeriaonline.Utils.MenuUtils;
import me.wakka.valeriaonline.Utils.StringUtils;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.features.trading.models.Type;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class TradeEditProvider extends MenuUtils implements InventoryProvider {

	public Profession profession;
	int level;
	Trade trade;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> TradeEditorMenus.openTrades(player, profession, level));

		// Types Item
		ItemBuilder typesBuilder = new ItemBuilder(Material.MAGMA_CREAM).name("&eTypes:");
		if (trade.getTypes().size() == 7)
			typesBuilder.lore("&eAll");
		else
			for (Type type : trade.getTypes())
				typesBuilder.lore("&3" + StringUtils.camelCase(type.name()));
		contents.set(0, 4, ClickableItem.from(typesBuilder.build(), e -> TradeEditorMenus.openTypeProvider(player, profession, level, trade)));

		//temp items
		contents.set(2, 2, ClickableItem.empty(new ItemBuilder(Material.ITEM_FRAME).name("&eIngredient 1").build()));
		contents.set(2, 3, ClickableItem.empty(new ItemBuilder(Material.ITEM_FRAME).name("&eIngredient 2").build()));
		contents.set(2, 6, ClickableItem.empty(new ItemBuilder(Material.ITEM_FRAME).name("&eResult").build()));
		contents.set(2, 7, ClickableItem.empty(new ItemBuilder(Material.PAPER).name("&eStock").lore("&369").build()));
	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {

	}
}
