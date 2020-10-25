package me.wakka.valeriaonline.features.commands.staff;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.cooldown.Cooldowns;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.hours.Hours;
import me.wakka.valeriaonline.models.hours.HoursService;
import me.wakka.valeriaonline.utils.RandomUtils;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Aliases("welc")
@NoArgsConstructor
@Permission("group.staff")
public class WelcomeCommand extends CustomCommand {
	private static String lastMessage = null;

	List<String> messages = new ArrayList<String>() {{
		add("Welcome to the server [player]! Make sure to read the &e&l/rules&r and feel free to ask questions.");
		add("Welcome to &bValeria Online&r [player]! Please take a moment to read the &e&l/rules and feel free to ask any questions you have.");
		add("Hi [player], welcome to &bValeria Online&r :) Please read the &e&l/rules&r and ask if you have any questions.");
		add("Hey [player]! Welcome to &bValeria Online&r. Be sure to read the &e&l/rules&r and don't be afraid to ask questions ^^");
		add("Hi there [player] :) Welcome to &bValeria Online&r. Make sure to read the&e&l /rules&r and feel free to ask questions.");
	}};

	public WelcomeCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("[player]")
	void welcome(Player player) {
		if (player != null) {
			if (!player.hasPermission("group.newcomer"))
				error("Prevented accidental welcome");
			if (((Hours) new HoursService().get(player)).getTotal() > (60 * 60))
				error("Prevented accidental welcome");
		}

		if (Cooldowns.check(ValeriaOnline.getUUID0(), "welc", Time.SECOND.x(20))) {
			String message = getMessage();
			if (player == null)
				message = message.replaceAll(" \\[player]", "");
			else
				message = message.replaceAll("\\[player]", player.getName());

			runCommand("ch qm g " + message);
		} else {
			if (player == null)
				runCommand("ch qm g Welcome to the server!");
			else
				runCommand("ch qm g Welcome to the server, " + player.getName() + "!");
		}
	}

	private String getMessage() {
		ArrayList<String> list = new ArrayList<>(messages);
		if (lastMessage != null)
			list.remove(lastMessage);
		String message = RandomUtils.randomElement(list);
		lastMessage = message;
		return message;
	}

}
