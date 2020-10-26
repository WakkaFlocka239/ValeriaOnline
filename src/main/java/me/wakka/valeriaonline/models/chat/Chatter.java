package me.wakka.valeriaonline.models.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.features.chat.ChatManager;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
import me.wakka.valeriaonline.utils.JsonBuilder;
import me.wakka.valeriaonline.utils.SoundUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;
import static me.wakka.valeriaonline.utils.StringUtils.trimFirst;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "chat")
public class Chatter {
	@NonNull
	private String uuid;
	private Channel activeChannel;
	private Set<PublicChannel> joinedChannels = new HashSet<>();
	private PrivateChannel lastPrivateMessage;

	public OfflinePlayer getOfflinePlayer() {
		return Utils.getPlayer(uuid);
	}

	public Player getPlayer() {
		return getOfflinePlayer().getPlayer();
	}

	public void playSound() {
		if (getOfflinePlayer().isOnline())
			SoundUtils.Jingle.PING.play(getPlayer());
	}

	public void say(String message) {
		if (message.startsWith("/"))
			Utils.runCommand(getPlayer(), trimFirst(message));
		else
			say(getActiveChannel(), message);
	}

	public void say(Channel channel, String message) {
		ChatManager.process(this, channel, message);
	}

	public void setActiveChannel(Channel channel) {
		if (channel == null)
			Utils.send(getPlayer(), Chat.PREFIX + "You are no longer speaking in a channel");
		else {
			if (channel instanceof PublicChannel)
				join((PublicChannel) channel);
			Utils.send(getPlayer(), Chat.PREFIX + channel.getAssignMessage(this));
		}
		this.activeChannel = channel;
	}

	public boolean canJoin(PublicChannel channel) {
		if (getPlayer() == null) {
			return false;
		}

		boolean hasPerm;
		if (getPlayer() != null && getPlayer().isOnline())
			hasPerm = getPlayer().hasPermission(channel.getPermission());
		else
			hasPerm = ValeriaOnline.getPerms().playerHas(null, getPlayer(), channel.getPermission());

		return hasPerm;
	}

	public boolean hasJoined(PublicChannel channel) {
		if (!canJoin(channel))
			return false;
		if (joinedChannels == null)
			joinedChannels = new HashSet<>();
		return joinedChannels.contains(channel);
	}

	public void join(PublicChannel channel) {
		if (!canJoin(channel))
			throw new InvalidInputException("You do not have permission to join that channel");
		if (joinedChannels == null)
			joinedChannels = new HashSet<>();
		joinedChannels.add(channel);
	}

	public void leave(PublicChannel channel) {
		if (joinedChannels == null)
			joinedChannels = new HashSet<>();
		joinedChannels.remove(channel);
		if (activeChannel == channel && channel != ChatManager.getMainChannel())
			setActiveChannel(ChatManager.getMainChannel());
	}

	public void send(String message) {
		if (getPlayer() != null && getPlayer().isOnline())
			getPlayer().sendMessage(colorize(message));
	}

	public void send(JsonBuilder message) {
		if (getPlayer() != null && getPlayer().isOnline())
			getPlayer().spigot().sendMessage(message.build());
	}

	public void updateChannels() {
		Tasks.wait(2, () -> {
			if (getPlayer() != null && getPlayer().isOnline())
				ChatManager.getChannels().forEach(channel -> {
					if (canJoin(channel)) {
						if (!hasJoined(channel))
							join(channel);
					} else {
						leave(channel);
					}
				});
		});
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chatter chatter = (Chatter) o;
		return Objects.equals(getOfflinePlayer().getUniqueId(), chatter.getOfflinePlayer().getUniqueId());
	}

}
