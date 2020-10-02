package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.entity.Player;

@Permission("group.staff")
@Aliases({"checkperm", "permtest", "testperm", "hasperm"})
public class PermCheckCommand extends CustomCommand {

	public PermCheckCommand(CommandEvent event) {
		super(event);
	}

	@Path("<permission>")
	void check(String permission) {
		check(player(), permission);
	}

	@Path("<player> <permission>")
	void check(Player player, String permission) {
		if (player.hasPermission(permission))
			send("&a✔ " + player.getName() + " has permission " + permission);
		else
			send("&c✗ " + player.getName() + " does not have permission " + permission);
	}

}