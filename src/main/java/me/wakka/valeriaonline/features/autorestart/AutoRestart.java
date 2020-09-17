package me.wakka.valeriaonline.features.autorestart;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.wakka.valeriaonline.Utils.Tasks;
import me.wakka.valeriaonline.Utils.Time;
import me.wakka.valeriaonline.Utils.Utils;
import me.wakka.valeriaonline.Utils.WorldGuardUtils;
import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static me.wakka.valeriaonline.Utils.StringUtils.colorize;

// TODO: instead of just rescheduling the restart, add some preventative measures
//  	so people cannot do certain things if the server is about to restart
public class AutoRestart {
	List<Double> warnTimes = Arrays.asList(10.0, 5.0, 1.0); // in minutes till restart
	String warningMessage = "Server will restart in... <minutes> minutes";
	String restartMessage = "Server is restart!";
	String PREFIX = "[AutoRestart] ";
	double restartInterval = 1; // in hours
	//
	static List<Timer> warningTimers = new ArrayList<>();
	static Timer rebootTimer;
	boolean delayRestart = false;
//	long startTimestamp;

	public AutoRestart(){
		// TODO: Load config settings
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

	private void scheduleRestart(){
		cancelTasks();

		for (Double time : warnTimes) {
			if ((restartInterval * 60.0) - time <= 0.0)
				continue;

			final double warnTime = time;
			Timer warnTimer = new Timer();
			warningTimers.add(warnTimer);
			warnTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(isNotDelayed()) {
						String warnMsg = warningMessage.replaceAll("<minutes>", "" + warnTime);
						Utils.broadcast(PREFIX + warnMsg);
						ValeriaOnline.log(warnMsg);
					}
				}
			}, (long) ((restartInterval * 60.0 - time) * (60.0 * 1000.0))); // milliseconds

			double minutes = (restartInterval * 60.0) - time;
			ValeriaOnline.log("AutoRestart warning scheduled for " + minutes + " minutes from now!");
		}

		rebootTimer = new Timer();
		rebootTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(isNotDelayed()) {
					Tasks.sync(() -> stopServer());
				}

			}
		}, (long)(restartInterval * (60.0 * 60.0 * 1000.0))); // milliseconds

		double minutes = restartInterval  * 60.0;
		ValeriaOnline.log("AutoRestart scheduled for " + minutes + " minutes from now!");
//		startTimestamp = System.currentTimeMillis();
	}

	private boolean isNotDelayed(){
		if(delayRestart)
			return false;

		// some checks
		for (Player player : Bukkit.getOnlinePlayers()) {
			WorldGuardUtils WGUtils = new WorldGuardUtils(player);
			for (ProtectedRegion region : WGUtils.getRegionsAt(player.getLocation())) {
				if(region.getId().contains("altar_")) {
					delayRestart = true;
				}
			}
		}

		if(delayRestart){
			cancelTasks();
			ValeriaOnline.log("AutoRestart has been cancelled, try again in 5 minutes");
			Tasks.wait(Time.MINUTE.x(5), () -> {
				delayRestart = false;
				if(isNotDelayed()) {
					scheduleRestart();
				}
			});
		}

		return !delayRestart;

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
}
