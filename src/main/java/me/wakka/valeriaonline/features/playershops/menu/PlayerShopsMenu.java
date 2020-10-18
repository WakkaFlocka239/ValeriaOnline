package me.wakka.valeriaonline.features.playershops.menu;

import fr.minuskube.inv.SmartInventory;

public class PlayerShopsMenu {

	public static SmartInventory open() {
		return SmartInventory.builder()
				.title("&fPlayer Shops")
				.size(6, 9)
				.provider(new PlayerShopsProvider())
				.build();
	}
}
