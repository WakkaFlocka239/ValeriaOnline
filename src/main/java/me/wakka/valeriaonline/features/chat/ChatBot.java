//package me.wakka.valeriaonline.features.chat;
//
//import lombok.Builder;
//import lombok.Data;
//import lombok.Getter;
//import me.wakka.valeriaonline.ValeriaOnline;
//import me.wakka.valeriaonline.features.chat.events.ChatEvent;
//import me.wakka.valeriaonline.features.discord.Discord;
//import me.wakka.valeriaonline.features.discord.DiscordId.Channel;
//import me.wakka.valeriaonline.features.discord.DiscordId.Role;
//import me.wakka.valeriaonline.models.chat.ChatService;
//import me.wakka.valeriaonline.models.chat.Chatter;
//import me.wakka.valeriaonline.models.chat.PublicChannel;
//import me.wakka.valeriaonline.utils.ConfigUtils;
//import me.wakka.valeriaonline.utils.RandomUtils;
//import me.wakka.valeriaonline.utils.StringUtils;
//import me.wakka.valeriaonline.utils.Tasks;
//import me.wakka.valeriaonline.utils.Time;
//import me.wakka.valeriaonline.utils.Utils;
//import org.bukkit.Bukkit;
//import org.bukkit.OfflinePlayer;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.entity.Player;
//
//import java.math.RoundingMode;
//import java.text.DecimalFormat;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.google.common.base.Strings.isNullOrEmpty;
//import static me.wakka.valeriaonline.utils.StringUtils.stripColor;
//
//public class ChatBot {
//	@Getter
//	private static final String PREFIX = StringUtils.getPrefix("KodaBear");
//	@Getter
//	private static final String nameFormat = "&5&oChatBot";
//	@Getter
//	private static final String globalFormat = "&2[G] " + nameFormat + " &2&l> &f";
//	@Getter
//	private static final String localFormat = "&e[L] " + nameFormat + " &e&l> &f";
//	@Getter
//	private static final String dmFormat = "&3&l[&bPM&3&l] &eFrom &3KodaBear &b&l> &e";
//	@Getter
//	private static final String discordFormat = "<@&&f" + Role.CHATBOT.getId() + "> **>** ";
//	@Getter
//	private static final OfflinePlayer player = Bukkit.getOfflinePlayer("KodaBear");
//	@Getter
//	private static final Chatter chatter = new ChatService().get(player);
//
//	public static void reply(String message) {
//		Tasks.wait(10, () -> say(message));
//	}
//
//	public static void replyIngame(String message) {
//		Tasks.wait(10, () -> sayIngame(message));
//	}
//
//	public static void replyDiscord(String message) {
//		Tasks.wait(10, () -> sayDiscord(message));
//	}
//
//	public static void say(String message) {
//		sayIngame(message);
//		sayDiscord(message);
//	}
//
//	public static void sayIngame(String message) {
//		Chat.broadcastIngame(globalFormat + message);
//	}
//
//	public static void sayDiscord(String message) {
//		Chat.broadcastDiscord(discordFormat + message);
//	}
//
//	public static void announce(String message) {
//		Discord.koda(message, Channel.ANNOUNCEMENTS);
//	}
//
//	public static void console(String message) {
//		Bukkit.getConsoleSender().sendMessage("[KodaBear] " + stripColor(message));
//	}
//
//	public static void dm(Player player, String message) {
//		Utils.send(player, dmFormat + message);
//	}
//
//	@Getter
//	private static final List<Trigger> triggers = new ArrayList<>();
//
//	static {
//		reloadConfig();
//	}
//
//	public static void reloadConfig() {
//		triggers.clear();
//		ConfigurationSection config = ConfigUtils.getConfig("koda.yml").getConfigurationSection("triggers");
//		if (config != null) {
//			for (String key : config.getKeys(false)) {
//				ConfigurationSection section = config.getConfigurationSection(key);
//				if (!config.isConfigurationSection(key) || section == null)
//					ValeriaOnline.warn(PREFIX + "Configuration section " + key + " misconfigured");
//				else
//					triggers.add(Trigger.builder()
//							.name(key)
//							.contains(section.getStringList("contains"))
//							.responses(section.getStringList("responses"))
//							.routine(section.getString("routine"))
//							.build());
//			}
//		}
//	}
//
//	@Data
//	@Builder
//	private static class Trigger {
//		private final String name;
//		private final List<String> contains;
//		private final List<String> responses;
//		private final String routine;
//
//		String getResponse() {
//			return RandomUtils.randomElement(responses);
//		}
//	}
//
//	public static void process(ChatEvent event) {
//		responses: for (Trigger trigger : triggers) {
//			for (String contains : trigger.getContains())
//				if (!(" " + event.getMessage() + " ").matches("(?i).*(" + contains + ").*"))
//					continue responses;
//
//			if (!isNullOrEmpty(trigger.getRoutine()))
//				routine(event, trigger.getRoutine());
//
//			String response = trigger.getResponse();
//			if (!isNullOrEmpty(response))
//				respond(event, response);
//
//			break;
//		}
//	}
//
//	public static void respond(ChatEvent event, String response) {
//		Tasks.waitAsync(Time.SECOND, () -> {
//			final String finalResponse = response.replaceAll("\\[player]", event.getOrigin());
//			PublicChannel channel = (PublicChannel) event.getChannel();
//			event.getRecipients().forEach(recipient -> recipient.send(channel.getChatterFormat(chatter) + finalResponse));
//			channel.broadcastDiscord(discordFormat + finalResponse);
//		});
//	}
//
//	private static void routine(ChatEvent event, String id) {
//		switch (id) {
//			case "canibestaff":
//				if (event.getChatter() != null && event.getChatter().getOfflinePlayer().isOnline()) {
//					Player player = event.getChatter().getPlayer();
//					if (player.hasPermission("rank.guest")) {
//						String command = "staff";
//						if (event.getMessage().contains("mod")) command = "moderator";
//						if (event.getMessage().contains(" op")) command = "operator";
//						if (event.getMessage().contains("admin")) command = "admin";
//						if (event.getMessage().contains("builder")) command = "builder";
//
//						if ("staff".equals(command)) {
//							respond(event, "Sorry [player], but you don't meet the requirements for staff. Type /moderator for more info about what's required.");
//						} else {
//							respond(event, "Sorry [player], but you don't meet the requirements for " + command + ". Type /" + command + " for more info about what's required.");
//						}
//					}
//				}
//				break;
//			case "serverage":
//				ServerAge serverAge = new ServerAge();
//				String days = ServerAge.format(serverAge.getDays());
//				String years = ServerAge.format(serverAge.getYears());
//				respond(event, "The server is " + days + " days old! That's " + years + " years!");
//				break;
//		}
//	}
//
//	@Data
//	public static class ServerAge {
//		private final double dogYears, years, months, weeks, days, hours, minutes, seconds;
//
//		public ServerAge() {
//			LocalDateTime bn = LocalDateTime.now().withMonth(10).withDayOfMonth(16).withYear(2020).withHour(11);
//			Duration age = Duration.between(bn, LocalDateTime.now());
//			seconds = age.getSeconds();
//			minutes = seconds / 60.0;
//			hours = minutes / 60;
//			days = hours / 24;
//			weeks = days / 7;
//			months = days / 30.42;
//			years = days / 365;
//			dogYears = years * 7;
//		}
//
//		private static final DecimalFormat format = new DecimalFormat("###,###,##0.00");
//		static { format.setRoundingMode(RoundingMode.UP); }
//
//		public static String format(double value) {
//			return format.format(value);
//		}
//	}
//
//}
