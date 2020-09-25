package me.wakka.valeriaonline.features.commands;


import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TestCommand extends CustomCommand {

	public TestCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void run() {
		int hour = 6;
		int interval = 6;
		ZoneId zone = ZoneId.of("GMT+2");
		LocalDateTime now = LocalDateTime.now(zone);
		send("Now: " + StringUtils.longDateTimeFormat(now));

		LocalDateTime then = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, 0);
		for (int i = 0; i < 4; i++) {
			int addHours = i * interval;
			LocalDateTime localDateTime = then.plusHours(addHours);
			long seconds = ChronoUnit.SECONDS.between(now, localDateTime);
			double hours = (seconds / 60.0) / 60.0;
			if (hours >= 1) {
				then = localDateTime;
				break;
			}
		}

		send("Then: " + StringUtils.longDateTimeFormat(then));
		long seconds = ChronoUnit.SECONDS.between(now, then);
		double hours = (seconds / 60.0) / 60.0;
		if (hours < 1) {
			ValeriaOnline.warn("Could not schedule a restart!");
			return;
		}

		send("Hours between: " + hours);

	}


}
