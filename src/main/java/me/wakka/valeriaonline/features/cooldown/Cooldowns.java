package me.wakka.valeriaonline.features.cooldown;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.TimespanFormatter;
import org.bukkit.OfflinePlayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cooldowns {
	public static List<Cooldown> cooldowns = new ArrayList<>();

	public static boolean check(OfflinePlayer player, String type, Time time) {
		return check(player.getUniqueId(), type, time);
	}

	public static boolean check(UUID uuid, String type, Time time) {
		return check(uuid, type, time.get());
	}

	public static boolean check(OfflinePlayer player, String type, double ticks) {
		return check(player.getUniqueId(), type, ticks);
	}

	public static boolean check(UUID uuid, String type, double ticks) {
		Cooldown cooldown = get(uuid);
		if (cooldown == null) {
			ValeriaOnline.warn("Cooldown object is null? " + uuid.toString() + " / " + type + " / " + ticks);
			return false;
		}

		if (!cooldown.check(type))
			return false;

		cooldown = cooldown.create(type, ticks);
		Cooldowns.cooldowns.add(cooldown);
		return true;
	}

	public static Cooldown get(UUID uuid){
		for (Cooldown cooldown : cooldowns) {
			if(cooldown.getUuid().equals(uuid))
				return cooldown;
		}

		return null;
	}

	public static String getDiff(OfflinePlayer player, String type) {
		return getDiff(player.getUniqueId(), type);
	}

	public static String getDiff(UUID uuid, String type) {
		Cooldown cooldown = get(uuid);
		if (cooldown.exists(type))
			return TimespanFormatter.timespanDiff(LocalDateTime.now(), cooldown.getTime(type));
		return ".0s";
	}
}
