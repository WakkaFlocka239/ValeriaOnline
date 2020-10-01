package me.wakka.valeriaonline.features.playershops.menu;

import fr.minuskube.inv.SmartInventory;

public class PlayerShopsMenu {

	public static SmartInventory open() {
		return SmartInventory.builder()
				.title("Player Shops")
				.size(6, 9)
				.provider(new PlayerShopsProvider())
				.build();
	}
}
