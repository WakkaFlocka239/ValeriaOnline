package me.wakka.valeriaonline.features.autorestart;

import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;

public class AutoRestart {
	// TODO: Config
	List<Double> warnTimes = Arrays.asList(10.0, 5.0, 1.0); // in minutes till restart
	String warningMessage = "Server will restart in... <minutes> minutes";
	String restartMessage = "Server is restarting!";
	String PREFIX = "[AutoRestart] ";
	int restartInterval = 6; // hours
	int restartTime = 6;
	double cancelTime = 30.0; // minutes
	ZoneId zone = ZoneId.of("GMT+2");
	//
	static List<Timer> warningTimers = new ArrayList<>();
	static Timer rebootTimer;
	@Getter
	public static boolean restartSoon = false;

	public AutoRestart() {
		scheduleRestart();
	}

	public static void shutdown() {
		cancelTasks();
	}

	private static void cancelTasks() {
		for (Timer warningTimer : warningTimers)
			warningTimer.cancel();

		warningTimers.clear();

		if(rebootTimer != null)
			rebootTimer.cancel();

		rebootTimer = new Timer();
	}

	private void scheduleRestart() {
		cancelTasks();
		String header = "========== Scheduling AutoRestart ==========";
		String footer = "============================================";

		ValeriaOnline.log(header);
		double restartIn = getNextRestart(); // hours
		if (restartIn == -1) {
			ValeriaOnline.warn("Something went wrong, restart not scheduled!");
			ValeriaOnline.log(footer);
			return;
		}

		ValeriaOnline.log("Timezone: " + zone.getId());

		int cancelSeconds = (int) (((restartIn * 60.0) - cancelTime) * 60);
		Tasks.wait(Time.SECOND.x(cancelSeconds), () -> restartSoon = true);

		LocalDateTime temp;

		temp = LocalDateTime.now(zone);
		temp = temp.plusSeconds(cancelSeconds);
		ValeriaOnline.log("Prevent at: " + StringUtils.longDateTimeFormat(temp) + " (~" + (cancelSeconds / 60) + " minutes)");

		// Schedule Warn Timers
		for (Double time : warnTimes) {
			double timeUntilWarn = ((restartIn * 60.0) - time); // minutes
			if (timeUntilWarn <= 0.0)
				continue;

			final double warnTime = time;
			Timer warnTimer = new Timer();
			warningTimers.add(warnTimer);
			warnTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					String warnMsg = warningMessage.replaceAll("<minutes>", "" + warnTime);
					Utils.broadcast(PREFIX + warnMsg);
					ValeriaOnline.log(warnMsg);
				}
			}, (long) (timeUntilWarn * (60.0 * 1000.0))); // milliseconds

			temp = LocalDateTime.now(zone);
			temp = temp.plusSeconds((long) (timeUntilWarn * 60.0));
			ValeriaOnline.log("Warning at: " + StringUtils.longDateTimeFormat(temp) + " (~" + (int) timeUntilWarn + " minutes)");
		}

		// Schedule Restart
		rebootTimer = new Timer();
		rebootTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Tasks.sync(() -> stopServer());
			}
		}, (long) ((restartIn) * (60.0 * 60.0 * 1000.0))); // milliseconds

		LocalDateTime now = LocalDateTime.now(zone);
		now = now.plusSeconds((long) ((restartIn * 60.0) * 60.0));
		ValeriaOnline.log("Restart at: " + StringUtils.longDateTimeFormat(now) + " (~" + (int) (restartIn * 60.0) + " minutes)");

		ValeriaOnline.log(footer);
	}

	private void stopServer() {
		ValeriaOnline.log("Restarting...");

		Utils.broadcast(PREFIX + restartMessage);
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			player.kickPlayer(colorize(restartMessage));
		}

		Utils.runCommandAsConsole("save-all");
		Utils.runCommandAsConsole("restart");
	}

	private double getNextRestart() {
		LocalDateTime now = LocalDateTime.now(zone);
		double latest = (warnTimes.get(0) / 60); // hours

		LocalDateTime then = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), restartTime, 0);
		for (int i = 0; i < 24; i++) {
			int addHours = i * restartInterval;
			LocalDateTime localDateTime = then.plusHours(addHours);
			long seconds = ChronoUnit.SECONDS.between(now, localDateTime);
			double hours = (seconds / 60.0) / 60.0;
			if (hours >= latest) {
				then = localDateTime;
				break;
			}
		}

		long seconds = ChronoUnit.SECONDS.between(now, then);
		double hours = (seconds / 60.0) / 60.0;
		if (hours < latest)
			return -1;

		return hours;
	}
}
