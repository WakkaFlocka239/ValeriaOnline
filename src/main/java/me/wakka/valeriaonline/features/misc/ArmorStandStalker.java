package me.wakka.valeriaonline.features.misc;

import me.wakka.valeriaonline.utils.CitizensUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ArmorStandStalker {
	private static final Location loc1 = new Location(Bukkit.getWorld("world"), -112, 69, -457);

	public static final List<Location> armorStandLocs = Collections.singletonList(loc1);

	public ArmorStandStalker() {

		Tasks.repeat(Time.SECOND.x(5), 2, () -> {
			for (Location location : armorStandLocs) {
				ArmorStand armorStand = (ArmorStand) Utils.getNearestEntityType(location, EntityType.ARMOR_STAND, 1.5);
				if (armorStand == null)
					continue;

				Player nearestPlayer = (Player) Utils.getNearestEntityType(location, EntityType.PLAYER, 15);
				if (nearestPlayer == null || CitizensUtils.isNPC(nearestPlayer))
					continue;

				Utils.makeArmorStandLookAtPlayer(armorStand, nearestPlayer, null, null, -30.0, 30.0);

			}
		});
	}
}
