package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.features.trading.models.Type;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.MenuUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class TradeEditProvider extends MenuUtils implements InventoryProvider {

	public Profession profession;
	int level;
	Trade trade;
	Type filter;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> TradeEditorMenus.openTrades(player, profession, level, Type.ALL));

		// Types Item
		ItemBuilder typesBuilder = new ItemBuilder(Material.MAGMA_CREAM).name("&dTypes:");
		if (trade.getTypes().size() >= 7)
			typesBuilder.lore("&7All");
		else
			for (Type type : trade.getTypes())
				typesBuilder.lore("&d" + StringUtils.camelCase(type.name()));
		if (profession != Profession.WANDERING_TRADER)
			contents.set(0, 4, ClickableItem.from(typesBuilder.build(), e -> TradeEditorMenus.openTypeProvider(player, profession, level, trade, filter)));

		//Ingredient 1
		ItemStack ingredient1 = new ItemBuilder(Material.ITEM_FRAME).name("&dIngredient 1").build();
		//Ingredient 2
		ItemStack ingredient2 = new ItemBuilder(Material.ITEM_FRAME).name("&dIngredient 2").build();
		//Result
		ItemStack result = new ItemBuilder(Material.ITEM_FRAME).name("&dResult").build();

		contents.set(2, 2, ClickableItem.from((trade.getIngredient1() == null ? ingredient1 : trade.getIngredient1()), e -> {
			if (e.getItem().equals(ingredient1)) {
				if (Utils.isNullOrAir(e.getPlayer().getItemOnCursor())) return;
				trade.setIngredient1(e.getPlayer().getItemOnCursor());
				Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
				Trading.save();
				e.getPlayer().setItemOnCursor(null);
				Tasks.wait(1, () -> TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter));
			}
			else {
				if (Utils.isNullOrAir(e.getPlayer().getItemOnCursor())) {
					if (trade.getIngredient2() == null)
						trade.setIngredient1(null);
					else {
						trade.setIngredient1(trade.getIngredient2());
						trade.setIngredient2(null);
					}
					Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
					Trading.save();
					Tasks.wait(1, () -> {
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
						e.getPlayer().setItemOnCursor(e.getItem());
					});
				}
				else {
					trade.setIngredient1(e.getPlayer().getItemOnCursor());
					Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
					Trading.save();
					Tasks.wait(1, () -> {
						e.getPlayer().setItemOnCursor(new ItemStack(Material.AIR));
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
						e.getPlayer().setItemOnCursor(e.getItem());
					});
				}
			}
		}));

		contents.set(2, 3, ClickableItem.from((trade.getIngredient2() == null ? ingredient2 : trade.getIngredient2()), e -> {
			if (e.getItem().equals(ingredient2)) {
				if (Utils.isNullOrAir(e.getPlayer().getItemOnCursor())) return;
				if (trade.getIngredient1() == null)
					trade.setIngredient1(e.getPlayer().getItemOnCursor());
				else
					trade.setIngredient2(e.getPlayer().getItemOnCursor());
				Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
				Trading.save();
				e.getPlayer().setItemOnCursor(null);
				Tasks.wait(1, () -> TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter));
			}
			else {
				if (Utils.isNullOrAir(e.getPlayer().getItemOnCursor())) {
					trade.setIngredient2(null);
					Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
					Trading.save();
					Tasks.wait(1, () -> {
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
						e.getPlayer().setItemOnCursor(e.getItem());
					});
				}
				else {
					if (trade.getIngredient1() == null)
						trade.setIngredient1(e.getPlayer().getItemOnCursor());
					else
						trade.setIngredient2(e.getPlayer().getItemOnCursor());
					Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
					Trading.save();
					Tasks.wait(1, () -> {
						e.getPlayer().setItemOnCursor(new ItemStack(Material.AIR));
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
						e.getPlayer().setItemOnCursor(e.getItem());
					});
				}
			}
		}));

		contents.set(2, 6, ClickableItem.from((trade.getResult() == null ? result : trade.getResult()), e -> {
			if (e.getItem().equals(result)) {
				if (Utils.isNullOrAir(e.getPlayer().getItemOnCursor())) return;
				trade.setResult(e.getPlayer().getItemOnCursor());
				Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
				Trading.save();
				e.getPlayer().setItemOnCursor(null);
				Tasks.wait(1, () -> TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter));
			}
			else {
				if (Utils.isNullOrAir(e.getPlayer().getItemOnCursor())) {
					trade.setResult(null);
					Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
					Trading.save();
					Tasks.wait(1, () -> {
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
						e.getPlayer().setItemOnCursor(e.getItem());
					});
				}
				else {
					trade.setResult(e.getPlayer().getItemOnCursor());
					Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
					Trading.save();
					Tasks.wait(1, () -> {
						e.getPlayer().setItemOnCursor(new ItemStack(Material.AIR));
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
						e.getPlayer().setItemOnCursor(e.getItem());
					});
				}
			}
		}));

		//Stock Item
		contents.set(2, 7, ClickableItem.from(new ItemBuilder(Material.PAPER).name("&dStock").lore("&7" + trade.getStock()).build(), e -> {
			ValeriaOnline.getSignMenuFactory().lines("", "^ ^ ^ ^ ^ ^", "Enter a", "stock amount")
					.response(lines -> {
						if (lines[0].length() > 0) {
							try {
								int stock = Integer.parseInt(lines[0]);
								trade.setStock(stock);
								Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
								Trading.save();
							} catch (NumberFormatException ex) {
								Utils.send(player, "&cThe stock amount must be a number");
							}
						}
						TradeEditorMenus.openTradeEditor(player, profession, level, trade, filter);
 					})
					.open(player);
		}));
	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {

	}
}
