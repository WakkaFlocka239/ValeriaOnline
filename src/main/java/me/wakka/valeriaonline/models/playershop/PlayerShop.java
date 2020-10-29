package me.wakka.valeriaonline.models.playershop;

import com.dieselpoint.norm.serialize.DbSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.framework.persistence.serializer.mysql.LocationSerializer;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerShop {
	@NonNull
	private String uuid;
	@DbSerializer(LocationSerializer.class)
	private Location location;
	private String description;

	public OfflinePlayer getPlayer() {
		return Utils.getPlayer(uuid);
	}

	public PlayerShop(OfflinePlayer player, Location location, String description) {
		this.uuid = player.getUniqueId().toString();
		this.location = location;
		this.description = description;
	}
}
