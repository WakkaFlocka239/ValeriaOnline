package me.wakka.valeriaonline.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.NonNull;
import me.wakka.valeriaonline.ValeriaOnline;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardFlagUtils {

	public enum CustomFlags {
		TRAP_DOORS(registerFlag(new StateFlag("trap-doors", true))),
		SOIL_WET(registerFlag(new StateFlag("soil-wet", false))),
		GREETING_TITLE(registerFlag(new StringFlag("greeting-title"))),
		GREETING_SUBTITLE(registerFlag(new StringFlag("greeting-subtitle"))),
		FAREWELL_TITLE(registerFlag(new StringFlag("farewell-title"))),
		FAREWELL_SUBTITLE(registerFlag(new StringFlag("farewell-subtitle"))),
		DENY_HOSTILE_SPAWN(registerFlag(new StateFlag("deny-hostile-spawn", false)));

		public final Flag<?> flag;

		CustomFlags(Flag<?> flag) {
			this.flag = flag;
		}

		public Flag<?> get() {
			return flag;
		}

		public static void register() {
			// static init
		}
	}

	public static final SimpleFlagRegistry registry = (SimpleFlagRegistry) WorldGuard.getInstance().getFlagRegistry();

	public static Flag<?> registerFlag(Flag<?> flag) {
		if (WorldGuardUtils.plugin == null || registry == null) {
			ValeriaOnline.warn("Could not find WorldGuard, aborting registry of flag " + flag.getName());
			return null;
		}

		try {
			boolean fix = registry.isInitialized();

			try {
				if (fix) registry.setInitialized(false);
				registry.register(flag);
			} catch (FlagConflictException duplicate) {
				flag = registry.get(flag.getName());
			} finally {
				if (fix) registry.setInitialized(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return flag;
	}

	public static String getStringValueFor(Player player, @NonNull StringFlag flag) {
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		return container.createQuery().queryValue(loc, localPlayer, flag);
	}

	public static boolean isFlagSetFor(Player player, @NonNull StateFlag flag) {
		Validate.notNull(flag, "Flag cannot be null");

		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		return container.createQuery().testState(loc, localPlayer, flag);
	}

	public static boolean isFlagSetFor(Block block, @NonNull StateFlag flag) {
		return isFlagSetFor(block.getLocation(), flag);
	}

	public static boolean isFlagSetFor(Location location, @NonNull StateFlag flag) {
		Validate.notNull(flag, "Flag cannot be null");

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
		return StateFlag.test(container.createQuery().queryState(loc, (RegionAssociable) null, flag));
	}

}
