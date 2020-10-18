package me.wakka.valeriaonline.features.chat.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PrivateChannel;

import java.util.Set;

@Data
@AllArgsConstructor
public class PrivateChatEvent extends MinecraftChatEvent {
	private final Chatter chatter;
	private PrivateChannel channel;
	private String message;

	private Set<Chatter> recipients;

	@Override
	public boolean wasSeen() {
		return true;
	}

}
