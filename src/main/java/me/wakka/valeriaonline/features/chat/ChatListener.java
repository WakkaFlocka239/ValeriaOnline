package me.wakka.valeriaonline.features.chat;

import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.features.chat.events.ChatEvent;
import me.wakka.valeriaonline.framework.commands.Commands;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.utils.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.regex.Pattern;

import static me.wakka.valeriaonline.utils.StringUtils.right;
import static me.wakka.valeriaonline.utils.Utils.runCommand;

@NoArgsConstructor
public class ChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;

		Chatter chatter = new ChatService().get(event.getPlayer());
		Tasks.sync(() -> {
			// Prevents "t/command"
			if (Pattern.compile("^[tT]" + Commands.getPattern() + ".*").matcher(event.getMessage()).matches())
				runCommand(event.getPlayer(), right(event.getMessage(), event.getMessage().length() - 2));
			else
				chatter.say(event.getMessage());
		});
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(ChatEvent event) {
		Censor.process(event);
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Chatter chatter = new ChatService().get(event.getPlayer());
		chatter.updateChannels();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Chatter chatter = new ChatService().get(event.getPlayer());
		if (chatter.getActiveChannel() == null)
			chatter.setActiveChannel(ChatManager.getMainChannel());
		chatter.updateChannels();
	}
}
