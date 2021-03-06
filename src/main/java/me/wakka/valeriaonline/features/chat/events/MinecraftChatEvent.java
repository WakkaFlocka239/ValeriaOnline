package me.wakka.valeriaonline.features.chat.events;

public abstract class MinecraftChatEvent extends ChatEvent {

	public abstract boolean wasSeen();

	@Override
	public String getOrigin() {
		return getChatter().getOfflinePlayer().getName();
	}

}
