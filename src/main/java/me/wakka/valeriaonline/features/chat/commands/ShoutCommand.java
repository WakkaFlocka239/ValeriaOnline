package me.wakka.valeriaonline.features.chat.commands;

import me.wakka.valeriaonline.features.chat.ChatManager;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;

public class ShoutCommand extends CustomCommand {
	private Chatter chatter;

	public ShoutCommand(CommandEvent event) {
		super(event);
		chatter = new ChatService().get(player());
	}

	@Path
	void run() {
		chatter.say(ChatManager.getMainChannel(), argsString());
	}
}
