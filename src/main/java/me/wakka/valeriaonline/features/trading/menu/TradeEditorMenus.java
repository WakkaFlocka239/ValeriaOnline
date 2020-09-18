package me.wakka.valeriaonline.features.trading.menu;

import fr.minuskube.inv.SmartInventory;
import me.wakka.valeriaonline.features.trading.Professions;
import me.wakka.valeriaonline.features.trading.menu.providers.LevelProvider;
import me.wakka.valeriaonline.features.trading.menu.providers.MainProvider;
import me.wakka.valeriaonline.features.trading.menu.providers.TradesProvider;
import org.bukkit.entity.Player;

public class TradeEditorMenus {

	public static void openMain(Player player) {
		SmartInventory.builder()
				.title("Trade Editor - Profession")
				.size(4, 9)
				.provider(new MainProvider())
				.build()
				.open(player);
	}

	public static void openLevel(Player player, Professions profession) {
		SmartInventory.builder()
				.title("Trade Editor - Level")
				.size(3, 9)
				.provider(new LevelProvider(profession))
				.build()
				.open(player);
	}

	public static void openTrades(Player player, Professions profession, int level) {
		SmartInventory.builder()
				.title("Trade Editor - Trades")
				.size(getTradeSize(), 9)
				.provider(new TradesProvider(profession, level))
				.build()
				.open(player);
	}

	public static int getTradeSize() {
		return 3;
	}



}
