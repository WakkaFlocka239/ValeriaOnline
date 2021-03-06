package me.wakka.valeriaonline.features.chat.events;

import me.wakka.valeriaonline.models.chat.Channel;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public abstract class ChatEvent extends Event implements Cancellable {

	public abstract Chatter getChatter();

	public abstract String getOrigin();

	public abstract String getMessage();

	public abstract void setMessage(String message);

	public abstract Channel getChannel();

	public abstract Set<Chatter> getRecipients();

	public boolean wasSentTo(Player player) {
		return wasSentTo((Chatter) new ChatService().get(player));
	}

	public boolean wasSentTo(Chatter chatter) {
		return getRecipients().contains(chatter);
	}

	public void respond(String response) {
		getRecipients().forEach(chatter -> chatter.send(response));
	}

	//<editor-fold desc="Boilerplate Bukkit">
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	//</editor-fold>

}
