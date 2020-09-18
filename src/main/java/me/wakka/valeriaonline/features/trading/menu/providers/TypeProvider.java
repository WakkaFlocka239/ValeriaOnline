package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.Utils.*;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.features.trading.models.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class TypeProvider extends MenuUtils implements InventoryProvider {

	Profession profession;
	int level;
	Trade trade;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> TradeEditorMenus.openTradeEditor(player, profession, level, trade));

		int i = 1;
		for (Type type : Type.values()) {
			ItemStack item = new ItemBuilder(type.getMaterial()).name("&e" + StringUtils.camelCase(type.name())).build().clone();
			boolean selected = trade.getTypes().contains(type);
			if (selected)
				Utils.addGlowing(item);
			contents.set(1, i++, ClickableItem.from(item, e -> {
				if (selected)
					trade.getTypes().remove(type);
				else
					trade.getTypes().add(type);
				Trading.getConfig().set(profession.name().toLowerCase() + "." + level + "." + trade.getId(), trade);
				Trading.save();
				Tasks.wait(1, () -> TradeEditorMenus.openTypeProvider(player, profession, level, trade));
			}));
		}

	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {

	}
}
