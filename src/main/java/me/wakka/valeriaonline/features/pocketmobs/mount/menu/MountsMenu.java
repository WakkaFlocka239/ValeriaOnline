package me.wakka.valeriaonline.features.pocketmobs.mount.menu;

import fr.minuskube.inv.SmartInventory;
import me.wakka.valeriaonline.features.menus.MenuUtils;
import me.wakka.valeriaonline.features.pocketmobs.mount.MountType;
import me.wakka.valeriaonline.features.pocketmobs.mount.menu.providers.MainProvider;
import org.bukkit.entity.Player;

public class MountsMenu {

	public static SmartInventory openMain(Player player) {
		return SmartInventory.builder()
				.title(MenuUtils.title("Mounts Menu"))
				.size(getRows(player), 9)
				.provider(new MainProvider())
				.build();
	}


	private static int getRows(Player player) {
		int mounts = MountType.getMountTypes(player).size();
		int rows = (int) Math.ceil((mounts / 9.0)) + 2;
		return Math.min(Math.max(rows, 3), 6);
	}
}
