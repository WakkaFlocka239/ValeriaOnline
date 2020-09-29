package me.wakka.valeriaonline.features.teleportrequests;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Request {
	private UUID senderUUID;
	private UUID receiverUUID;
	private TeleportType type;
	private Location location;
	private LocalDateTime timeSent = LocalDateTime.now();
	private boolean expired = false;

	public Request(Player sender, Player receiver, TeleportType type) {
		this.senderUUID = sender.getUniqueId();
		this.receiverUUID = receiver.getUniqueId();
		if (type == TeleportType.TELEPORT)
			this.location = receiver.getLocation();
		else
			this.location = sender.getLocation();
		this.type = type;
	}

	public OfflinePlayer getSender() {
		return Utils.getPlayer(senderUUID);
	}

	public OfflinePlayer getReceiver() {
		return Utils.getPlayer(receiverUUID);
	}

	public enum TeleportType {
		TELEPORT,
		TELEPORT_HERE
	}
}
