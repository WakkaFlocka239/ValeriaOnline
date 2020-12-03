package me.wakka.valeriaonline.features.pocketmobs.mount.mounts;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.features.pocketmobs.mount.MountType;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class Mount {
	UUID owner;

	String name;
	Entity entity;
	UUID uuid;
	MountType type;

	boolean godmode = false;
	boolean spawned = false;
	boolean dead = false;
	boolean hasBaby = true;

	double health = 20;
	double maxHealth = 20;

	boolean storage = false;
	int storageRows = 0;

	// Player Editable
	ItemStack[] inventory;
	boolean rideable = false; // allow others to ride
	boolean baby = false;

	public Mount(Player owner, Entity entity) {
		this(
				owner,
				!Strings.isNullOrEmpty(entity.getCustomName()) ? entity.getCustomName() : "",
				entity.getUniqueId(),
				MountType.of(entity.getType()),
				false);
	}


	public Mount(Player owner, String name, UUID uuid, MountType type, boolean godmode) {
		this.owner = owner.getUniqueId();
		this.name = name;
		this.uuid = uuid;
		this.type = type == null ? MountType.HORSE : type;
		this.godmode = godmode;
	}

	public EntityType getEntityType() {
		return type.getEntityType();
	}

	public Player getOwningPlayer() {
		OfflinePlayer offlinePlayer = Utils.getPlayer(owner);
		if (!offlinePlayer.isOnline())
			return null;

		return offlinePlayer.getPlayer();
	}

	public boolean spawn() {
		if (isSpawned()) {
			return false;
		}

		if (isDead()) {
			Utils.send(getOwningPlayer(), "&cYou can't summon a dead mount, you need to revive it.");
			return false;
		}

		Player owner = getOwningPlayer();
		if (owner == null) {
			return false;
		}

		EntityType entityType = getEntityType();
		if (entityType == null) {
			return false;
		}

		Location loc = owner.getLocation();
		entity = owner.getWorld().spawnEntity(loc, entityType);
		spawned = true;

		//
		LivingEntity livingEntity = (LivingEntity) entity;
		AttributeInstance attribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if (attribute != null)
			attribute.setBaseValue(maxHealth);

		livingEntity.setHealth(health);

		return true;
	}

	public void despawn() {
		if (!isSpawned() || isDead())
			return;

		Player owner = getOwningPlayer();
		if (owner == null)
			return;

		EntityType entityType = getEntityType();
		if (entityType == null)
			return;

		saveMount();
		entity.eject();
		entity.remove();
		spawned = false;
	}

	public void teleport() {
		entity.eject();

		Location loc = getOwningPlayer().getLocation();
		World world = loc.getWorld();
		Location surface = world.getHighestBlockAt(loc).getLocation();
		entity.teleport(surface);
	}

	public void saveMount() {
		LivingEntity livingEntity = (LivingEntity) entity;
		this.health = livingEntity.getHealth();
	}
}
