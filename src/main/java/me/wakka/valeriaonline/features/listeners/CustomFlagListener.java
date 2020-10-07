package me.wakka.valeriaonline.features.listeners;

import com.sk89q.worldguard.protection.flags.StateFlag;
import me.wakka.valeriaonline.models.setting.SettingService;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.SOIL_WET;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.TRAP_DOORS;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.isFlagSetFor;

public class CustomFlagListener implements Listener {

	@EventHandler
	public void onInteract_trapDoor(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (Utils.isNullOrAir(block) || !(Tag.TRAPDOORS.isTagged(block.getType())))
			return;

		Player player = event.getPlayer();
		WorldGuardUtils WGUtils = new WorldGuardUtils(player);

		if (WGUtils.getRegionsAt(player.getLocation()).size() == 0)
			return;

		if (!isFlagSetFor(player, (StateFlag) TRAP_DOORS.get())) {
			if (player.hasPermission("group.staff")) {
				if (new SettingService().get(player, "worldGuardEdit").getBoolean())
					return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onSoilMoistureChange(MoistureChangeEvent event) {
		Block block = event.getBlock();
		if (!block.getType().equals(Material.FARMLAND))
			return;

		Location loc = block.getLocation();
		WorldGuardUtils WGUtils = new WorldGuardUtils(loc);


		if (WGUtils.getRegionsAt(loc).size() == 0)
			return;

		if (isFlagSetFor(block, (StateFlag) SOIL_WET.get())) {
			event.setCancelled(true);
		}
	}
}
