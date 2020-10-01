package me.wakka.valeriaonline.features.chat;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChannelManager {
	@Getter
	private static List<Channel> channels = new ArrayList<>();

	public ChannelManager() {
		addChannels();
	}

	public static Channel getChannel(String id) {
		Optional<Channel> channel = channels.stream().filter(_channel -> _channel.getPrefix().equalsIgnoreCase(id)).findFirst();
		if (!channel.isPresent())
			channel = channels.stream().filter(_channel -> _channel.getName().equalsIgnoreCase(id)).findFirst();

		return channel.orElse(null);
	}

	public static boolean canJoin(Player player, Channel channel) {
		if (channel.getPermission() == null)
			return true;

		return player.hasPermission(channel.getPermission());
	}

	private void addChannels() {
		Channel global = new Channel("G", "Global", null);
		Channel local = new Channel("L", "Local", null);

		channels = Arrays.asList(global, local);
	}
}
