package me.wakka.valeriaonline.features.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.wakka.valeriaonline.Utils.ConfigUtils;
import me.wakka.valeriaonline.Utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.Set;

// Multiple worlds can have the same region names, not an issue now, but somehow support worlds in settings
public class EndPortalRestrictions implements Listener {
	public static final List<String> allowedRegions = ConfigUtils.getSettings().getStringList("endPortalRegions");

	@EventHandler
	public void onEntityEndPortalTP(EntityPortalEvent event) {
		if(event.getEntityType().equals(EntityType.PLAYER))
			return;

		Location loc = event.getTo();
		if(loc == null)
			return;

		World world = loc.getWorld();
		World end = Bukkit.getWorld("world_the_end");
		if(!world.equals(end))
			return;

		if(isRestricted(event.getFrom()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerEndPortalTP(PlayerPortalEvent event){
		PlayerTeleportEvent.TeleportCause cause = event.getCause();
		if(!cause.equals(PlayerTeleportEvent.TeleportCause.END_PORTAL))
			return;

		Player player = event.getPlayer();
		if(player.hasPermission("group.creator"))
			return;

		if(allowedRegions.size() == 0)
			return;

		if(isRestricted(event.getFrom()))
			event.setCancelled(true);
	}

	private boolean isRestricted(Location location){
		Set<ProtectedRegion> regions = new WorldGuardUtils(location).getRegionsAt(location);

		if(regions == null || regions.size() == 0)
			return true;

		for (ProtectedRegion region : regions) {
			if(allowedRegions.contains(region.getId())) {
				return false;
			}
		}

		return true;
	}
}
