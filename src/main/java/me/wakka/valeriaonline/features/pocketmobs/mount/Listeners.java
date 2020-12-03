package me.wakka.valeriaonline.features.pocketmobs.mount;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.pocketmobs.PocketMobsCommand;
import me.wakka.valeriaonline.features.pocketmobs.mount.mounts.Mount;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class Listeners implements Listener {
	public Listeners() {
		ValeriaOnline.registerListener(this);
	}

	@EventHandler
	public void onOpenMountInventory(InventoryOpenEvent event) {
		if (PocketMobsCommand.mount == null)
			return;

		if (!(event.getInventory().getHolder() instanceof Entity))
			return;

		Entity entity = (Entity) event.getInventory().getHolder();
		if (PocketMobsCommand.mount.getEntity().equals(entity)) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onMountDeath(EntityDeathEvent event) {
		if (PocketMobsCommand.mount == null)
			return;

		Mount mount = PocketMobsCommand.mount;
		if (mount.getEntity().equals(event.getEntity())) {
			mount.setDead(true);
			mount.setSpawned(false);
			mount.setHealth(1);
		}
	}

	@EventHandler
	public void onMountDamage(EntityDamageEvent event) {
		if (PocketMobsCommand.mount == null)
			return;

		if (PocketMobsCommand.mount.getEntity().equals(event.getEntity())) {
			LivingEntity livingEntity = (LivingEntity) PocketMobsCommand.mount.getEntity();
			PocketMobsCommand.mount.setHealth(livingEntity.getHealth() - event.getFinalDamage());
		}
	}
}
