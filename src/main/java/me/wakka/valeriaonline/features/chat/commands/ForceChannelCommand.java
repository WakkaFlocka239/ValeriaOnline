package me.wakka.valeriaonline.features.chat.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PublicChannel;

@Permission("group.staff")
@Aliases("fc")
public class ForceChannelCommand extends CustomCommand {

	public ForceChannelCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("<player> <channel>")
	void forceChannel(Chatter chatter, PublicChannel channel) {
		chatter.setActiveChannel(channel);
		send("&7Forced &d" + chatter.getOfflinePlayer().getName() + " &7to " + channel.getColor() + channel.getName());
	}

}
