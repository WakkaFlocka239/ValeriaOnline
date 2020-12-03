package me.wakka.valeriaonline.features.pocketmobs.mount;

import com.destroystokyo.paper.entity.Pathfinder;
import me.wakka.valeriaonline.features.pocketmobs.PocketMobsCommand;
import me.wakka.valeriaonline.features.pocketmobs.mount.mounts.Mount;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public class Mounts {

	public Mounts() {
		new Listeners();

		startFollowTask();
	}

	private void startFollowTask() {
		Tasks.repeat(Time.SECOND, Time.TICK.x(10), () -> {
			Mount mount = PocketMobsCommand.mount;
			if (mount == null)
				return;

			if (!mount.isSpawned() || mount.isDead())
				return;

			Player player = mount.getOwningPlayer();
			if (player == null) {
				mount.despawn();
				return;
			}

			if (!mount.getEntity().getWorld().equals(player.getWorld())) {
				mount.despawn();
				return;
			}

			Entity entity = mount.getEntity();
			double distance = player.getLocation().distance(entity.getLocation());

			Mob mob = (Mob) entity;
			Pathfinder pathfinder = mob.getPathfinder();

			if (distance < 4.0) {
				pathfinder.stopPathfinding();
				return;
			}

			mob.setTarget(player);
			pathfinder.moveTo(player, 1.3);

			if (distance > 40.0)
				mount.teleport();
		});
	}
}
