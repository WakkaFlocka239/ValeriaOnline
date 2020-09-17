package me.wakka.valeriaonline.framework.exceptions;

import me.wakka.valeriaonline.features.cooldown.Cooldowns;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CommandCooldownException extends CustomException{
	public CommandCooldownException(OfflinePlayer player, String type) {
		this(player.getUniqueId(), type);
	}

	public CommandCooldownException(UUID uuid, String type) {
		super("You can run this command again in &e" + Cooldowns.getDiff(uuid, type));
	}

}
