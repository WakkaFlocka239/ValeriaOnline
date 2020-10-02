package me.wakka.valeriaonline.features.dungeons;

import lombok.Getter;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Dungeons {
	@Getter
	public static List<Dungeon> dungeons = new ArrayList<>();

	public static boolean isInDungeon(Player player) {
		Dungeon dungeon = fromLocation(player.getLocation());
		return dungeon != null;
	}

	public static Dungeon fromLocation(Location location) {
		WorldGuardUtils WGUtils = new WorldGuardUtils(location);
		for (Dungeon dungeon : dungeons) {
			if (WGUtils.isInRegion(location, dungeon.getRegionID()))
				return dungeon;
		}

		return null;
	}

	public static boolean isDungeonRegion(String id) {
		for (Dungeon dungeon : dungeons) {
			if (id.equalsIgnoreCase(dungeon.getRegionID()))
				return true;
		}
		return false;
	}
}
