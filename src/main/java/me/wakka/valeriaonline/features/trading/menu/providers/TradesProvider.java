package me.wakka.valeriaonline.features.trading.menu.providers;

import com.google.common.base.Strings;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.Utils.*;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.features.trading.models.Type;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TradesProvider extends MenuUtils implements InventoryProvider {

	Profession profession;
	int level;
	Type filter;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> {
			if (profession == Profession.WANDERING_TRADER)
				TradeEditorMenus.openMain(player);
			else
				TradeEditorMenus.openLevel(player, profession);
		});

		contents.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.BOOK).name("&eInfo")
				.lore("&3Shift-Right Click a")
				.lore("&3trade to delete it.")
				.build()));

		List<Trade> trades = Trading.getTrades(profession, level);

		//New Trade Button
		contents.set(0, 4, ClickableItem.from(new ItemBuilder(Material.LIME_CONCRETE).name("&aNew Trade").build(), e -> {
			Trade trade = new Trade(Trading.getNextID(profession, level));
			Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
			Trading.save();
			Tasks.wait(1, () -> TradeEditorMenus.openTradeEditor(player, profession, level, trade));
		}));

		Pagination page = contents.pagination();

		//Filter Button
		contents.set(0, 6, ClickableItem.from(
				new ItemBuilder(Material.HOPPER).name("&eFilter:").lore("&3" + StringUtils.camelCase(filter.name())).build(),
				e -> TradeEditorMenus.getTrades(profession, level, Utils.EnumUtils.nextWithLoop(Type.class, filter.ordinal())).open(player, page.getPage())
		));

		List<ClickableItem> menuItems = new ArrayList<>();

		for (int i = 0; i < trades.size(); i++) {
			if (filter != Type.ALL)
				if (trades.get(i).getTypes().contains(filter))
					continue;
			ItemBuilder item = new ItemBuilder(Material.CHEST).name("&eTrade " + (i + 1)).amount(i + 1);
			if (trades.get(i).getIngredient1() != null)
					item.lore("&3● " + (Strings.isNullOrEmpty(trades.get(i).getIngredient1().getItemMeta().getDisplayName()) ?
							StringUtils.camelCase(trades.get(i).getIngredient1().getType().name()) : trades.get(i).getIngredient1().getItemMeta().getDisplayName()) +
							" x " + trades.get(i).getIngredient1().getAmount());
			if (trades.get(i).getIngredient2() != null)
				item.lore("&3● " + (Strings.isNullOrEmpty(trades.get(i).getIngredient2().getItemMeta().getDisplayName()) ?
						StringUtils.camelCase(trades.get(i).getIngredient2().getType().name()) : trades.get(i).getIngredient2().getItemMeta().getDisplayName()) +
						" x " + trades.get(i).getIngredient2().getAmount());
			if (trades.get(i).getResult() != null) {
				item.lore(" ")
					.lore("&3○ " + (Strings.isNullOrEmpty(trades.get(i).getResult().getItemMeta().getDisplayName()) ?
						StringUtils.camelCase(trades.get(i).getResult().getType().name()) : trades.get(i).getResult().getItemMeta().getDisplayName()) +
							" x " + trades.get(i).getResult().getAmount());
			}

			item.lore("")
					.lore("&eStock:")
					.lore("&3" + trades.get(i).getStock())
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
			menuItems.add(ClickableItem.from(item.build(), e -> {
				if (((InventoryClickEvent) e.getEvent()).isShiftClick() && ((InventoryClickEvent) e.getEvent()).isRightClick()) {
					ConfirmationMenu.builder()
							.onConfirm(itemClickData -> {
								Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trades.get(j).getId(), null);
								Trading.save();
								Tasks.wait(1, () ->TradeEditorMenus.openTrades(player, profession, level, filter));
							})
							.onCancel(itemClickData -> TradeEditorMenus.openTrades(player, profession, level, filter))
							.open(player);
				}
				else
					TradeEditorMenus.openTradeEditor(player, profession, level, trades.get(j));
			}));
		}

		page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
		page.setItemsPerPage(36);
		page.setItems(menuItems.toArray(new ClickableItem[0]));

		if (!page.isFirst())
			contents.set(5, 0, ClickableItem.from(new ItemBuilder(Material.ARROW).name("<-- Back").build(),
					e -> TradeEditorMenus.getTrades(profession, level, filter).open(player, page.previous().getPage())));
		if (!page.isLast())
			contents.set(5, 8, ClickableItem.from(new ItemBuilder(Material.ARROW).name("Next -->").build(),
					e -> TradeEditorMenus.getTrades(profession, level, filter).open(player, page.next().getPage())));

	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
