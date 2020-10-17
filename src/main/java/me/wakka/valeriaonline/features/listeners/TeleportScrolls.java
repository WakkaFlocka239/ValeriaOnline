package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.features.cooldown.Cooldowns;
import me.wakka.valeriaonline.features.dungeons.Dungeons;
import me.wakka.valeriaonline.models.setting.Setting;
import me.wakka.valeriaonline.models.setting.SettingService;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.SoundUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TeleportScrolls implements Listener {
	private static final World world = Bukkit.getWorld("world");
	public static final Location valeria = new Location(world, -111.5, 67, -577.5, 0F, 0F);
	public static final Location eredhil = new Location(world, -219.5, 88, -3661.5, -180F, 0F);
	public static final Location maldun = new Location(world, -718.5, 35, 3388.5, -90F, 0F);
	private static final Map<UUID, Integer> playerTaskIdMap = new HashMap<>();

	public static final ItemStack scroll_valeria = new ItemBuilder(Material.PAPER)
			.name("&f[&dTeleport Scroll&f]")
			.lore("&7Destination: &b&oValeria &7(Human)")
			.unbreakable()
			.damage(10000)
			.glow()
			.build();

	public static final ItemStack scroll_eredhil = new ItemBuilder(Material.PAPER)
			.name("&f[&dTeleport Scroll&f]")
			.lore("&7Destination: &b&oEredhil &7(Elven)")
			.unbreakable()
			.damage(10000)
			.glow()
			.build();

	public static final ItemStack scroll_maldun = new ItemBuilder(Material.PAPER)
			.name("&f[&dTeleport Scroll&f]")
			.lore("&7Destination: &b&oMaldun &7(Dwarven)")
			.unbreakable()
			.damage(10000)
			.glow()
			.build();

	public static final ItemStack scroll_grave = new ItemBuilder(Material.BOOK)
			.name("&f[&dGrave Scroll&f]")
			.lore("&7Use this scroll to get back to your last death location")
			.unbreakable()
			.damage(10000)
			.glow()
			.build();

	public static final List<ItemStack> scrolls = Arrays.asList(scroll_valeria, scroll_eredhil, scroll_maldun, scroll_grave);
	private static final Map<ItemStack, Location> scrollMap = new HashMap<>();

	static {
		scrollMap.put(scroll_valeria, valeria);
		scrollMap.put(scroll_eredhil, eredhil);
		scrollMap.put(scroll_maldun, maldun);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		SettingService service = new SettingService();
		Setting setting = service.get(player, "deathLocation");
		setting.setLocation(player.getLocation());
		service.save(setting);
	}

	@EventHandler
	public void onUseScroll(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;

		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		ItemStack tool = Utils.getTool(player);
		if (tool == null || tool.getLore() == null || tool.getLore().size() == 0)
			return;

		if (!isHoldingAScroll(player))
			return;

		if (Dungeons.isInDungeonOrOnTeam(player)) {
			Dungeons.error(player);
			return;
		}

		ItemStack scroll = getScroll(tool);
		if (scroll == null) {
			Utils.send(player, "&cSomething went wrong (1), report this to WakkaFlocka.");
			return;
		}

		Location loc;
		if (scroll.equals(scroll_grave)) {
			Setting setting = new SettingService().get(player, "deathLocation");

			loc = setting.getLocation();
			if (loc == null) {
				Utils.send(player, "&cYou don't have a death location");
				return;
			}
		} else
			loc = scrollMap.get(scroll);

		if (loc == null) {
			Utils.send(player, "&cSomething went wrong (2), report this to WakkaFlocka.");
			return;
		}

		// If you have an active teleport
		if (playerTaskIdMap.getOrDefault(uuid, null) != null) {
			return;
		}

		// If you're on cooldown
		if (!Cooldowns.check(player, "teleportScroll", Time.SECOND.x(1))) {
			return;
		}

		Location initialLoc = getPlayerBlockLoc(player);
		Location finalLoc = loc;
		int taskId = Tasks.Countdown.builder()
				.duration(Time.SECOND.x(3))
				.onStart(() -> Utils.send(player, "Teleporting in 3 seconds, don't move!"))
				.onTick(i -> {
					if (!isHoldingSameScroll(player, scroll))
						cancelTeleport(player, "Teleport cancelled, you need to hold the scroll");

					if (!initialLoc.equals(getPlayerBlockLoc(player)))
						cancelTeleport(player, "Teleport cancelled, you moved!");
				})
				.onComplete(() -> {
					if (playerTaskIdMap.get(uuid) != null)
						teleportPlayer(player, finalLoc, scroll);
				})
				.start()
				.getTaskId();

		playerTaskIdMap.put(uuid, taskId);
	}

	private Location getPlayerBlockLoc(Player player) {
		Location playerLoc = player.getLocation();
		return new Location(playerLoc.getWorld(), playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ());
	}

	private boolean isHoldingAScroll(Player player) {
		ItemStack tool = Utils.getTool(player);
		if (tool == null || tool.getLore() == null || tool.getLore().size() == 0)
			return false;

		ItemStack single = tool.clone();
		single.setAmount(1);
		return scrolls.contains(single);
	}

	private boolean isHoldingSameScroll(Player player, ItemStack scroll) {
		ItemStack tool = Utils.getTool(player);
		ItemStack _scroll = getScroll(tool);
		if (_scroll == null)
			return false;

		return _scroll.equals(scroll);
	}

	private ItemStack getScroll(ItemStack scroll) {
		if (Utils.isNullOrAir(scroll))
			return null;

		for (ItemStack _scroll : scrolls) {
			if (Objects.equals(scroll.getLore(), _scroll.getLore())) {
				return _scroll;
			}
		}

		return null;
	}

	private void cancelTeleport(Player player, String reason) {
		Utils.send(player, reason);
		int taskId = playerTaskIdMap.get(player.getUniqueId());
		Tasks.cancel(taskId);
		playerTaskIdMap.remove(player.getUniqueId());
	}

	private void teleportPlayer(Player player, Location location, ItemStack scroll) {
		ItemStack tool = Utils.getTool(player);
		if (Utils.isNullOrAir(tool) || !isHoldingSameScroll(player, scroll)) {
			cancelTeleport(player, "Teleport cancelled, you need to hold the scroll");
			return;
		}

		tool.setAmount(tool.getAmount() - 1);
		playerTaskIdMap.remove(player.getUniqueId());
		Utils.send(player, "Teleporting...");

		player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 3000, 0.5, 0.5, 0.5, 3);
		SoundUtils.playSound(player, Sound.BLOCK_PORTAL_TRAVEL, 0.2F, 1F);

		Utils.setPlayerBackLoc(player);
		Tasks.wait(Time.SECOND.x(2), () ->
				player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND));
	}
}
