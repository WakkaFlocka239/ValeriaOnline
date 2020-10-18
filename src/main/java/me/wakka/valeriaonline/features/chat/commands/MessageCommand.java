package me.wakka.valeriaonline.features.chat.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PrivateChannel;

@Aliases({"m", "msg", "w", "whisper", "t", "tell", "pm", "dm"})
public class MessageCommand extends CustomCommand {
	private Chatter chatter;

	public MessageCommand(@NonNull CommandEvent event) {
		super(event);
		PREFIX = Chat.PREFIX;
		chatter = new ChatService().get(player());
	}

	@Path("<player> [message...]")
	void message(Chatter to, String message) {
		if (chatter == to)
			error("You cannot message yourself");

		PrivateChannel dm = new PrivateChannel(chatter, to);
		if (isNullOrEmpty(message))
			chatter.setActiveChannel(dm);
		else
			chatter.say(dm, message);
	}
}
