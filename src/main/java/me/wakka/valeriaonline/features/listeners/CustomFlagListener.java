package me.wakka.valeriaonline.features.listeners;

import com.mewin.worldguardregionapi.events.RegionEnteredEvent;
import com.mewin.worldguardregionapi.events.RegionLeavingEvent;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import me.wakka.valeriaonline.models.setting.SettingService;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.DENY_HOSTILE_SPAWN;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.FAREWELL_SUBTITLE;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.FAREWELL_TITLE;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.GREETING_SUBTITLE;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.GREETING_TITLE;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.SOIL_WET;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.CustomFlags.TRAP_DOORS;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.getStringValueFor;
import static me.wakka.valeriaonline.utils.WorldGuardFlagUtils.isFlagSetFor;

public class CustomFlagListener implements Listener {

	@EventHandler
	public void sendGreetingTitle(RegionEnteredEvent event) {
		Player player = event.getPlayer();

		String greeting_title = getStringValueFor(player, (StringFlag) GREETING_TITLE.get());
		String greeting_subtitle = getStringValueFor(player, (StringFlag) GREETING_SUBTITLE.get());

		if (greeting_title == null && greeting_subtitle == null)
			return;

		if (greeting_title == null)
			greeting_title = "";

		if (greeting_subtitle == null)
			greeting_subtitle = "";

		player.sendTitle(colorize(greeting_title), colorize(greeting_subtitle), 10, 20, 10);
	}

	@EventHandler
	public void sendFarewellTitle(RegionLeavingEvent event) {
		Player player = event.getPlayer();

		String farewell_title = getStringValueFor(player, (StringFlag) FAREWELL_TITLE.get());
		String farewell_subtitle = getStringValueFor(player, (StringFlag) FAREWELL_SUBTITLE.get());

		Utils.wakka();

		if (farewell_title == null && farewell_subtitle == null)
			return;

		if (farewell_title == null)
			farewell_title = "";

		if (farewell_subtitle == null)
			farewell_subtitle = "";

		player.sendTitle(colorize(farewell_title), colorize(farewell_subtitle), 10, 20, 10);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Location eventLoc = event.getLocation();

		WorldGuardUtils WGUtils = new WorldGuardUtils(eventLoc);

		if (WGUtils.getRegionsAt(eventLoc).size() == 0)
			return;

		if (isFlagSetFor(event.getLocation(), (StateFlag) DENY_HOSTILE_SPAWN.get())) {
			if (event.getEntity() instanceof Monster) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInteract_trapDoor(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (Utils.isNullOrAir(block) || !(Tag.TRAPDOORS.isTagged(block.getType())))
			return;

		WorldGuardUtils WGUtils = new WorldGuardUtils(block);

		if (WGUtils.getRegionsAt(block.getLocation()).size() == 0)
			return;

		if (!isFlagSetFor(block, (StateFlag) TRAP_DOORS.get())) {
			Player player = event.getPlayer();
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

		Farmland farmland = (Farmland) block.getBlockData();

		Location loc = block.getLocation();
		WorldGuardUtils WGUtils = new WorldGuardUtils(loc);


		if (WGUtils.getRegionsAt(loc).size() == 0)
			return;

		if (isFlagSetFor(block, (StateFlag) SOIL_WET.get())) {
			if (farmland.getMoisture() < 7) {
				farmland.setMoisture(7);
				block.setBlockData(farmland);
			}

			event.setCancelled(true);
		}
	}
}
