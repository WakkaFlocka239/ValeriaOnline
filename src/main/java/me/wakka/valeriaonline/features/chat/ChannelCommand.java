package me.wakka.valeriaonline.features.chat;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

import java.util.List;
import java.util.stream.Collectors;


@Aliases("ch")
public class ChannelCommand extends CustomCommand {

	public ChannelCommand(CommandEvent event) {
		super(event);
	}

	@Path("<channel>")
	void changeChannel(Channel channel) {
		runCommandAsOp("cr " + channel.getName());
	}

	@ConverterFor(Channel.class)
	Channel convertToWarp(String value) {
		Channel channel = ChannelManager.getChannel(value);
		if (channel == null)
			error("Channel " + value + " not found");
		return channel;
	}

	@TabCompleterFor(Channel.class)
	List<String> tabCompleteWarp(String filter) {
		return ChannelManager.getChannels().stream()
				.filter(channel -> {
					if (!ChannelManager.canJoin(player(), channel))
						return false;

					return channel.getPrefix().toLowerCase().startsWith(filter.toLowerCase()) ||
							channel.getName().toLowerCase().startsWith(filter.toLowerCase());
				})
				.map(Channel::getPrefix)
				.collect(Collectors.toList());
	}
}
