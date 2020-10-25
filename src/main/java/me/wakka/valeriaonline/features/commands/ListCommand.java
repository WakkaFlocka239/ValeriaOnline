package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Fallback;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

@Fallback("essentials")
public class ListCommand extends CustomCommand {

	public ListCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void list() {
		if (isConsole())
			runCommandAsConsole("minecraft:list");
		else
			fallback();
	}
}
