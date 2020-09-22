package me.wakka.valeriaonline.utils;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimespanFormatter {
	private final int original;
	private final boolean noneDisplay;
	private final TimespanFormatType formatType;
	private int years, days, hours, minutes, seconds;

	@lombok.Builder(buildMethodName = "_build")
	public TimespanFormatter(int seconds, boolean noneDisplay, TimespanFormatType formatType) {
		this.original = seconds;
		this.seconds = seconds;
		this.noneDisplay = noneDisplay;
		this.formatType = formatType == null ? TimespanFormatType.SHORT : formatType;
		calculate();
	}

	public static TimespanFormatterBuilder of(long seconds) {
		return of(Long.valueOf(seconds).intValue());
	}

	public static TimespanFormatterBuilder of(int seconds) {
		return TimespanFormatter.builder().seconds(seconds);
	}

	public static class TimespanFormatterBuilder {
		public String format() {
			return _build().format();
		}
	}

	private void calculate() {
		if (seconds == 0) return;

		years = seconds / 60 / 60 / 24 / 365;
		seconds -= years * 60 * 60 * 24 * 365;
		days = seconds / 60 / 60 / 24;
		seconds -= days * 60 * 60 * 24;
		hours = seconds / 60 / 60;
		seconds -= hours * 60 * 60;
		minutes = seconds / 60;
		seconds -= minutes * 60;
	}

	public String format() {
		if (original == 0 && noneDisplay)
			return "None";

		String result = "";
		if (years > 0)
			result += years + formatType.get(formatType.getYear(), years);
		if (days > 0)
			result += days + formatType.get(formatType.getDay(), days);
		if (hours > 0)
			result += hours + formatType.get(formatType.getHour(), hours);
		if (minutes > 0)
			result += minutes + formatType.get(formatType.getMinute(), minutes);
		if (years == 0 && days == 0 && hours == 0 && minutes > 0 && seconds > 0)
			result += seconds + formatType.get(formatType.getSecond(), seconds);

		if (result.length() == 0)
			result = original + formatType.get(formatType.getSecond(), seconds);

		return result.trim();
	}

	public static String timespanDiff(LocalDateTime from) {
		LocalDateTime now = LocalDateTime.now();
		if (from.isBefore(now))
			return timespanDiff(from, now);
		else
			return timespanDiff(now, from);
	}

	public static String timespanDiff(LocalDateTime from, LocalDateTime to) {
		return TimespanFormatter.of(Long.valueOf(from.until(to, ChronoUnit.SECONDS)).intValue()).format();
	}

	public enum TimespanFormatType {
		SHORT("y", "d", "h", "m", "s") {
			@Override
			public String get(String label, int value) {
				return label + " ";
			}
		},
		LONG("year", "day", "hour", "minute", "second") {
			@Override
			public String get(String label, int value) {
				return " " + label + (value == 1 ? "" : "s") + " ";
			}
		};

		@Getter
		private final String year, day, hour, minute, second;

		TimespanFormatType(String year, String day, String hour, String minute, String second) {
			this.year = year;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}

		abstract String get(String label, int value);
	}
}
