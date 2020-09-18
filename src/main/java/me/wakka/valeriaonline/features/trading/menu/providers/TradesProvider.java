package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.Utils.ItemBuilder;
import me.wakka.valeriaonline.Utils.MenuUtils;
import me.wakka.valeriaonline.Utils.StringUtils;
import me.wakka.valeriaonline.Utils.Tasks;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.features.trading.models.Type;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
public class TradesProvider extends MenuUtils implements InventoryProvider {

	Profession profession;
	int level;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> TradeEditorMenus.openLevel(player, profession));

		List<Trade> trades = Trading.getTrades(profession, level);

		//Remove New Trade Button if more than 28 trades
		if (trades.size() < 28) {
			//New Trade Button
			contents.set(0, 4, ClickableItem.from(new ItemBuilder(Material.GREEN_CONCRETE).name("&aNew Trade").build(), e -> {
				Trade trade = new Trade(Trading.getNextID(profession, level));
				Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
				Trading.save();
				Tasks.wait(1, () -> TradeEditorMenus.openTradeEditor(player, profession, level, trade));
			}));
		}

		int row = 1;
		int column = 1;
		for (int i = 0; i < trades.size(); i++) {
			ItemBuilder item = new ItemBuilder(Material.CHEST).name("&eTrade " + (i + 1)).amount(i + 1)
					.lore("&3● " + trades.get(i).getIngredient1().getItemMeta().getDisplayName());
			if (trades.get(i).getIngredient2() != null)
				item.lore("&3● " + trades.get(i).getIngredient2().getItemMeta().getDisplayName());
			item.lore(" ")
					.lore("&3○ " + trades.get(i).getResult().getItemMeta().getDisplayName())
					.lore(" ")
					.lore("&eTypes:");
			if (trades.get(i).getTypes().size() == 7)
				item.lore("&3All");
			else {
				for (Type type : trades.get(i).getTypes()) {
					item.lore("&3- " + StringUtils.camelCase(type.name()));
				}
			}

			int j = i;
			contents.set(row, column, ClickableItem.from(item.build(), e ->
					TradeEditorMenus.openTradeEditor(player, profession, level, trades.get(j))));

			if (column == 7) {
				row++;
				column = 1;
			}
			else
				column++;

		}
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
