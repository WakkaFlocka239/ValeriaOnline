package me.wakka.valeriaonline.features.trading.menu;

import fr.minuskube.inv.SmartInventory;
import me.wakka.valeriaonline.Utils.StringUtils;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.menu.providers.LevelProvider;
import me.wakka.valeriaonline.features.trading.menu.providers.MainProvider;
import me.wakka.valeriaonline.features.trading.menu.providers.TradeEditProvider;
import me.wakka.valeriaonline.features.trading.menu.providers.TradesProvider;
import me.wakka.valeriaonline.features.trading.menu.providers.TypeProvider;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import org.bukkit.entity.Player;

public class TradeEditorMenus {

	public static void openMain(Player player) {
		SmartInventory.builder()
				.title("Trade Editor - Select Profession")
				.size(4, 9)
				.provider(new MainProvider())
				.build()
				.open(player);
	}

	public static void openLevel(Player player, Profession profession) {
		String professionName = StringUtils.camelCase(profession.name());
		SmartInventory.builder()
				.title("Select " + professionName + " Trade Level")
				.size(3, 9)
				.provider(new LevelProvider(profession))
				.build()
				.open(player);
	}

	public static void openTrades(Player player, Profession profession, int level) {
		String professionName = StringUtils.camelCase(profession.name());
		SmartInventory.builder()
				.title("Edit " + professionName + " Lvl " + level)
				.size(getTradeSize(profession, level), 9)
				.provider(new TradesProvider(profession, level))
				.build()
				.open(player);
	}

	public static int getTradeSize(Profession profession, int level) {
		int trades = Trading.getTrades(profession, level).size();
		int rows = (int) Math.ceil((trades / 9.0)) + 2;
		return Math.min(Math.max(rows, 3), 6);
	}

	public static void openTradeEditor(Player player, Profession profession, int level, Trade trade) {
		SmartInventory.builder()
				.title("Edit Trade")
				.size(4, 9)
				.provider(new TradeEditProvider(profession, level, trade))
				.build()
				.open(player);
	}

	public static void openTypeProvider(Player player, Profession profession, int level, Trade trade) {
		SmartInventory.builder()
				.title("Select Villager Types")
				.size(4, 9)
				.provider(new TypeProvider(profession, level, trade))
				.build()
				.open(player);
	}
}
