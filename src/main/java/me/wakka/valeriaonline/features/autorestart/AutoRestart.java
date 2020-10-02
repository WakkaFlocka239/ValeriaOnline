package me.wakka.valeriaonline.features.autorestart;

import com.google.common.base.Strings;
import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;

public class AutoRestart {
	private final List<Double> warnTimes = ConfigUtils.getSettings().getDoubleList("autorestart.warnTimes");     // minutes till restart warning times
	private final String PREFIX = ConfigUtils.getSettings().getString("autorestart.prefix");
	private final String warningMessage = ConfigUtils.getSettings().getString("autorestart.warnMsg");
	private final String restartMessage = ConfigUtils.getSettings().getString("autorestart.restartMsg");
	private final int restartInterval = ConfigUtils.getSettings().getInt("autorestart.interval");                // hours
	private final int restartTime = ConfigUtils.getSettings().getInt("autorestart.time");                        // time to start at, Ex: 6 == 0600
	private final double cancelTime = ConfigUtils.getSettings().getDouble("autorestart.cancelTime");             // minutes
	private static final String timezone = ConfigUtils.getSettings().getString("autorestart.timezone");
	//
	private static final List<Timer> warningTimers = new ArrayList<>();
	private static Timer rebootTimer = null;
	public static ZoneId zone = Strings.isNullOrEmpty(timezone) ? ZoneId.systemDefault() : ZoneId.of(timezone);
	//
	@Getter
	public static boolean restartSoon = false;
	public static LocalDateTime preventAt = null;
	public static List<LocalDateTime> warningsAt = new ArrayList<>();
	public static LocalDateTime restartAt = null;
	//
	private final static Location closeDungeons = new Location(Bukkit.getWorld("events"), 46, 11, -40);
	private final static Location openDungeons = new Location(Bukkit.getWorld("events"), 43, 13, -38);

	public AutoRestart() {
		scheduleRestart();

		closeDungeons.getBlock().setType(Material.AIR);
		openDungeons.getBlock().setType(Material.AIR);
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
		LocalDateTime temp;

		int cancelSeconds = Math.max((int) (((restartIn * 60.0) - cancelTime) * 60), 0);
		Tasks.wait(Time.SECOND.x(cancelSeconds), () -> {
			restartSoon = true;
			closeDungeons.getBlock().setType(Material.REDSTONE_BLOCK);
		});

		temp = LocalDateTime.now(zone);
		temp = temp.plusSeconds(cancelSeconds);
		ValeriaOnline.log("Prevent at: " + StringUtils.longDateTimeFormat(temp) + " (~" + (cancelSeconds / 60) + " minutes)");
		preventAt = temp;

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
			warningsAt.add(temp);
		}

		// Schedule Restart
		rebootTimer = new Timer();
		rebootTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Tasks.sync(() -> stopServer());
			}
		}, (long) ((restartIn) * (60.0 * 60.0 * 1000.0))); // milliseconds

		temp = LocalDateTime.now(zone);
		temp = temp.plusSeconds((long) ((restartIn * 60.0) * 60.0));
		ValeriaOnline.log("Restart at: " + StringUtils.longDateTimeFormat(temp) + " (~" + (int) (restartIn * 60.0) + " minutes)");
		restartAt = temp;

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
