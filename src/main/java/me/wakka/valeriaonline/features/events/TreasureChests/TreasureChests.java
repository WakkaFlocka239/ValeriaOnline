package me.wakka.valeriaonline.features.events.TreasureChests;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.Commands;
import me.wakka.valeriaonline.models.setting.Setting;
import me.wakka.valeriaonline.models.setting.SettingService;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.MaterialTag;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.wakka.valeriaonline.features.itemtags.ItemTagUtils.updateItem;

public class TreasureChests implements Listener {
	// Settings
	public static final String setting = "treasureChestLocs";
	private static final boolean active = true;
	private static final String activeRegion = "valeria";
	private static final int total = 15;
	private static final String skullOwner = "4bb7ea44-a33a-4023-91d7-d44d28ae5aac";
	private static final String PREFIX = Commands.VO_PREFIX + "&r";
	private static String foundOne = PREFIX + "&aYou found a secret treasure chest! There are still more to find.";
	private static String duplicate = PREFIX + "&cYou already found this one.";
	private static String foundAll = PREFIX + "&6You've found the final treasure chest!";
	//
	SettingService service = new SettingService();
	private static final boolean testing = false;
	//
	private World world = Bukkit.getWorld("world");
	private List<Location> locations = Arrays.asList(
			new Location(world, -218, 72, -516),
			new Location(world, -127, 69, -419),
			new Location(world, -77, 71, -419),
			new Location(world, -45, 74, -474),
			new Location(world, -59, 73, -452),
			new Location(world, -60, 66, -557),
			new Location(world, -17, 64, -555),
			new Location(world, 25, 64, -534),
			new Location(world, 45, 75, -521),
			new Location(world, 13, 67, -448),
			new Location(world, 122, 91, -475),
			new Location(world, 54, 63, -607),
			new Location(world, 162, 63, -553),
			new Location(world, -29, 72, -621),
			new Location(world, -180, 85, -553)
	);

	private final ItemBuilder[] prizes = {
			new ItemBuilder(Material.COOKED_BEEF).amount(16),
			new ItemBuilder(Material.IRON_AXE).amount(1),
			new ItemBuilder(Material.BOOKSHELF).amount(4),
			new ItemBuilder(Material.CAKE).amount(1),
			new ItemBuilder(Material.SHIELD).amount(1),
			new ItemBuilder(Material.POTION).potionEffect(PotionEffectType.HEAL, 1, 2).amount(1),
			new ItemBuilder(Material.EMERALD).amount(1),
			new ItemBuilder(Material.PAPER).amount(500),
			new ItemBuilder(Material.NETHERITE_SCRAP).amount(1),
			new ItemBuilder(Material.TOTEM_OF_UNDYING).amount(1),
			new ItemBuilder(Material.MUSIC_DISC_PIGSTEP).amount(1),
			new ItemBuilder(Material.GOLDEN_APPLE).amount(2),
			new ItemBuilder(Material.EXPERIENCE_BOTTLE).amount(8),
			new ItemBuilder(Material.SHULKER_SHELL).amount(2),
			new ItemBuilder(Material.ENCHANTED_BOOK).enchant(Enchantment.DURABILITY, 3).amount(1)
	};

	public TreasureChests() {
		ValeriaOnline.registerListener(this);

		// Skull Particles Task
		Tasks.repeat(0, Time.SECOND.x(3), () -> {
			if (Utils.isNullOrEmpty(locations))
				return;

			Bukkit.getOnlinePlayers().forEach(player -> {
				if (!player.equals(Utils.wakka()))
					return;

				if (!player.getWorld().equals(world))
					return;

				if (!isInActionRegion(player))
					return;

				for (Location skullLoc : locations) {
					Location centered = Utils.getCenteredLocation(skullLoc);
					centered.setY(centered.getY() + 0.25);

					if (!hasFound(skullLoc, player)) {
						player.spawnParticle(Particle.VILLAGER_HAPPY, centered, 10, 0.25, 0.25, 0.25, 0.01);
					} else {
						player.spawnParticle(Particle.CRIT, centered, 10, 0.25, 0.25, 0.25, 0.01);
					}
				}
			});
		});
	}

	private boolean hasFound(Location skullLoc, Player player) {
		Setting foundLocsSetting = service.get(player, setting);
		if (foundLocsSetting.getLocationList() == null)
			return false;

		return foundLocsSetting.getLocationList().contains(skullLoc);

	}

	private boolean isInActionRegion(Player player) {
		return new WorldGuardUtils(player).isInRegion(player.getLocation(), activeRegion);
	}

	@EventHandler
	public void onRightClickSkull(PlayerInteractEvent event) {
		if (!testing && !active)
			return;

		if (!event.getPlayer().equals(Utils.wakka()))
			return;

		Block block = event.getClickedBlock();
		if (block == null) return;
		if (!MaterialTag.SKULLS.isTagged(block.getType())) return;
		if (!EquipmentSlot.HAND.equals(event.getHand())) return;

		Player player = event.getPlayer();
		if (!isInActionRegion(player)) return;

		Skull skull = (Skull) block.getState();
		if (skull.getOwningPlayer() == null) return;
		if (!skullOwner.equals(skull.getOwningPlayer().getUniqueId().toString())) return;

		Location playerLoc = player.getLocation();
		Location blockLoc = block.getLocation();

		Setting foundLocsSetting = service.get(player, setting);

		List<Location> foundLocs = foundLocsSetting.getLocationList();
		if (foundLocsSetting.getLocationList() == null) {
			foundLocs = new ArrayList<>();
			foundLocsSetting.setLocationList(foundLocs);
		}

		if (foundLocs.contains(blockLoc)) {
			Utils.send(player, duplicate);
			player.playSound(playerLoc, Sound.ENTITY_VILLAGER_NO, 2F, 1F);
			return;
		}

		foundLocs.add(blockLoc);
		ItemBuilder prize = prizes[foundLocs.size() - 1];
		if (!testing)
			foundLocsSetting.addToLocationList(blockLoc);

		if (foundLocs.size() == total) {
			Utils.send(player, foundAll);
			player.playSound(playerLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2F, 1F);

		} else {
			Utils.send(player, foundOne);
			player.playSound(playerLoc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2F, 2F);
			player.playSound(playerLoc, Sound.BLOCK_BEACON_POWER_SELECT, 2F, 2F);

		}

		if (active)
			givePrize(player, prize);

		if (!testing)
			service.save(foundLocsSetting);
	}

	private void givePrize(Player player, ItemBuilder prize) {
		ItemStack item = prize.build();
		if (item.getType().equals(Material.PAPER)) {
			int amount = item.getAmount();
			Utils.send(player, PREFIX + "&aYou received: " + amount + " Crowns!");
			Utils.deposit(player, amount, PREFIX);
		} else {
			Utils.send(player, PREFIX + "&aYou received: " + StringUtils.pretty(item));
			Utils.giveItem(player, updateItem(item));
		}
	}


}
