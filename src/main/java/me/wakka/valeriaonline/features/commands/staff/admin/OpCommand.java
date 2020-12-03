package me.wakka.valeriaonline.features.commands.staff.admin;

import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.features.chat.Chat.StaticChannel;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;

@Permission("group.dm")
public class OpCommand extends CustomCommand {

	public OpCommand(CommandEvent event) {
		super(event);
	}

	@Path("<player>")
	public void op(Player player) {
		String oper = player().getName();
		String opee = player.getName();

		if (!player.hasPermission("group.staff"))
			error(opee + " is not staff");

		if (player.isOp())
			error(opee + " is already op");

		player.setOp(true);
		if (player.equals(player()))
			Chat.broadcast(PREFIX + oper + " opped themselves", StaticChannel.ADMIN);
		else
			Chat.broadcast(PREFIX + oper + " opped " + opee, StaticChannel.ADMIN);

		if (player.isOnline() && !player.equals(player()))
			send(player.getPlayer(), PREFIX + "You are now op");

	}

	@Path("list")
	public void list() {
		Set<OfflinePlayer> ops = Bukkit.getOperators();
		if (ops.isEmpty())
			error("There are no server operators");

		send(PREFIX + "Ops:");
		for (OfflinePlayer operator : ops)
			send(" &7- &b" + operator.getName());
	}
}
