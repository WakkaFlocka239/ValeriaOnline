package me.wakka.valeriaonline.features.dungeons;

import me.wakka.valeriaonline.features.menus.MenuUtils.ConfirmationMenu;
import me.wakka.valeriaonline.framework.commands.CommandBlockArgs;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleteIgnore;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aliases("dungeons")
public class DungeonCommand extends CustomCommand {
	WorldGuardUtils WGUtils = new WorldGuardUtils(Dungeons.world);
	private static final int warpCost = 300;

	public DungeonCommand(CommandEvent event) {
		super(event);
	}

	@Path("warp")
	void warp() {
		if (!player().hasPermission("group.staff")) {
			ConfirmationMenu.builder()
					.title("&fTeleport to dungeons")
					.confirmLore("&cCosts: &6" + warpCost + " Crowns")
					.onConfirm(e -> {
						if (Utils.withdraw(player(), warpCost))
							player().teleport(Dungeons.lobby);
						else
							error("You don't have enough Crowns.");
					})
					.open(player());
		} else
			player().teleport(Dungeons.lobby);
	}

	@Path("leave")
	void leave() {
		if (!Dungeons.isInDungeonOrOnTeam(player())) {
			error("You're not in a dungeon!");
		}

		Dungeons.removeFromDungeonTeam(player());
		Dungeons.lobby(player());
	}

	@Permission("group.creator")
	@Path("clear <region>")
	void clearRegion(String region) {
		Collection<Entity> entities = WGUtils.getEntitiesInRegion(Dungeons.world, region);
		int count = 0;
		for (Entity entity : entities) {
			EntityType type = entity.getType();

			if (type.equals(EntityType.ARMOR_STAND) || type.equals(EntityType.ITEM_FRAME))
				continue;

			if (type.equals(EntityType.PLAYER)) {
				entity.teleport(Dungeons.lobby);
				continue;
			}

			entity.remove();
			++count;
		}

		send("Removed " + count + " entities in region " + region);
	}

	@Permission("group.creator")
	@Path("tp <player> <x> <y> <z> [yaw] [pitch] [world]")
	void teleportPlayer(Player player, double x, double y, double z, Float yaw, Float pitch, String world) {

		if (yaw == null)
			yaw = player.getLocation().getYaw();

		if (pitch == null)
			pitch = player.getLocation().getPitch();

		if (world == null)
			world = player.getWorld().getName();

		Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		Dungeons.teleport(player, loc);
	}

	@Permission("group.creator")
	@Path("tp team <team> <x> <y> <z> [yaw] [pitch] [world]")
	void teleportTeam(String teamArg, double x, double y, double z, Float yaw, Float pitch, @Arg("events") String world) {
		if (teamArg == null)
			throw new InvalidInputException("Team cannot be null!");

		Team team = Dungeons.scoreboard.getTeam(teamArg);
		if (team == null)
			throw new InvalidInputException("Team " + teamArg + " not found!");

		List<Player> teamPlayers = new ArrayList<>();
		for (String entry : team.getEntries()) {
			Player player = Bukkit.getPlayer(entry);
			if (player != null && player.isOnline())
				teamPlayers.add(player);
		}

		for (Player teamPlayer : teamPlayers) {
			if (yaw == null)
				yaw = teamPlayer.getLocation().getYaw();

			if (pitch == null)
				pitch = teamPlayer.getLocation().getPitch();

			if (world == null)
				world = teamPlayer.getWorld().getName();

			Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			Dungeons.teleport(teamPlayer, loc);
		}
	}

	@Permission("group.dev")
	@TabCompleteIgnore
	@Path("tp cmdblock <args...>")
	void teleportCommandBlock(String commandBlockArgs) {
		CommandBlockArgs args = convertToCommandBlockArgs(commandBlockArgs);
		int count = 0;
		Location dest = args.getDestination();
		for (Object target : args.getTargets()) {
			Entity entity = (Entity) target;
			if (!(entity instanceof Player)) {
				continue;
			}

			Player player = (Player) entity;

			if (args.getDestinationYaw() == null)
				dest.setYaw(player.getLocation().getYaw());
			if (args.getDestinationPitch() == null)
				dest.setPitch(player.getLocation().getPitch());

			Dungeons.teleport(player, dest);
			++count;
		}

		send("Teleported " + count + " players to " + StringUtils.getShortLocationString(dest));
	}
}
