package me.wakka.valeriaonline.features.pocketmobs.mount;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MountType {
	// @formatter:off
	COW(			EntityType.COW, 			Material.COW_SPAWN_EGG,				"cow"),
	HOGLIN(			EntityType.HOGLIN, 			Material.HOGLIN_SPAWN_EGG,			"hoglin"),
	HORSE(			EntityType.HORSE, 			Material.HORSE_SPAWN_EGG,			"horse"),
	LLAMA(			EntityType.LLAMA, 			Material.LLAMA_SPAWN_EGG,			"llama"),
	MULE(			EntityType.MULE, 			Material.MULE_SPAWN_EGG,			"mule"),
	PANDA(			EntityType.PANDA, 			Material.PANDA_SPAWN_EGG,			"panda"),
	POLAR_BEAR(		EntityType.POLAR_BEAR, 		Material.POLAR_BEAR_SPAWN_EGG,		"polarbear"),
	SHEEP(			EntityType.SHEEP, 			Material.SHEEP_SPAWN_EGG,			"sheep"),
	SKELETON_HORSE(	EntityType.SKELETON_HORSE, 	Material.SKELETON_HORSE_SPAWN_EGG,	"skeletonhorse"),
	ZOGLIN(			EntityType.ZOGLIN, 			Material.ZOGLIN_SPAWN_EGG,			"zoglin"),
	ZOMBIE_HORSE(	EntityType.ZOMBIE_HORSE, 	Material.ZOMBIE_HORSE_SPAWN_EGG,	"zombiehorse");
	// @formatter:on

	@Getter
	private final EntityType entityType;
	@Getter
	private final Material displayMaterial;
	@Getter
	private final String permission;

	MountType(EntityType entityType, Material displayMaterial, String permission) {
		this.entityType = entityType;
		this.displayMaterial = displayMaterial;
		this.permission = permission;
	}

	public static MountType of(EntityType type) {
		return Arrays.stream(values()).filter(mountType -> type.equals(mountType.getEntityType())).findFirst().orElse(null);
	}


	public static List<MountType> getMountTypes(Player player) {
		return Arrays.stream(values())
				.filter(mountType -> player.hasPermission("mount." + mountType.getPermission()))
				.collect(Collectors.toList());
	}
}
