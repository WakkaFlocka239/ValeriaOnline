package me.wakka.valeriaonline.framework.commands.models.events;

import lombok.Data;
import lombok.NonNull;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.exceptions.CustomException;
import me.wakka.valeriaonline.framework.exceptions.MustBeIngameException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;

@Data
public class CommandEvent extends Event implements Cancellable {
	@NonNull
	private CommandSender sender;
	@NonNull
	private CustomCommand command;
	@NonNull
	private String aliasUsed;
	@NonNull
	private List<String> args;
	private Method method;
	private String usage;
	private boolean cancelled = false;
	private HandlerList handlers = new HandlerList();

	public void reply(String message) {
		sender.sendMessage(colorize(message));
	}

	public Player getPlayer() throws CustomException {
		if (!(sender instanceof Player))
			throw new MustBeIngameException();

		return (Player) sender;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public String getArgsString() {
		return String.join(" ", args);
	}

	public void setUsage(Method method) {
		this.method = method;
		Path path = method.getAnnotation(Path.class);
		if (path != null) {
			this.usage = path.value();
			Description desc = method.getAnnotation(Description.class);
			if (desc != null)
				this.usage += " &7- " + desc.value();
		}
	}

	public String getUsageMessage() {
		return "Correct usage: /" + aliasUsed + " " + usage;
	}

	public void handleException(Throwable ex) {
		if (ex.getCause() != null && ex.getCause() instanceof CustomException)
			reply(command.getPrefix() + "&c" + ex.getCause().getMessage());
		else if (ex instanceof CustomException)
			reply(command.getPrefix() + "&c" + ex.getMessage());
		else if (ex instanceof IllegalArgumentException && ex.getMessage() != null && ex.getMessage().contains("type mismatch"))
			reply(command.getPrefix() + "&c" + getUsageMessage());
		else {
			reply("&cAn internal error occurred while attempting to execute this command");

			if (ex.getCause() != null && ex instanceof InvocationTargetException)
				ex.getCause().printStackTrace();
			else
				ex.printStackTrace();
		}
	}
}
