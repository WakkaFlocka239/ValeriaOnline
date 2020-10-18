package me.wakka.valeriaonline.features.chat.alerts;

import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.features.chat.events.MinecraftChatEvent;
import me.wakka.valeriaonline.models.alerts.AlertsService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PrivateChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AlertsListener implements Listener {

	@NotNull
	public Set<Chatter> getEveryoneElse(Chatter origin, Set<Chatter> recipients) {
		return recipients.stream().filter(chatter -> !chatter.equals(origin)).collect(Collectors.toSet());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChat(MinecraftChatEvent event) {
		Set<Chatter> everyoneElse = getEveryoneElse(event.getChatter(), event.getRecipients());
		if (event.getChannel() instanceof PrivateChannel) {
			everyoneElse.forEach(Chatter::playSound);
		} else
			tryAlerts(everyoneElse, event.getMessage());
	}

	public void tryAlerts(Set<Chatter> recipients, String message) {
		List<String> uuids = recipients.stream().map(Chatter::getUuid).collect(Collectors.toList());
		new AlertsService().getAll(uuids).forEach(alerts -> alerts.tryAlerts(message));
	}

}
