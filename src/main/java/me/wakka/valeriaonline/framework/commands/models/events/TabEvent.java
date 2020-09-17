package me.wakka.valeriaonline.framework.commands.models.events;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.exceptions.NoPermissionException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TabEvent extends CommandEvent{

	public TabEvent(CommandSender sender, CustomCommand command, String aliasUsed, List<String> args) {
		super(sender, command, aliasUsed, args);
	}

	@Override
	public void handleException(Throwable ex) {
		if (ex instanceof NoPermissionException)
			return;
		ex.printStackTrace();
	}
}
