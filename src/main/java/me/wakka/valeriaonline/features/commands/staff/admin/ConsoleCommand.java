package me.wakka.valeriaonline.features.commands.staff.admin;

import lombok.NonNull;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

@Permission("group.admin")
public class ConsoleCommand extends CustomCommand {

	public ConsoleCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("[command...]")
	void run(String command) {
		runCommandAsConsole(command);
		send(PREFIX + "Ran command &c/" + command);
	}

}
