package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class MobSpanwers implements Listener {
	private static final List<Location> triggered = new ArrayList<>();

	@EventHandler
	public void onOpenTrapChest(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (Utils.isNullOrAir(block))
			return;

		if (!block.getType().equals(Material.TRAPPED_CHEST))
			return;

		Block spawner = block.getRelative(BlockFace.DOWN);
		if (!spawner.getType().equals(Material.SPAWNER)) {
			return;
		}

		explodeSpawner(spawner);
	}

	private void explodeSpawner(Block block) {
		World world = block.getWorld();
		Location loc = block.getLocation();
		if (triggered.contains(loc))
			return;

		triggered.add(loc);

		Tasks.wait(Time.SECOND.x(5), () -> {
			if (!block.getType().equals(Material.SPAWNER))
				return;

			world.playSound(loc, Sound.ENTITY_TNT_PRIMED, 1F, 1F);

			Tasks.wait(Time.SECOND.x(2), () -> {
				if (!block.getType().equals(Material.SPAWNER))
					return;

				world.createExplosion(loc, 4F, true, true);
				block.setType(Material.AIR);
			});
		});

		Tasks.wait(Time.SECOND.x(10), () -> triggered.remove(loc));

	}
}
