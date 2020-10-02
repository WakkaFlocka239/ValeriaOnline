package me.wakka.valeriaonline.features.autorestart;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Permission("group.staff")
public class AutoRestartCommand extends CustomCommand {
	public AutoRestartCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void info() {
		String preventAt = getTimespanDiff(AutoRestart.preventAt);

		List<String> warningsAt = new ArrayList<>();
		for (LocalDateTime localDateTime : AutoRestart.warningsAt) {
			warningsAt.add(getTimespanDiff(localDateTime));
		}

		String restartAt = getTimespanDiff(AutoRestart.restartAt);
		//
		send(PREFIX + "Scheduled times: ");
		send("&8- &7Prevent: " + preventAt);
		for (String warning : warningsAt) {
			send("&8- &7Warning: " + warning);
		}
		send("&8- &7Restart: " + restartAt);
	}

	private String getTimespanDiff(LocalDateTime localDateTime) {
		String timespanDiff = StringUtils.timespanDiff(LocalDateTime.now(AutoRestart.zone), localDateTime);
		if (timespanDiff.startsWith("-"))
			timespanDiff = timespanDiff.replace("-", "") + " ago";

		return timespanDiff;
	}

}
