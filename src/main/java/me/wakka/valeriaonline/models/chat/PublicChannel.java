package me.wakka.valeriaonline.models.chat;

import lombok.Builder;
import lombok.Data;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.utils.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.wakka.valeriaonline.utils.StringUtils.stripColor;

@Data
@Builder
public class PublicChannel implements Channel {
	private String name;
	private String nickname;
	private ChatColor color;
	private ChatColor messageColor;
	@Builder.Default
	private boolean censor = true;
	private boolean isPrivate;
	private boolean local;
	private boolean crossWorld;
	private String permission;

	public ChatColor getMessageColor() {
		return messageColor == null ? Channel.super.getMessageColor() : messageColor;
	}

	@Override
	public String getAssignMessage(Chatter chatter) {
		return "Now chatting in " + color + name;
	}

	public String getDiscordChatterFormat(Chatter chatter) {
		Player player = chatter.getPlayer();
		String channel = nickname.toUpperCase();
		String rank = PrefixTags.getGroupFormat(player, false);

		return "**[" + channel + "] " + rank + "" + player.getName() + " >** ";
	}

	public String getChatterFormat(Chatter chatter) {
		Player player = chatter.getPlayer();
		return "&7[" + color + ChatColor.BOLD + nickname.toUpperCase() + "&7] " +
				PrefixTags.getActiveTagFormat(player) + "&7" + player.getName() + " "
				+ color + ChatColor.BOLD + "> " + getMessageColor();
	}

	public Set<Chatter> getRecipients(Chatter chatter) {
		List<Player> recipients = new ArrayList<>();
		if (local)
			recipients.addAll(chatter.getPlayer().getLocation().getNearbyPlayers(Chat.getLocalRadius()));
		else if (crossWorld)
			recipients.addAll(Bukkit.getOnlinePlayers());
		else
			recipients.addAll(chatter.getPlayer().getWorld().getPlayers());

		return recipients.stream()
				.map(player -> new ChatService().get(player))
				.filter(_chatter -> _chatter.canJoin(this))
				.filter(_chatter -> _chatter.hasJoined(this))
				.collect(Collectors.toSet());
	}

	public void broadcast(String message) {
		Bukkit.getConsoleSender().sendMessage(stripColor(message));
		Bukkit.getOnlinePlayers().stream()
				.map(player -> (Chatter) new ChatService().get(player))
				.filter(chatter -> chatter.hasJoined(this))
				.forEach(chatter -> chatter.send(message));
	}


	public void broadcast(JsonBuilder builder) {
		Bukkit.getConsoleSender().spigot().sendMessage(builder.build());
		Bukkit.getOnlinePlayers().stream()
				.map(player -> (Chatter) new ChatService().get(player))
				.filter(chatter -> chatter.hasJoined(this))
				.forEach(chatter -> chatter.send(builder));
	}

	public void broadcast(Chatter chatter, JsonBuilder builder) {
		Bukkit.getConsoleSender().spigot().sendMessage(builder.build());
		getRecipients(chatter).forEach(_chatter -> _chatter.send(builder));
	}

	public String getPermission() {
		if (permission == null)
			return "chat.use." + name.toLowerCase();
		return permission;
	}

}
