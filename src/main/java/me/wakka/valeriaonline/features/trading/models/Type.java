package me.wakka.valeriaonline.features.trading.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum Type {

	DESERT(Material.DEAD_BUSH),
	JUNGLE(Material.JUNGLE_SAPLING),
	PLAINS(Material.OAK_SAPLING),
	TAIGA(Material.SPRUCE_SAPLING),
	SAVANNA(Material.ACACIA_SAPLING),
	SWAMP(Material.LILY_PAD),
	SNOW(Material.SNOWBALL);

	@Getter
	Material material;

}
