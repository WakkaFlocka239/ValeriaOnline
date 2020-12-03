package me.wakka.valeriaonline.features.scoreboard;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.player.UserManager;
import lombok.Getter;
import lombok.SneakyThrows;
import me.ryanhamshire.GriefPrevention.Claim;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.models.chat.Channel;
import me.wakka.valeriaonline.models.chat.ChatService;
import me.wakka.valeriaonline.models.chat.Chatter;
import me.wakka.valeriaonline.models.chat.PrivateChannel;
import me.wakka.valeriaonline.models.chat.PublicChannel;
import me.wakka.valeriaonline.models.scoreboard.ScoreboardUser;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.wakka.valeriaonline.utils.StringUtils.camelCase;
import static me.wakka.valeriaonline.utils.StringUtils.left;
import static me.wakka.valeriaonline.utils.Utils.isVanished;

public enum ScoreboardLine {
	ONLINE {
		@Override
		public String render(Player player) {
			long count = Bukkit.getOnlinePlayers().stream().filter(_player -> Utils.canSee(player, _player)).count();
			return "&7Online: &b" + count;
		}
	},

	TPS {
		@Override
		public String render(Player player) {
			double tps1m = Bukkit.getTPS()[0];
			return "&7TPS: &" + (tps1m >= 19 ? "e" : tps1m >= 16 ? "6" : "c") + new DecimalFormat("0.00").format(tps1m);
		}
	},

	PING {
		@Override
		public String render(Player player) {
			return "&7Ping: &b" + player.spigot().getPing() + "ms";
		}
	},

	CHANNEL {
		@Override
		public String render(Player player) {
			String line = "&7Channel: &b";
			Chatter chatter = new ChatService().get(player);
			if (chatter == null)
				return line + "&bNone";
			Channel activeChannel = chatter.getActiveChannel();
			if (activeChannel == null)
				return line + "&bNone";
			if (activeChannel instanceof PrivateChannel)
				return line + "&b" + String.join(",", ((PrivateChannel) activeChannel).getOthersNames(chatter));
			if (activeChannel instanceof PublicChannel) {
				PublicChannel channel = (PublicChannel) activeChannel;
				return line + channel.getColor() + channel.getName();
			}
			return line + "Unknown";
		}
	},

	@Permission("pv.use")
	VANISHED {
		@Override
		public String render(Player player) {
			return "&7Vanished: &b" + isVanished(player);
		}
	},

//	PUSHING {
//		@Override
//		public String render(Player player) {
//			return "&7Pushing: &b" + player.hasPermission(PushCommand.getPerm());
//		}
//	},

	CLAIMS {
		@Override
		public String render(Player player) {
			String line = "&7Claims: &b";
			int remaining = 0;
			if (player.isOnline())
				remaining = ValeriaOnline.getGriefPrevention().dataStore.getPlayerData(player.getUniqueId()).getRemainingClaimBlocks();

			return line + remaining;

		}
	},

	CLAIM_OWNER {
		@Override
		public String render(Player player) {
			String line = "&7Claimed By: &b";
			Claim claim = null;

			if (player.isOnline())
				claim = ValeriaOnline.getGriefPrevention().dataStore.getClaimAt(player.getLocation(), true, null);

			String owner = "Unclaimed";
			if (claim != null)
				owner = String.valueOf(claim.getOwnerName());

			return line + owner;

		}
	},

	@Permission("essentials.gamemode")
	GAMEMODE {
		@Override
		public String render(Player player) {
			return "&7Mode: &b" + camelCase(player.getGameMode().name());
		}
	},

	@Permission("group.staff")
	WORLD {
		@Override
		public String render(Player player) {
			return "&7World: &b" + camelCase(player.getWorld().getName());
		}
	},

	BIOME {
		@Override
		public String render(Player player) {
			Location location = player.getLocation();
			return "&7Biome: &b" + camelCase(location.getWorld().getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ()).name());
		}
	},

	MCMMO {
		@Override
		public String render(Player player) {
			McMMOPlayer mcmmo = UserManager.getPlayer(player);
			return "&7McMMO Level: &b" + (mcmmo == null ? "0" : mcmmo.getPowerLevel());
		}
	},

	BALANCE {
		@Override
		public String render(Player player) {
			double balance = ValeriaOnline.getEcon().getBalance(player);

			String formatted = new DecimalFormat("###,###,###.00").format(balance);

			if (balance > 1000000)
				formatted = new DecimalFormat("###,###,###.###").format(balance / 1000000) + "m";
			else if (balance > 100000)
				formatted = new DecimalFormat("###,###").format(balance);

			if (formatted.endsWith(".00"))
				formatted = left(formatted, formatted.length() - 3);

			return "&7Crowns: &6" + formatted;
		}
	},

//	VOTE_POINTS {
//		@Override
//		public String render(Player player) {
//			return "&7Vote Points: &b" + ((Voter) new VoteService().get(player)).getPoints();
//		}
//	},

//	@Interval(2)
//	COMPASS {
//		@Override
//		public String render(Player player) {
//			return StringUtils.compass(player, 8);
//		}
//	},

	@Interval(3)
	COORDINATES {
		@Override
		public String render(Player player) {
			Location location = player.getLocation();
			return "&b" + (int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ();
		}
	},

//	@Interval(20)
//	HOURS {
//		@Override
//		public String render(Player player) {
//			Hours hours = new HoursService().get(player);
//			int seconds = hours == null ? 0 : hours.getTotal();
//			return "&7Hours: &b" + TimespanFormatter.of(seconds).noneDisplay(true).format();
//		}
//	},

	HELP {
		@Override
		public String render(Player player) {
			return "&c/sb help";
		}
	},

//	@Interval(20)
//	@Required
//	AFK {
//		@Override
//		public String render(Player player) {
//			AFKPlayer afkPlayer = me.pugabyte.bncore.features.afk.AFK.get(player);
//			if (afkPlayer.isAfk())
//				return "&7AFK for: &b" + timespanDiff(afkPlayer.getTime());
//			return null;
//		}
//	}
	;

	public abstract String render(Player player);

	@SneakyThrows
	public <T extends Annotation> T getAnnotation(Class<? extends Annotation> clazz) {
		return (T) getClass().getField(name()).getAnnotation(clazz);
	}

	public Permission getPermission() {
		return getAnnotation(Permission.class);
	}

	public boolean isOptional() {
		return getAnnotation(Required.class) == null;
	}

	public int getInterval() {
		Interval annotation = getAnnotation(Interval.class);
		return annotation == null ? ScoreboardUser.UPDATE_INTERVAL : annotation.value();
	}

	public boolean hasPermission(Player player) {
		Permission annotation = getPermission();
		return annotation == null || player.hasPermission(annotation.value());
	}

	// @formatter:off
	public static Map<ScoreboardLine, Boolean> getDefaultLines(Player player) {
		return new HashMap<ScoreboardLine, Boolean>() {{
			if (ScoreboardLine.ONLINE.hasPermission(player)) 			put(ScoreboardLine.ONLINE, true);
//			if (ScoreboardLine.TICKETS.hasPermission(player))			put(ScoreboardLine.TICKETS, true);
			if (ScoreboardLine.TPS.hasPermission(player)) 				put(ScoreboardLine.TPS, true);
			if (ScoreboardLine.PING.hasPermission(player)) 				put(ScoreboardLine.PING, true);
			if (ScoreboardLine.CHANNEL.hasPermission(player)) 			put(ScoreboardLine.CHANNEL, true);
			if (ScoreboardLine.VANISHED.hasPermission(player)) 			put(ScoreboardLine.VANISHED, true);
			if (ScoreboardLine.CLAIMS.hasPermission(player)) 			put(ScoreboardLine.CLAIMS, true);
			if (ScoreboardLine.CLAIM_OWNER.hasPermission(player)) 		put(ScoreboardLine.CLAIM_OWNER, true);
			if (ScoreboardLine.MCMMO.hasPermission(player)) 			put(ScoreboardLine.MCMMO, !player.hasPermission("group.staff"));
			if (ScoreboardLine.BALANCE.hasPermission(player)) 			put(ScoreboardLine.BALANCE, !player.hasPermission("group.staff"));
//			if (ScoreboardLine.VOTE_POINTS.hasPermission(player))		put(ScoreboardLine.VOTE_POINTS, !player.hasPermission("group.staff"));
			if (ScoreboardLine.GAMEMODE.hasPermission(player)) 			put(ScoreboardLine.GAMEMODE, true);
			if (ScoreboardLine.WORLD.hasPermission(player)) 			put(ScoreboardLine.WORLD, !player.hasPermission("group.staff"));
			if (ScoreboardLine.BIOME.hasPermission(player)) 			put(ScoreboardLine.BIOME, false);
//			if (ScoreboardLine.COMPASS.hasPermission(player))			put(ScoreboardLine.COMPASS, false);
			if (ScoreboardLine.COORDINATES.hasPermission(player)) 		put(ScoreboardLine.COORDINATES, true);
//			if (ScoreboardLine.HOURS.hasPermission(player))				put(ScoreboardLine.HOURS, true);
			if (ScoreboardLine.HELP.hasPermission(player)) 				put(ScoreboardLine.HELP, !player.hasPermission("group.staff"));
//			if (ScoreboardLine.AFK.hasPermission(player))				put(ScoreboardLine.AFK, true);
		}};
	}
	// @formatter:on

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface Permission {
		String value();
	}

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface Required {
	}

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface Interval {
		int value();
	}

	@Getter
	private static final List<String> headerFrames = Arrays.asList(
			" &dValeria Online ",
			" &dValeria Online ",
			" &dValeria Online ",
			" &5Valeria Online ",
			" &dValeria Online ",
			" &5Valeria Online ",
			" &dValeria Online ",
			" &dValeria Online ",
			" &dValeria Online ",
			" &5V&daleria Online ",
			" &dV&5a&dleria Online ",
			" &dVa&5l&deria Online ",
			" &dVal&5e&dria Online ",
			" &dVale&5r&dia Online ",
			" &dValer&5i&da Online ",
			" &dValeri&5a&d Online ",
			" &dValeria &5O&dnline ",
			" &dValeria O&5n&dline ",
			" &dValeria On&5l&dine ",
			" &dValeria Onl&5i&dne ",
			" &dValeria Onli&5n&de ",
			" &dValeria Onlin&5e&d ",
			" &dValeria Onli&5n&de ",
			" &dValeria Onl&5i&dne ",
			" &dValeria On&5l&dine ",
			" &dValeria O&5n&dline ",
			" &dValeria &5O&dnline ",
			" &dValeri&5a&d Online ",
			" &dValer&5i&da Online ",
			" &dVale&5r&dia Online ",
			" &dVal&5e&dria Online ",
			" &dVa&5l&deria Online ",
			" &dV&5a&dleria Online ",
			" &5V&daleria Online ",
			" &dV&5a&dleria Online ",
			" &dVa&5l&deria Online ",
			" &dVal&5e&dria Online ",
			" &dVale&5r&dia Online ",
			" &dValer&5i&da Online ",
			" &dValeri&5a&d Online ",
			" &dValeria &5O&dnline ",
			" &dValeria O&5n&dline ",
			" &dValeria On&5l&dine ",
			" &dValeria Onl&5i&dne ",
			" &dValeria Onli&5n&de ",
			" &dValeria Onlin&5e&d ",
			" &dValeria Onli&5n&de ",
			" &dValeria Onl&5i&dne ",
			" &dValeria On&5l&dine ",
			" &dValeria O&5n&dline ",
			" &dValeria &5O&dnline ",
			" &dValeri&5a&d Online ",
			" &dValer&5i&da Online ",
			" &dVale&5r&dia Online ",
			" &dVal&5e&dria Online ",
			" &dVa&5l&deria Online ",
			" &dV&5a&dleria Online ",
			" &5V&daleria Online "
	);
}
