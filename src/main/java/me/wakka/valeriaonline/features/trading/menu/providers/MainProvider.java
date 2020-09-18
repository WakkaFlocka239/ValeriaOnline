package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.wakka.valeriaonline.Utils.ItemBuilder;
import me.wakka.valeriaonline.Utils.MenuUtils;
import me.wakka.valeriaonline.Utils.StringUtils;
import me.wakka.valeriaonline.features.trading.Professions;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import org.bukkit.entity.Player;

public class MainProvider extends MenuUtils implements InventoryProvider {
	@Override
	public void init(Player player, InventoryContents contents) {
		addCloseItem(contents);

		int row = 1;
		int column = 1;

		for (Professions profession : Professions.values()) {
			contents.set(row, column, ClickableItem.from(new ItemBuilder(profession.getMaterial()).name("&e" + StringUtils.camelCase(profession.name())).build(), e -> {
				if (profession == Professions.WANDERING_TRADER) TradeEditorMenus.openTrades(player, profession, 1);
				else TradeEditorMenus.openLevel(player, profession);
			}));
			if (column == 7) {
				column = 1;
				row++;
			}
			else
				column++;
		}
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
