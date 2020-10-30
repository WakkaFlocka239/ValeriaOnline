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

	// Prevents Multicraft spam
	@Path()
	void list() {
		if (!isConsole())
			fallback();
	}
}
