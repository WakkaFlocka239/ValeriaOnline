package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.Utils.ItemBuilder;
import me.wakka.valeriaonline.Utils.MenuUtils;
import me.wakka.valeriaonline.features.trading.Professions;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class LevelProvider extends MenuUtils implements InventoryProvider {

	Professions profession;

	@Override
	public void init(Player player, InventoryContents contents) {

		for (int i = 1; i <= 5; i++) {
			int j = i;
			contents.set(1, i + 1, ClickableItem.from(new ItemBuilder(Material.EXPERIENCE_BOTTLE).name("&eLevel " + i).amount(i).build(),
					e -> TradeEditorMenus.openTrades(player, profession, j)));
		}
	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {

	}
}
