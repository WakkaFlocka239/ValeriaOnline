package me.wakka.valeriaonline.features.chat.alerts;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.SoundUtils;
import org.bukkit.entity.Player;

public class AlertCommand extends CustomCommand {

	public AlertCommand(CommandEvent event) {
		super(event);
	}

	@Path("<player>")
	void alert(Player player) {
		SoundUtils.Jingle.PING.play(player);
	}

}
