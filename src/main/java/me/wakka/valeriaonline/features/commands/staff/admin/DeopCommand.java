package me.wakka.valeriaonline.features.commands.staff.admin;

import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.features.chat.Chat.StaticChannel;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.ServerOperator;

import java.util.List;
import java.util.stream.Collectors;

public class DeopCommand extends CustomCommand {

	public DeopCommand(CommandEvent event) {
		super(event);
		PREFIX = StringUtils.getPrefix("Op");
	}

	@Path("<player>")
	@Permission("group.admin")
	public void deop(ServerOperator op) {
		OfflinePlayer player = (OfflinePlayer) op;

		String oper = player().getName();
		String opee = player.getName();
		if (!player.isOp())
			error(opee + " is not op");

		player.setOp(false);
		if (player.equals(player()))
			Chat.broadcast(PREFIX + oper + " deopped themselves", StaticChannel.ADMIN);
		else
			Chat.broadcast(PREFIX + oper + " deopped " + opee, StaticChannel.ADMIN);

		if (player.isOnline() && !player.equals(player()))
			send(player.getPlayer(), PREFIX + "You are no longer op");
	}

	@ConverterFor(ServerOperator.class)
	OfflinePlayer convertToServerOperator(String value) {
		return convertToOfflinePlayer(value);
	}

	@TabCompleterFor(ServerOperator.class)
	List<String> tabCompleteServerOperator(String filter) {
		return Bukkit.getOperators().stream()
				.map(OfflinePlayer::getName)
				.filter(name -> name != null && name.toLowerCase().startsWith(filter.toLowerCase()))
				.collect(Collectors.toList());
	}
}
