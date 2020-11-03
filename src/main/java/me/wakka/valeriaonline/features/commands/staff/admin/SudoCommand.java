package me.wakka.valeriaonline.features.commands.staff.admin;

import lombok.NonNull;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.entity.Player;

@Permission("group.admin")
public class SudoCommand extends CustomCommand {

	public SudoCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("<player> <command...>")
	void run(Player player, String command) {
		Utils.runCommandAsOp(player, command);
		send("&7Made &d" + player.getName() + " &7run &d/" + command);
	}

}
