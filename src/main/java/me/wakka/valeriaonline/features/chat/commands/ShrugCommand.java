package me.wakka.valeriaonline.features.chat.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;

public class ShrugCommand extends CustomCommand {
	private Chatter chatter;

	public ShrugCommand(CommandEvent event) {
		super(event);
		chatter = new ChatService().get(player());
	}

	@Path()
	void shrug() {
		chatter.say(argsString() + " ¯\\_(ツ)_/¯");
	}
}
