package me.wakka.valeriaonline.features.altars;

import com.mewin.worldguardregionapi.events.RegionLeavingEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.wakka.valeriaonline.Utils.Utils;
import me.wakka.valeriaonline.Utils.WorldGuardUtils;
import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Altars implements Listener {
	private static List<Altar> altars = new ArrayList<>();
	WorldGuardUtils WGUtils;

	public Altars() {
		ValeriaOnline.registerListener(this);

		loadAltars();
	}

	private void loadAltars() {
		Altar test = new Altar(Bukkit.getWorld("world"), "altar_test");

		altars = Collections.singletonList(test);
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

	@EventHandler
	public void onRegionLeave(RegionLeavingEvent event) {
		if (event.getRegion().getId().contains("altar_")) {
			ItemStack key = Utils.getTool(event.getPlayer());
			if (Utils.isNullOrAir(key) || !key.getType().equals(Material.ORANGE_WOOL))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onThrowEnderPearl(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		ItemStack item = Utils.getTool(event.getPlayer());
		if (Utils.isNullOrAir(item))
			return;

		if (!item.getType().equals(Material.ENDER_PEARL) && !item.getType().equals(Material.CHORUS_FRUIT))
			return;

		Player player = event.getPlayer();
		WGUtils = new WorldGuardUtils(player);
		Set<ProtectedRegion> regions = WGUtils.getRegionsAt(player.getLocation());
		if (regions == null || regions.size() == 0)
			return;

		for (ProtectedRegion region : regions) {
			if (region.getId().contains("altar_")) {
				event.setCancelled(true);
				return;
			}
		}

	}
}
