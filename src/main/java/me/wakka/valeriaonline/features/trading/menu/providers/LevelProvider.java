package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Type;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.MenuUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class LevelProvider extends MenuUtils implements InventoryProvider {

	Profession profession;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> TradeEditorMenus.openMain(player));

		for (int i = 1; i <= 5; i++) {
			int j = i;
			contents.set(1, i + 1, ClickableItem.from(new ItemBuilder(Material.EXPERIENCE_BOTTLE).name("&eLevel " + i).amount(i).build(),
					e -> TradeEditorMenus.openTrades(player, profession, j, Type.ALL)));
		}
	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {

	}
}
