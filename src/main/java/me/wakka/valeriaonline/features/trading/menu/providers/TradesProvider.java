package me.wakka.valeriaonline.features.trading.menu.providers;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.Utils.MenuUtils;
import me.wakka.valeriaonline.features.trading.Professions;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class TradesProvider extends MenuUtils implements InventoryProvider {

	Professions profession;
	int level;

	@Override
	public void init(Player player, InventoryContents contents) {

	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
