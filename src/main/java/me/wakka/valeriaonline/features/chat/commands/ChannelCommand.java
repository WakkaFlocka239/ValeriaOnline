package me.wakka.valeriaonline.features.chat.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.features.chat.ChatManager;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Redirects;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.PlayerNotOnlineException;
import me.wakka.valeriaonline.models.chat.Channel;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PublicChannel;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.stream.Collectors;


@Aliases({"ch", "chat"})
@Redirects.Redirect(from = "/qm", to = "/ch qm")
public class ChannelCommand extends CustomCommand {
	private Chatter chatter;

	public ChannelCommand(@NonNull CommandEvent event) {
		super(event);
		PREFIX = Chat.PREFIX;
		chatter = new ChatService().get(player());
	}

	@Path("<channel> [message...]")
	void changeChannel(PublicChannel channel, String message) {
		if (channel.equals(chatter.getActiveChannel()))
			error("You are already in that channel");

		if (isNullOrEmpty(message))
			chatter.setActiveChannel(channel);
		else
			quickMessage(channel, message);
	}

	@Path("list [filter]")
	void list(String filter) {
		ChatManager.getChannels().forEach(channel -> {
			if (!isNullOrEmpty(filter) && !channel.getName().toLowerCase().startsWith(filter))
				return;

			if (chatter.canJoin(channel))
				send(channel.getColor() + "[" + channel.getNickname().toUpperCase() + "] " + channel.getName() +
						(chatter.hasJoined(channel) ? chatter.getActiveChannel().equals(channel) ? " &a(Active)" : " &7(Joined)" : ""));
		});
	}

	@Path("qm <channel> <message...>")
	void quickMessage(PublicChannel channel, String message) {
		chatter.say(channel, message);
	}

	@Path("join <channel>")
	void join(PublicChannel channel) {
		chatter.join(channel);
		send(PREFIX + "Joined " + channel.getColor() + channel.getName());
	}

	@Path("leave <channel>")
	void leave(PublicChannel channel) {
		chatter.leave(channel);
		send(PREFIX + "Left " + channel.getColor() + channel.getName());
	}

	@ConverterFor({Channel.class, PublicChannel.class})
	PublicChannel convertToChannel(String value) {
		return ChatManager.getChannel(value);
	}

	@TabCompleterFor({Channel.class, PublicChannel.class})
	List<String> tabCompleteChannel(String filter) {
		return ChatManager.getChannels().stream()
				.filter(channel -> {
					if (!new ChatService().get(player()).canJoin(channel))
						return false;
					return channel.getNickname().toLowerCase().startsWith(filter.toLowerCase()) ||
							channel.getName().toLowerCase().startsWith(filter.toLowerCase());
				})
				.map(PublicChannel::getNickname)
				.collect(Collectors.toList());
	}

	@ConverterFor(Chatter.class)
	Chatter convertToChatter(String value) {
		OfflinePlayer player = convertToOfflinePlayer(value);
		if (!player.isOnline()) {
			throw new PlayerNotOnlineException(player);
		}
		return new ChatService().get(player);
	}

	@TabCompleterFor(Chatter.class)
	List<String> tabCompleteChatter(String filter) {
		return tabCompletePlayer(filter);
	}
}
