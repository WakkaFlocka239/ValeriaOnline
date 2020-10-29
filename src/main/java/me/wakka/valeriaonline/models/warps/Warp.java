package me.wakka.valeriaonline.models.warps;

import com.dieselpoint.norm.serialize.DbSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.framework.persistence.serializer.mysql.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warp {
	private String name;
	@DbSerializer(LocationSerializer.class)
	private Location location;
	private String type;

	public void teleport(Player player) {
		player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
	}

}
