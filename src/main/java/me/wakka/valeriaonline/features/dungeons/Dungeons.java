package me.wakka.valeriaonline.features.dungeons;

import com.destroystokyo.paper.Title;
import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.CitizensUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Dungeons implements Listener {
	@Getter
	public static List<Dungeon> dungeons = new ArrayList<>();
	public static final World world = Bukkit.getWorld("events");
	public static final Location lobby = new Location(world, 128.5, 5, -33.5, 180, 0);
	public static final String PREFIX = StringUtils.getPrefix("Dungeon");
	public static List<Player> allowedTeleport = new ArrayList<>();
	public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	public static List<String> disabledCommands = Arrays.asList("/sethome", "/tpa", "/tpask", "/tpahere",
			"/teleporthere", "/teleport", "/back", "/spawn", "/back");

	public static final String ERROR = "&cYou can't do that here.";

	public Dungeons() {
		ValeriaOnline.registerListener(this);

		Dungeon crypt = new Dungeon("dungeon_crypt", "Green");
		dungeons = Collections.singletonList(crypt);
	}

	public static boolean isInDungeonOrOnTeam(Player player) {
		boolean inDungeon = isInDungeon(player);
		if (!inDungeon) {
			Team team = getDungeonTeam(player);
			return team != null;
		}

		return inDungeon;
	}

	public static boolean isInDungeon(Player player) {
		return isInDungeon(player.getLocation());
	}

	public static boolean isInDungeon(Location location) {
		Dungeon dungeon = fromLocation(location);
		return dungeon != null;
	}

	public static Dungeon fromLocation(Location location) {
		WorldGuardUtils WGUtils = new WorldGuardUtils(location);
		for (Dungeon dungeon : dungeons) {
			if (WGUtils.isInRegion(location, dungeon.getRegionID()))
				return dungeon;
		}

		return null;
	}

	public static boolean isDungeonRegion(String id) {
		for (Dungeon dungeon : dungeons) {
			if (id.equalsIgnoreCase(dungeon.getRegionID()))
				return true;
		}
		return false;
	}

	private boolean isInDungeonExit(Player player) {
		Dungeon dungeon = fromLocation(player.getLocation());
		if (dungeon == null)
			return false;

		String exitRegion = dungeon.getRegionID() + "_exit";
		WorldGuardUtils WGUtils = new WorldGuardUtils(player);

		return WGUtils.isInRegion(player.getLocation(), WGUtils.getProtectedRegion(exitRegion));
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (CitizensUtils.isNPC(player))
			return;

		if (!isInDungeonOrOnTeam(player)) {
			return;
		}

		if (allowedTeleport.contains(player)) {
			return;
		}

		if (isInDungeonExit(player)) {
			return;
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void onItemTeleport(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		ItemStack item = Utils.getTool(event.getPlayer());
		if (Utils.isNullOrAir(item))
			return;

		if (!item.getType().equals(Material.ENDER_PEARL) && !item.getType().equals(Material.CHORUS_FRUIT))
			return;

		Player player = event.getPlayer();
		if (isInDungeon(player))
			event.setCancelled(true);
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();

		Tasks.wait(Time.SECOND.x(1), () -> {
			if (isInDungeon(player)) {
				lobby(player);
			}
		});
	}

	@EventHandler
	public void onDeath(EntityDamageEvent event) {
		if (!event.getEntityType().equals(EntityType.PLAYER))
			return;

		Player player = (Player) event.getEntity();
		double health = player.getHealth() - event.getFinalDamage();
		if (health <= 0 && isInDungeon(player)) {
			event.setCancelled(true);

			lobby(player);

			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Time.SECOND.x(5), 1, false, false, false));
			player.sendTitle(new Title("You died.", null, 10, 40, 10));

			player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Time.SECOND.x(5), 1, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Time.SECOND.x(45), 2, false, false, false));
		}
	}

	@EventHandler
	public void onElytra(EntityToggleGlideEvent event) {
		if (!event.getEntity().getType().equals(EntityType.PLAYER))
			return;

		Player player = (Player) event.getEntity();
		if (isInDungeon(player))
			event.setCancelled(true);
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String[] args = event.getMessage().toLowerCase().split(" ");
		String command = args[0];

		// Only return if you're NOT in a dungeon, or in a dungeon team
		if (!isInDungeonOrOnTeam(player)) {
			return;
		}

		if (disabledCommands.contains(command)) {
			event.setCancelled(true);
			error(player);
		}
	}

	public static void lobby(Player player) {
		teleport(player, lobby);
	}

	public static void teleport(Player player, Location loc) {
		allowedTeleport.add(player);
		player.teleport(loc);
		allowedTeleport.remove(player);
	}

	public static Team getDungeonTeam(Player player) {
		Team playerTeam = null;
		for (Team team : scoreboard.getTeams()) {
			if (team.hasEntry(player.getName()))
				playerTeam = team;
		}

		if (playerTeam == null)
			return null;

		Team dungeonTeam = null;
		for (Dungeon dungeon : dungeons) {
			if (dungeon.getTeam().equalsIgnoreCase(playerTeam.getName()))
				dungeonTeam = playerTeam;
		}

		return dungeonTeam;
	}

	public static void removeFromDungeonTeam(Player player) {
		Team dungeonTeam = Dungeons.getDungeonTeam(player);
		if (dungeonTeam == null)
			return;

		dungeonTeam.removeEntry(player.getName());
	}

	public static void error(Player player) {
		Utils.send(player, PREFIX + ERROR);
	}
}
