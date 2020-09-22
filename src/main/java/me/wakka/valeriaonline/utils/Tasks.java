package me.wakka.valeriaonline.utils;

import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.scheduler.BukkitScheduler;

public class Tasks {

	private static final BukkitScheduler scheduler = ValeriaOnline.getInstance().getServer().getScheduler();
	private static final ValeriaOnline instance = ValeriaOnline.getInstance();

	public static int wait(Time delay, Runnable runnable) {
		return wait(delay.get(), runnable);
	}

	public static int wait(long delay, Runnable runnable) {
		if (instance.isEnabled())
			return scheduler.runTaskLater(instance, runnable, delay).getTaskId();
		ValeriaOnline.log("Attempted to register wait task while disabled");
		return -1;
	}

	public static int repeat(Time startDelay, long interval, Runnable runnable) {
		return repeat(startDelay.get(), interval, runnable);
	}

	public static int repeat(long startDelay, Time interval, Runnable runnable) {
		return repeat(startDelay, interval.get(), runnable);
	}

	public static int repeat(Time startDelay, Time interval, Runnable runnable) {
		return repeat(startDelay.get(), interval.get(), runnable);
	}

	public static int repeat(long startDelay, long interval, Runnable runnable) {
		if (instance.isEnabled())
			return scheduler.scheduleSyncRepeatingTask(instance, runnable, startDelay, interval);
		ValeriaOnline.log("Attempted to register repeat task while disabled");
		return -1;
	}

	public static int sync(Runnable runnable) {
		if (instance.isEnabled())
			return scheduler.runTask(instance, runnable).getTaskId();
		ValeriaOnline.log("Attempted to register sync task while disabled");
		return -1;
	}

	public static int waitAsync(Time delay, Runnable runnable) {
		return waitAsync(delay.get(), runnable);
	}

	public static int waitAsync(long delay, Runnable runnable) {
		if (instance.isEnabled())
			return scheduler.runTaskLater(instance, () -> async(runnable), delay).getTaskId();
		ValeriaOnline.log("Attempted to register waitAsync task while disabled");
		return -1;
	}

	public static int repeatAsync(long startDelay, Time interval, Runnable runnable) {
		return repeatAsync(startDelay, interval.get(), runnable);
	}

	public static int repeatAsync(Time startDelay, long interval, Runnable runnable) {
		return repeatAsync(startDelay.get(), interval, runnable);
	}

	public static int repeatAsync(Time startDelay, Time interval, Runnable runnable) {
		return repeatAsync(startDelay.get(), interval.get(), runnable);
	}

	public static int repeatAsync(long startDelay, long interval, Runnable runnable) {
		if (instance.isEnabled())
			return scheduler.runTaskTimerAsynchronously(instance, runnable, startDelay, interval).getTaskId();
		ValeriaOnline.log("Attempted to register repeatAsync task while disabled");
		return -1;
	}

	public static int async(Runnable runnable) {
		if (instance.isEnabled())
			return scheduler.runTaskAsynchronously(instance, runnable).getTaskId();
		ValeriaOnline.log("Attempted to register async task while disabled");
		return -1;
	}

	public static void cancel(int taskId) {
		scheduler.cancelTask(taskId);
	}
}
