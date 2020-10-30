package me.wakka.valeriaonline.features.commands.staff;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

@Permission("group.staff")
public class StaffHelpCommand extends CustomCommand {
	String name = player().getName();

	public StaffHelpCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void help() {
		send("");
		send("&dCustom commands to help with moderation");
		send("&dHover over each command, to see more info");
		send("");
		send(json("")
				.next("&b/freeze <player> &7- Freezes the player and cancels any interaction from them")
				.hover("Ex: /freeze " + name));
		send(json("")
				.next("&b/unfreeze <player> &7- Unfreezes the player")
				.hover("Ex: /unfreeze " + name));
		send(json("")
				.next("&b/fc <player> <channel> &7- Forces the player into that channel")
				.hover("Alias: /forcechannel" + "\n"
						+ "\n"
						+ "Ex: /fc " + name + " l"));
		send(json("")
				.next("&b/rh <player> &7- /freeze <player>, /gm 0, /fc <player> l, /vanish off, /fly off, and /ch l")
				.hover("Alias: /redhand" + "\n"
						+ "\n"
						+ "Ex: /rh " + name));
		send(json("")
				.next("&b/ymc <player> [warning] &7- /unfreeze <player>, /fc <player> g, /vanish on, and /warn")
				.hover("Alias: /youmaycontinue"
						+ "\n"
						+ "Ex: /ymc " + name + " fly hacking" + "\n"
						+ "Ex: /ymc " + name));
		send("");
		if (player().hasPermission("group.admin")) {
			send("&eAdmin Commands: ");
			send("&b/op <player> &7- Player must be staff");
			send("&b/op list &7- List of ops");
			send("&b/deop <player>");
			send("&b/sudo <player> <command> &7- Forces the player to run the command as op");
			send("&b/console <command> &7- Runs command as console");
			send("");
		}
	}
}
