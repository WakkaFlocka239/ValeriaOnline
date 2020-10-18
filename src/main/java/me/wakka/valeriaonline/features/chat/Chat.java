package me.wakka.valeriaonline.features.chat;

import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.chat.alerts.AlertsListener;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PublicChannel;
import me.wakka.valeriaonline.utils.ColorType;
import me.wakka.valeriaonline.utils.JsonBuilder;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;

public class Chat {

	public static final String PREFIX = StringUtils.getPrefix("Chat");

	public Chat() {
		new Time.Timer("    ChatListener", () -> ValeriaOnline.registerListener(new ChatListener()));
		new Time.Timer("    AlertsListener", () -> ValeriaOnline.registerListener(new AlertsListener()));
		new Time.Timer("    addChannels", this::addChannels);
	}

	public static void shutdown() {
		new HashMap<>(new ChatService().getCache()).forEach((uuid, chatter) -> new ChatService().saveSync(chatter));
	}

	private void updateChannels() {
		Bukkit.getOnlinePlayers().stream()
				.map(player -> (Chatter) new ChatService().get(player))
				.forEach(Chatter::updateChannels);
	}

	private void addChannels() {
		for (StaticChannel channel : StaticChannel.values())
			ChatManager.addChannel(channel.getChannel());

		ChatManager.setMainChannel(StaticChannel.GLOBAL.getChannel());
	}

	public enum StaticChannel {
		GLOBAL(PublicChannel.builder()
				.name("Global")
				.nickname("G")
				.color(ChatColor.DARK_GREEN)
				.local(false)
				.crossWorld(true)
				.build()),
		LOCAL(PublicChannel.builder()
				.name("Local")
				.nickname("L")
				.color(ColorType.YELLOW.getChatColor())
				.local(true)
				.crossWorld(false)
				.build()),
		STAFF(PublicChannel.builder()
				.name("Staff")
				.nickname("S")
				.permission("group.staff")
				.color(ColorType.LIGHT_BLUE.getChatColor())
				.messageColor(ColorType.LIGHT_BLUE.getChatColor())
				.censor(false)
				.local(false)
				.crossWorld(true)
				.build());

		@Getter
		private final PublicChannel channel;

		StaticChannel(PublicChannel channel) {
			this.channel = channel;
		}
	}

	public static int getLocalRadius() {
		return ValeriaOnline.getInstance().getConfig().getInt("localRadius", 500);
	}

	// Broadcasts

	public static void broadcast(String message) {
		broadcast(message, ChatManager.getMainChannel());
	}

	public static void broadcast(String message, StaticChannel channel) {
		broadcast(message, ChatManager.getChannel(channel.name()));
	}

	public static void broadcast(String message, String channel) {
		broadcast(message, ChatManager.getChannel(channel));
	}

	public static void broadcast(String message, PublicChannel channel) {
		channel.broadcast(message);
	}

	public static void broadcast(JsonBuilder message) {
		broadcast(message, ChatManager.getMainChannel());
	}

	public static void broadcast(JsonBuilder message, StaticChannel channel) {
		broadcast(message, ChatManager.getChannel(channel.name()));
	}

	public static void broadcast(JsonBuilder message, String channel) {
		broadcast(message, ChatManager.getChannel(channel));
	}

	public static void broadcast(JsonBuilder message, PublicChannel channel) {
		channel.broadcast(message);
	}

}
