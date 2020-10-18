package me.wakka.valeriaonline.features.chat.commands;


import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Censor;
import me.wakka.valeriaonline.features.chat.events.ChatEvent;
import me.wakka.valeriaonline.features.chat.events.PublicChatEvent;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.PublicChannel;

import java.util.HashSet;

@Permission("group.seniorstaff")
public class CensorCommand extends CustomCommand {

	public CensorCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("process <channel> <message...>")
	void process(PublicChannel channel, String message) {
		ChatEvent event = new PublicChatEvent(new ChatService().get(player()), channel, message, new HashSet<>());
		Censor.process(event);
		send(PREFIX + "Processed message:" + (event.isCancelled() ? " &c(Cancelled)" : ""));
		send("&eOriginal: &f" + message);
		send("&eResult: &f" + event.getMessage());
	}

	@Path("reload")
	void reload() {
		Censor.reloadConfig();
		send(PREFIX + Censor.getCensorItems().size() + " censor items loaded from disk");
	}

	@Path("debug")
	void debug() {
		send(Censor.getCensorItems().toString());
	}

}
