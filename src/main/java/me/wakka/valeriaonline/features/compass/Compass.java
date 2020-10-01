package me.wakka.valeriaonline.features.compass;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.ActionBarUtils;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.SoundUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.LinkedHashMap;
import java.util.List;

public class Compass implements Listener {
	private static final LinkedHashMap<String, Location> locationMap = new LinkedHashMap<>();
	private static final ItemBuilder item = new ItemBuilder(Material.COMPASS).name("Kingdom Compass").lore("&dTarget: &7Elven Kingdom", "&f", "&7[Left-Click: Switch Target]");

	public Compass() {
		ValeriaOnline.registerListener(this);

		List<String> strings = ConfigUtils.getSettings().getStringList("compassLocs");
		for (String string : strings) {
			String[] stringSplit = string.split(", ");

			String name = StringUtils.camelCase(stringSplit[0]);
			int x = Integer.parseInt(stringSplit[1]);
			int z = Integer.parseInt(stringSplit[2]);
			World world = Bukkit.getWorld("world");

			locationMap.put(name, new Location(world, x, 0, z));
		}
	}

	public static ItemStack getItem() {
		ItemStack compass = item.build();
		CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
		compassMeta.setLodestone(locationMap.get("Elven"));
		compassMeta.setLodestoneTracked(false);
		compass.setItemMeta(compassMeta);
		return compass;
	}

	@EventHandler
	public void onClickCompass(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.LEFT_CLICK_AIR))
			return;

		ItemStack compass = Utils.getTool(event.getPlayer());
		if (Utils.isNullOrAir(compass) || !compass.getType().equals(Material.COMPASS)) return;

		List<String> lore = compass.getLore();
		if (lore == null || lore.size() == 0) return;

		boolean hasTarget = false;
		for (String line : lore) {
			if (StringUtils.stripColor(line).contains("Target:"))
				hasTarget = true;
		}

		if (!hasTarget)
			return;

		int loreNdx = 0;
		for (String line : lore) {
			if (line.contains("Target:")) {
				String target = getNextTarget(line);
				lore.set(loreNdx, StringUtils.colorize("&dTarget: &7" + target) + " Kingdom");
				compass.setLore(lore);

				if (target != null) {
					CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
					compassMeta.setLodestone(locationMap.get(target));
					compassMeta.setLodestoneTracked(false);
					compass.setItemMeta(compassMeta);

					SoundUtils.playSound(event.getPlayer(), Sound.UI_BUTTON_CLICK, 0.5F, 1F);
					ActionBarUtils.sendActionBar(event.getPlayer(), "&dTarget: &f" + target + " Kingdom");
				}
				event.setCancelled(true);
				return;
			}
			++loreNdx;
		}
	}

	private String getNextTarget(String target) {
		String kingdom = (StringUtils.stripColor(target).toLowerCase()
				.replaceAll("target:", "")).replaceAll("kingdom", "").trim();

		String next = null;
		Object[] keys = locationMap.keySet().toArray();

		if (!kingdom.equalsIgnoreCase("???")) {
			for (int i = 0; i < keys.length; i++) {
				String _kingdom = ((String) keys[i]).toLowerCase();
				if (_kingdom.equals(kingdom)) {
					int ndx = i + 1;
					if (ndx > (keys.length - 1))
						ndx = 0;

					next = (String) keys[ndx];
					break;
				}
			}
		} else {
			next = (String) keys[0];
		}

		return next;
	}


}
