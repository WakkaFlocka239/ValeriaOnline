package me.wakka.valeriaonline.features.chat.commands;

import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

@Permission("group.admin")
public class AnnouncementCommand extends CustomCommand {
	private static final String line = "&7&m                                                  ";
	private static final String header = "&e&lANNOUNCEMENT";
	private static final String textColor = "&f";

	public AnnouncementCommand(CommandEvent event) {
		super(event);
	}

	@Path("<message...>")
	void announcement(String message) {
		Chat.broadcast(line + "\n"
				+ header + "\n"
				+ textColor + message + "\n"
				+ line);
	}
}
