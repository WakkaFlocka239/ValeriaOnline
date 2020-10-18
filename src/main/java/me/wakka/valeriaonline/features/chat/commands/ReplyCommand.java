package me.wakka.valeriaonline.features.chat.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;

@Aliases("r")
public class ReplyCommand extends CustomCommand {
	private Chatter chatter;

	public ReplyCommand(@NonNull CommandEvent event) {
		super(event);
		PREFIX = Chat.PREFIX;
		chatter = new ChatService().get(player());
	}

	@Path("[message...]")
	void reply(String message) {
		if (chatter.getLastPrivateMessage() == null)
			error("No one has messaged you");

		if (isNullOrEmpty(message))
			chatter.setActiveChannel(chatter.getLastPrivateMessage());
		else
			chatter.say(chatter.getLastPrivateMessage(), message);
	}
}
