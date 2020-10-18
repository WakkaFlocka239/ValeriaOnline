package me.wakka.valeriaonline.models.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.ChatManager;
import me.wakka.valeriaonline.utils.Utils;

import javax.persistence.Table;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Table(name = "chat")
public class DatabaseChatter {
	@NonNull
	private String uuid;
	private String activePublicChannel;
	private String activePrivateChannel;
	private String joinedChannels;
	private String lastPrivateMessage;

	public DatabaseChatter(Chatter chatter) {
		uuid = chatter.getUuid();

		if (chatter.getActiveChannel() instanceof PublicChannel)
			activePublicChannel = ((PublicChannel) chatter.getActiveChannel()).getName();
		else if (chatter.getActiveChannel() instanceof PrivateChannel) {
			List<String> list = getRecipients((PrivateChannel) chatter.getActiveChannel());
			activePrivateChannel = String.join(",", list);
		}

		if (chatter.getJoinedChannels() != null) {
			List<String> list = chatter.getJoinedChannels().stream().map(PublicChannel::getName).collect(Collectors.toList());
			joinedChannels = String.join(",", list);
		}

		List<String> list = getRecipients(chatter.getLastPrivateMessage());
		if (!Utils.isNullOrEmpty(list))
			lastPrivateMessage = String.join(",", list);
		else
			lastPrivateMessage = null;
	}

	public Chatter deserialize() {
		Channel activeChannel = null;
		Set<PublicChannel> joinedChannels = new HashSet<>();
		PrivateChannel lastPrivateMessage = null;
		if (this.activePublicChannel != null)
			activeChannel = ChatManager.getChannel(this.activePublicChannel);
		else if (this.activePrivateChannel != null)
			activeChannel = new PrivateChannel(Arrays.asList(this.lastPrivateMessage.split(",")));
		if (this.joinedChannels != null) {
			List<String> list = Arrays.asList(this.joinedChannels.split(","));
			joinedChannels = list.stream().map(ChatManager::getChannel).collect(Collectors.toSet());
		}
		if (this.lastPrivateMessage != null) {
			List<String> list = Arrays.asList(this.lastPrivateMessage.split(","));
			lastPrivateMessage = new PrivateChannel(list);
		}

		return new Chatter(uuid, activeChannel, joinedChannels, lastPrivateMessage);
	}

	public List<String> getRecipients(PrivateChannel privateChannel) {
		if (privateChannel != null)
			return privateChannel.getRecipients().stream()
					.map(Chatter::getUuid)
					.collect(Collectors.toList());
		return null;
	}
}
