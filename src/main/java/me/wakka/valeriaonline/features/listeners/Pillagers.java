package me.wakka.valeriaonline.features.listeners;

import com.google.common.base.Strings;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class Pillagers implements Listener {

	@EventHandler
	public void onPillagerSpawn(EntitySpawnEvent event) {
		if (!event.getEntityType().equals(EntityType.PILLAGER))
			return;

		if (Strings.isNullOrEmpty(event.getEntity().getCustomName()))
			return;

		if (!(event.getEntity() instanceof LivingEntity))
			return;

		LivingEntity livingEntity = (LivingEntity) event.getEntity();
		livingEntity.setRemoveWhenFarAway(false);
	}
}
