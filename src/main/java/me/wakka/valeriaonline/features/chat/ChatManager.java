package me.wakka.valeriaonline.features.chat;

import lombok.Getter;
import lombok.Setter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.chat.events.PrivateChatEvent;
import me.wakka.valeriaonline.features.chat.events.PublicChatEvent;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.PlayerNotOnlineException;
import me.wakka.valeriaonline.models.chat.Channel;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PrivateChannel;
import me.wakka.valeriaonline.models.chat.PublicChannel;
import me.wakka.valeriaonline.utils.JsonBuilder;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static me.wakka.valeriaonline.features.chat.Chat.PREFIX;
import static me.wakka.valeriaonline.utils.StringUtils.stripColor;

public class ChatManager {
	@Getter
	private static List<PublicChannel> channels = new ArrayList<>();

	@Getter
	@Setter
	private static PublicChannel mainChannel;

	public static PublicChannel getChannel(String id) {
		Optional<PublicChannel> channel = channels.stream().filter(_channel -> _channel.getNickname().equalsIgnoreCase(id)).findFirst();
		if (!channel.isPresent())
			channel = channels.stream().filter(_channel -> _channel.getName().equalsIgnoreCase(id)).findFirst();
		if (!channel.isPresent())
			channel = channels.stream().filter(_channel -> _channel.getName().toLowerCase().startsWith(id.toLowerCase())).findFirst();

		return channel.orElseThrow(() -> new InvalidInputException("Channel not found"));
	}

	public static void addChannel(PublicChannel channel) {
		channels.add(channel);
	}

	public static void process(Chatter chatter, Channel channel, String message) {
		if (chatter == null || message == null)
			return;

		message = message.trim();

		if (!chatter.getPlayer().hasPermission("group.admin"))
			message = stripColor(message);

		if (message.length() == 0)
			return;

		if (channel == null) {
			chatter.send(PREFIX + "&cYou are not speaking in a channel. &7Use &c/ch g &7to return to Global chat.");
			return;
		}

		Set<Chatter> recipients = channel.getRecipients(chatter);
		if (channel instanceof PublicChannel) {
			PublicChatEvent event = new PublicChatEvent(chatter, (PublicChannel) channel, message, recipients);
			if (event.callEvent())
				process(event);
		} else if (channel instanceof PrivateChannel) {
			PrivateChatEvent event = new PrivateChatEvent(chatter, (PrivateChannel) channel, message, recipients);
			if (event.callEvent())
				process(event);
		}
	}

	public static void process(PublicChatEvent event) {
		if (!event.wasSeen())
			Tasks.wait(1, () -> event.getChatter().send(PREFIX + "No one can hear you! Type &c/ch g &7to talk globally"));

		JsonBuilder json = new JsonBuilder()
				.next(event.getChannel().getChatterFormat(event.getChatter()))
				.next(event.getChannel().getMessageColor() + event.getMessage());

		String discordJson = stripColor(event.getChannel().getDiscordChatterFormat(event.getChatter()) + event.getMessage());


		event.getRecipients().forEach(recipient -> recipient.send(json));
		try {
			ValeriaOnline.getDiscordSRV().getMainTextChannel().sendMessage(discordJson).submit();
		} catch (Exception ignored) {
		}

		Bukkit.getConsoleSender().sendMessage(stripColor(json.toString()));
	}

	public static void process(PrivateChatEvent event) {
		Set<String> othersNames = event.getChannel().getOthersNames(event.getChatter());

		JsonBuilder to = new JsonBuilder("&7&l[&bPM&7&l] &dTo &7" + String.join(", ", othersNames) + " &b&l> ")
				.next(event.getChannel().getMessageColor() + event.getMessage());
		JsonBuilder from = new JsonBuilder("&7&l[&bPM&7&l] &dFrom &7" + event.getChatter().getOfflinePlayer().getName() + " &b&l> ")
				.next(event.getChannel().getMessageColor() + event.getMessage());

		int seen = 0;
		for (Chatter recipient : event.getRecipients()) {
			recipient.setLastPrivateMessage(event.getChannel());

			if (!recipient.equals(event.getChatter())) {
				boolean canSee = Utils.canSee(event.getChatter().getOfflinePlayer(), recipient.getOfflinePlayer());
				String notOnline = new PlayerNotOnlineException(recipient.getOfflinePlayer()).getMessage();
				if (!recipient.getOfflinePlayer().isOnline()) {
					recipientNotOnline(PREFIX + notOnline, event);
				} else {
					recipient.send(from);
					if (canSee)
						++seen;
					else
						recipientNotOnline(PREFIX + notOnline, event);
				}
			}
		}

		if (seen > 0)
			event.getChatter().send(to);

		Bukkit.getConsoleSender().sendMessage(event.getChatter().getOfflinePlayer().getName() + " -> " + String.join(", ", othersNames) + ": " + event.getMessage());
	}

	private static void recipientNotOnline(String message, PrivateChatEvent event) {
		event.getChatter().send(message + ", switching you to global chat");
		event.getChatter().setActiveChannel(Chat.StaticChannel.GLOBAL.getChannel());
	}

}
