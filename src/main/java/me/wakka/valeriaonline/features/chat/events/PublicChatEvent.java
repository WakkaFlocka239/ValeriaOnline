package me.wakka.valeriaonline.features.chat.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PublicChannel;
import me.wakka.valeriaonline.utils.Utils;

import java.util.Set;

@Data
@AllArgsConstructor
public class PublicChatEvent extends MinecraftChatEvent {
	private final Chatter chatter;
	private PublicChannel channel;
	private String message;

	private Set<Chatter> recipients;

	public boolean wasSeen() {
		return recipients.stream().anyMatch(recipient -> chatter.getOfflinePlayer() != recipient.getOfflinePlayer() &&
				Utils.canSee(chatter.getOfflinePlayer(), recipient.getOfflinePlayer()));
	}

}
