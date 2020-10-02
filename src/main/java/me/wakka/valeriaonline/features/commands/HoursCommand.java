package me.wakka.valeriaonline.features.commands;


import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Async;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.hours.Hours;
import me.wakka.valeriaonline.models.hours.HoursService;
import me.wakka.valeriaonline.utils.TimespanFormatter;
import org.bukkit.OfflinePlayer;

import java.util.List;

@Aliases({"playtime", "days", "minutes"})
public class HoursCommand extends CustomCommand {
	private final HoursService service = new HoursService();

	public HoursCommand(CommandEvent event) {
		super(event);
	}

	@Path("[player]")
	void player(@Arg("self") OfflinePlayer player) {
		boolean isSelf = isSelf(player);

		Hours hours = service.get(player);
		send("");
		send(PREFIX + (isSelf ? "Your" : "&d" + player.getName() + "&7's") + " playtime");
		send("&7Total: &d" + TimespanFormatter.of(hours.getTotal()).format());
		send("&8- &7Today: &d" + TimespanFormatter.of(hours.getDaily()).format());
		send("&8- &7This week: &d" + TimespanFormatter.of(hours.getWeekly()).format());
		send("&8- &7This month: &d" + TimespanFormatter.of(hours.getMonthly()).format());
	}

	@Path("cleanup")
	@Permission("group.dev")
	void cleanup() {
		send("Cleaned up " + service.cleanup() + " records");
	}

	@Path("set <player> <type> <seconds>")
	@Permission("group.admin")
	void set(OfflinePlayer player, HoursService.HoursType type, int seconds) {
		Hours hours = service.get(player);
		hours.set(type, seconds);
		service.save(hours);
		send(PREFIX + "Set " + player.getName() + "'s " + camelCase(type.name()) + " to " + hours.get(type));
	}

	@Path("endOfDay")
	@Permission("group.dev")
	void endOfDay() {
		console();
		service.endOfDay();
	}

	@Path("endOfWeek")
	@Permission("group.dev")
	void endOfWeek() {
		console();
		service.endOfWeek();
	}

	@Path("endOfMonth")
	@Permission("group.dev")
	void endOfMonth() {
		console();
		service.endOfMonth();
	}

	@Async
	@Path("top")
	void top() {
		String type = isIntArg(2) ? "total" : arg(2);
		int page = isIntArg(2) ? intArg(2) : isIntArg(3) ? intArg(3) : 1;

		final HoursService.HoursType hoursType = service.getType(type);

		List<Hours> results = service.getPage(hoursType, page);
		if (results.size() == 0)
			error(PREFIX + "&cNo results on page " + page);

		send("");
		send(PREFIX + "Total: " + TimespanFormatter.of(service.total(hoursType)).format() + (page > 1 ? "&d  |  &7Page " + page : ""));
		int i = (page - 1) * 10 + 1;
		for (Hours hours : results)
			send("&7" + i++ + " &d" + hours.getPlayer().getName() + " &8- " + TimespanFormatter.of(hours.get(hoursType)).format());
	}
}
