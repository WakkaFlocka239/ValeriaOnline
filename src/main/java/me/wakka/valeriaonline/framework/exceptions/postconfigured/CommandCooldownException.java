package me.wakka.valeriaonline.framework.exceptions.postconfigured;

import me.wakka.valeriaonline.features.cooldown.Cooldowns;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CommandCooldownException extends PostConfiguredException {
	public CommandCooldownException(OfflinePlayer player, String type) {
		this(player.getUniqueId(), type);
	}

	public CommandCooldownException(UUID uuid, String type) {
		super("You can run this command again in &7" + Cooldowns.getDiff(uuid, type));
	}

}
