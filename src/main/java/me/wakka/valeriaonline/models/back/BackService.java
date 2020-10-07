package me.wakka.valeriaonline.models.back;

import me.wakka.valeriaonline.models.MySQLService;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackService extends MySQLService {

	public Back get(Player player) {
		return get(player.getUniqueId().toString());
	}

	public Back get(String uuid) {
		Back back = database.where("uuid = ?", uuid).first(Back.class);
		if (back.getUuid() == null) return null;
		return back;
	}

	public void save(Back back) {
		Location loc = back.getLocation();
		if (loc == null || loc.getWorld() == null) {
			delete(back);
		} else {
			back.setLocation(loc);
			super.save(back);
		}
	}

	public void delete(Back back) {
		database.table("back").where("uuid = ? ", back.getUuid()).delete();
	}
}
