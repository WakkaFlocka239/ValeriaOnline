package me.wakka.valeriaonline.features.chat;

import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.chat.alerts.AlertsListener;
import me.wakka.valeriaonline.framework.features.Feature;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PublicChannel;
import me.wakka.valeriaonline.utils.ColorType;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.JsonBuilder;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Time;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class Chat extends Feature {

	public static final String PREFIX = StringUtils.getPrefix("Chat");

	@Override
	public void startup() {
		new Time.Timer("    addChannels", this::addChannels);
		new Time.Timer("    ChatListener", () -> ValeriaOnline.registerListener(new ChatListener()));
		new Time.Timer("    AlertsListener", () -> ValeriaOnline.registerListener(new AlertsListener()));
	}

	@Override
	public void shutdown() {
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
				.color(ColorType.GREEN.getChatColor())
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
				.build()),
		ADMIN(PublicChannel.builder()
				.name("Admin")
				.nickname("A")
				.permission("group.admin")
				.color(ChatColor.of("#fc8003"))
				.messageColor(ChatColor.of("#fc8003"))
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
		return ConfigUtils.getSettings().getInt("localRadius", 500);
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

	public static void broadcastIngame(String message) {
		broadcastIngame(message, ChatManager.getMainChannel());
	}

	public static void broadcastIngame(String message, StaticChannel channel) {
		broadcastIngame(message, ChatManager.getChannel(channel.name()));
	}

	public static void broadcastIngame(String message, String channel) {
		broadcastIngame(message, ChatManager.getChannel(channel));
	}

	public static void broadcastIngame(String message, PublicChannel channel) {
		channel.broadcastIngame(message);
	}

	public static void broadcastIngame(JsonBuilder message) {
		broadcastIngame(message, ChatManager.getMainChannel());
	}

	public static void broadcastIngame(JsonBuilder message, StaticChannel channel) {
		broadcastIngame(message, ChatManager.getChannel(channel.name()));
	}

	public static void broadcastIngame(JsonBuilder message, String channel) {
		broadcastIngame(message, ChatManager.getChannel(channel));
	}

	public static void broadcastIngame(JsonBuilder message, PublicChannel channel) {
		channel.broadcastIngame(message);
	}

//	public static void broadcastDiscord(String message) {
//		broadcastDiscord(message, ChatManager.getMainChannel());
//	}
//
//	public static void broadcastDiscord(String message, StaticChannel channel) {
//		broadcastDiscord(message, ChatManager.getChannel(channel.name()));
//	}
//
//	public static void broadcastDiscord(String message, String channel) {
//		broadcastDiscord(message, ChatManager.getChannel(channel));
//	}
//
//	public static void broadcastDiscord(String message, PublicChannel channel) {
//		channel.broadcastDiscord(message);
//	}
//
//	public static void broadcastDiscord(JsonBuilder message) {
//		broadcastDiscord(message, ChatManager.getMainChannel());
//	}
//
//	public static void broadcastDiscord(JsonBuilder message, StaticChannel channel) {
//		broadcastDiscord(message, ChatManager.getChannel(channel.name()));
//	}
//
//	public static void broadcastDiscord(JsonBuilder message, String channel) {
//		broadcastDiscord(message, ChatManager.getChannel(channel));
//	}
//
//	public static void broadcastDiscord(JsonBuilder message, PublicChannel channel) {
//		channel.broadcastDiscord(message);
//	}
}
