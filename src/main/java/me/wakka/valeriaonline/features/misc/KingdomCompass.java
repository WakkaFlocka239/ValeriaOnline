package me.wakka.valeriaonline.features.misc;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.cooldown.Cooldowns;
import me.wakka.valeriaonline.models.compass.Compass;
import me.wakka.valeriaonline.models.compass.CompassService;
import me.wakka.valeriaonline.utils.ActionBarUtils;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.SoundUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class KingdomCompass implements Listener {
	private static final HashMap<UUID, Location> playerMap = new HashMap<>();
	public static final LinkedHashMap<String, Location> locationMap = new LinkedHashMap<>();
	private static final ItemBuilder item = new ItemBuilder(Material.COMPASS)
			.name("Kingdom Compass")
			.lore("&dTarget: &7Elven Kingdom", "&f", "&7[Left-Click: Switch Target]");
	CompassService service = new CompassService();

	public KingdomCompass() {
		ValeriaOnline.registerListener(this);

		List<String> strings = ConfigUtils.getSettings().getStringList("compassLocs");
		for (String string : strings) {
			String[] stringSplit = string.split(", ");

			String name = StringUtils.camelCase(stringSplit[0]);
			int x = Integer.parseInt(stringSplit[1]);
			int z = Integer.parseInt(stringSplit[2]);
			World world = Bukkit.getWorld("world");
			Location loc = new Location(world, x, 0, z);

			locationMap.put(name, loc);
		}

		Tasks.repeat(Time.SECOND.x(5), Time.SECOND, () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				Compass compass = service.get(player);
				boolean enabled = false;
				if (compass.isEnabled() && compass.isKingdom())
					enabled = true;

				boolean hasCompass = false;
				for (ItemStack itemStack : player.getInventory()) {
					if (isKingdomCompass(itemStack)) {
						hasCompass = true;
						break;
					}
				}

				if (hasCompass && !enabled) {
					compass.setEnabled(true);
					compass.setKingdom(true);
					service.save(compass);
					setPlayerLocation(player, false);
					compass.start();
				} else if (!hasCompass && enabled) {
					compass.setEnabled(false);
					compass.setKingdom(false);
					service.save(compass);
					setPlayerLocation(player, true);
					compass.stop();
				}
			}
		});
	}

	private void setPlayerLocation(Player player, boolean delete) {
		if (delete)
			playerMap.remove(player.getUniqueId());
		else {
			Location loc = getKingdomLocation(player);
			if (loc != null)
				playerMap.put(player.getUniqueId(), loc);
		}
	}

	private Location getKingdomLocation(Player player) {
		for (ItemStack itemStack : player.getInventory()) {
			if (isKingdomCompass(itemStack)) {
				List<String> lore = itemStack.getLore();
				if (lore == null || lore.size() == 0) return null;

				for (String line : lore) {
					if (line.contains("Target:")) {
						String kingdom = StringUtils.camelCase(
								StringUtils.stripColor(line)
										.toLowerCase()
										.replaceAll("target:", "")
										.replaceAll("kingdom", "")
										.trim());

						return locationMap.getOrDefault(kingdom, null);
					}
				}
			}
		}

		return null;
	}

	public static ItemStack getItem() {
		ItemStack compass = item.build();
		CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
		compassMeta.setLodestone(locationMap.get("Elven").clone());
		compassMeta.setLodestoneTracked(false);
		compass.setItemMeta(compassMeta);
		return compass;
	}

	public static Location getTarget(Player player) {
		return playerMap.getOrDefault(player.getUniqueId(), null);
	}

	@EventHandler
	public void onClickCompass(PlayerInteractEvent event) {
		if (!Utils.ActionGroup.LEFT_CLICK.applies(event))
			return;

		Player player = event.getPlayer();
		ItemStack compass = Utils.getTool(player);
		if (Utils.isNullOrAir(compass) || !compass.getType().equals(Material.COMPASS)) return;

		List<String> lore = compass.getLore();
		if (lore == null || lore.size() == 0) return;

		if (!isKingdomCompass(compass))
			return;

		// If you're on cooldown
		if (!Cooldowns.check(player, "kingdomCompass", Time.SECOND.x(1))) {
			return;
		}

		int loreNdx = 0;
		for (String line : lore) {
			if (line.contains("Target:")) {
				String target = getNextTarget(line);
				lore.set(loreNdx, StringUtils.colorize("&dTarget: &7" + target) + " Kingdom");
				compass.setLore(lore);

				if (target != null) {
					Location targetLoc = locationMap.get(target).clone();
					playerMap.put(player.getUniqueId(), targetLoc);

					CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
					compassMeta.setLodestone(targetLoc);
					compassMeta.setLodestoneTracked(false);
					compass.setItemMeta(compassMeta);

					SoundUtils.playSound(player, Sound.UI_BUTTON_CLICK, 0.5F, 1F);
					ActionBarUtils.sendActionBar(player, "&dTarget: &f" + target + " Kingdom");
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

	private boolean isKingdomCompass(ItemStack compass) {
		if (Utils.isNullOrAir(compass) || !compass.getType().equals(Material.COMPASS)) return false;

		List<String> lore = compass.getLore();
		if (lore == null || lore.size() == 0) return false;

		boolean hasTarget = false;
		for (String line : lore) {
			if (StringUtils.stripColor(line).contains("Target:"))
				hasTarget = true;
		}

		return hasTarget;
	}


}
