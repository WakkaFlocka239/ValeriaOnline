package me.wakka.valeriaonline.models.playershop;

import me.wakka.valeriaonline.models.MySQLService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerShopService extends MySQLService {

	public PlayerShop get(OfflinePlayer player) {
		return get(player.getUniqueId().toString());
	}

	public PlayerShop get(Player player) {
		return get(player.getUniqueId().toString());
	}

	public PlayerShop get(String uuid) {
		PlayerShop shop = database.where("uuid = ?", uuid).first(PlayerShop.class);
		if (shop.getUuid() == null) return null;
		return shop;
	}

	public List<PlayerShop> getAll() {
		return database.select("*").results(PlayerShop.class);
	}

	public void delete(PlayerShop playerShop) {
		database.table("playershop").where("uuid = ? ", playerShop.getUuid()).delete();
	}
}
