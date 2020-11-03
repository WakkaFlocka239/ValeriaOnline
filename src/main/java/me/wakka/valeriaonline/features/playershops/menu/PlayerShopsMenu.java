package me.wakka.valeriaonline.features.playershops.menu;

import fr.minuskube.inv.SmartInventory;
import me.wakka.valeriaonline.features.menus.MenuUtils;

public class PlayerShopsMenu {

	public static SmartInventory open() {
		return SmartInventory.builder()
				.title(MenuUtils.title("Player Shops"))
				.size(6, 9)
				.provider(new PlayerShopsProvider())
				.build();
	}
}
