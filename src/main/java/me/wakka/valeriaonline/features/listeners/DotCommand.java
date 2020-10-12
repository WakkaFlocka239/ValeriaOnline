package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.framework.commands.Commands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DotCommand implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Pattern pattern = Pattern.compile("( |^)." + Commands.getPattern());
		Matcher matcher = pattern.matcher(message);
		while (matcher.find()) {
			String group = matcher.group();
			String replace = group.replace("./", "/");
			message = message.replace(group, replace);
		}
		event.setMessage(message);
	}
}
