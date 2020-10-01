package me.wakka.valeriaonline.features.chat;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

public class ShrugCommand extends CustomCommand {

	public ShrugCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void shrug() {
		player().chat(argsString() + " ¯\\_(ツ)_/¯");
	}
}
