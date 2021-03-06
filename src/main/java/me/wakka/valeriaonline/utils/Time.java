package me.wakka.valeriaonline.utils;

import me.wakka.valeriaonline.ValeriaOnline;

public enum Time {
	TICK(1),
	SECOND(TICK.get() * 20),
	MINUTE(SECOND.get() * 60),
	HOUR(MINUTE.get() * 60),
	DAY(HOUR.get() * 24),
	WEEK(DAY.get() * 7),
	MONTH(DAY.get() * 30),
	YEAR(DAY.get() * 365);

	private final int value;

	Time(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}

	public int x(int multiplier) {
		return value * multiplier;
	}

	public static class Timer {
		private static final int IGNORE = 2000;

		public Timer(String id, Runnable runnable) {
			long startTime = System.currentTimeMillis();

			runnable.run();

			long duration = System.currentTimeMillis() - startTime;
			if (duration > IGNORE)
				ValeriaOnline.log("[Timer] " + id + " took " + duration + "ms");
		}
	}
}
