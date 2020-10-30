package me.wakka.valeriaonline.features.commands.staff.freeze;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.entity.Player;

@Aliases("ymc")
@Permission("group.staff")
public class YouMayContinueCommand extends CustomCommand {

	public YouMayContinueCommand(CommandEvent event) {
		super(event);
	}

	@Path("<player> [warn...]")
	void player(Player player, String reason) {
		line(2);
		runCommand("unfreeze " + player.getName());
		runCommand("vanish on");
		runCommand("forcechannel " + player.getName() + " g");
		if (!isNullOrEmpty(reason))
			runCommand("warn " + player.getName() + " " + reason);
	}

}
