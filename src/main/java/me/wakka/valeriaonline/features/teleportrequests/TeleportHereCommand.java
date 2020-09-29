package me.wakka.valeriaonline.features.teleportrequests;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.entity.Player;

@Aliases({"tpahere"})
public class TeleportHereCommand extends CustomCommand {
	public static final String PREFIX = TeleportCommand.PREFIX;
	private static final double cost = TeleportCommand.COST;

	public TeleportHereCommand(CommandEvent event) {
		super(event);
	}

	@Path("<player>")
	void teleportHere(Player target) {
		if (target == player())
			error(player(), PREFIX + "&cYou cannot poof to yourself");

		Request request = new Request(player(), target, Request.TeleportType.TELEPORT_HERE);
		Requests.add(request);

		send(player(), json(PREFIX + "&dtphere &7request sent to &d" + target.getName() + "&7. ")
				.next("&cClick to cancel")
				.command("/teleport cancel"));

		send(target, PREFIX + "&d" + player().getName() + " &7is asking you to tp to them.");
		send(target, json("&7  Click one  ||  &a&lAccept")
				.command("/teleport confirm")
				.hover("&dClick &7to accept")
				.group()
				.next("  &7||  &7")
				.group()
				.next("&c&lDeny")
				.command("/teleport deny")
				.hover("&dClick &7to deny.")
				.group()
				.next("&7  ||"));
	}

	@Path("cancel")
	void cancel() {
		new TeleportCommand().cancel(player());
	}

	@Path("accept")
	void accept() {
		new TeleportCommand().accept(player());
	}

	@Path("deny")
	void deny() {
		new TeleportCommand().deny(player());
	}
}
