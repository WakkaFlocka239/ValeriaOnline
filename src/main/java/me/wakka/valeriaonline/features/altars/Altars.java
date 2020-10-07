package me.wakka.valeriaonline.features.altars;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Altars implements Listener {
	@Getter
	public static List<Altar> altars = new ArrayList<>();

	public Altars() {
		ValeriaOnline.registerListener(this);

		Altar desert = new Altar("world", "altar_desert");
		Altar plains = new Altar("world", "altar_plains");
		Altar big = new Altar("world", "altar_big");
		Altar snow = new Altar("world", "altar_snow");

		altars = Arrays.asList(desert, plains, big, snow);
	}

	public static void set(Location location, boolean enabled){
		WorldGuardUtils WGUtils = new WorldGuardUtils(location.getWorld());
		Set<ProtectedRegion> regions = WGUtils.getRegionsAt(location);
		for (ProtectedRegion region : regions) {
			if(region.getId().contains("altar_")) {
				if(enabled)
					Altars.activate(region.getId());
				else
					Altars.deactivate(region.getId());

				break;
			}
		}
	}

	public static void activate(String regionID) {
		for (Altar altar : altars) {
			if (altar.getRegionID().equalsIgnoreCase(regionID)) {
				altar.setActive(true);
				break;
			}
		}
	}

	public static void deactivate(String regionID) {
		for (Altar altar : altars) {
			if (altar.getRegionID().equalsIgnoreCase(regionID)) {
				altar.setActive(false);
				break;
			}
		}
	}

	public static boolean isInAltar(Player player) {
		return fromLocation(player.getLocation()) != null;
	}

	public static Altar fromLocation(Location location) {
		WorldGuardUtils WGUtils = new WorldGuardUtils(location);
		for (Altar altar : altars) {
			if (WGUtils.isInRegion(location, altar.getRegionID()))
				return altar;
		}

		return null;
	}

	public static boolean isAltarRegion(String id) {
		for (Altar altar : altars) {
			if (id.equalsIgnoreCase(altar.getRegionID()))
				return true;
		}
		return false;
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
		if (isInAltar(player))
			event.setCancelled(true);

	}
}
