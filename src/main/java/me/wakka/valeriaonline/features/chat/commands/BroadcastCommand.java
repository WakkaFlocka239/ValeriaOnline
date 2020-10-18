package me.wakka.valeriaonline.features.chat.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

@Permission("group.admin")
public class BroadcastCommand extends CustomCommand {

	public BroadcastCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("<message...>")
	void run(String message) {
		Chat.broadcast(message);
	}

}
