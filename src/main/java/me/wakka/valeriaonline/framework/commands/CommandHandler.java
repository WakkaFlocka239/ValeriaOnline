package me.wakka.valeriaonline.framework.commands;

import com.google.common.base.Strings;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.framework.commands.models.events.TabEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter, Listener {
	private final CustomCommand customCommand;

	CommandHandler(CustomCommand customCommand) {
		this.customCommand = customCommand;
	}

	private void call(CommandEvent event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		CommandEvent event = new CommandEvent(sender, customCommand, alias, new ArrayList<>(Arrays.asList(args)));
		if (event.callEvent())
			customCommand.execute(event);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		TabEvent event = new TabEvent(sender, customCommand, alias, new ArrayList<>(Arrays.asList(args)));

		// Remove any empty args except the last one
		boolean lastIndexIsEmpty = Strings.isNullOrEmpty(event.getArgs().get(event.getArgs().size() - 1));
		event.getArgs().removeIf(Strings::isNullOrEmpty);
		if (lastIndexIsEmpty)
			event.getArgs().add("");

		if (event.callEvent())
			return customCommand.tabComplete(event);

		return new ArrayList<>();
	}
}
