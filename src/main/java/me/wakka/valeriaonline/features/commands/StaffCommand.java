package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

// TODO
@Aliases("stafflist")
public class StaffCommand extends CustomCommand {

	public StaffCommand(CommandEvent event) {
		super(event);
	}

	@Path
	void staff() {
		send("TODO");
	}
}
