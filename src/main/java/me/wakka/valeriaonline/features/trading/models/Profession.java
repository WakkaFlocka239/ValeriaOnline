package me.wakka.valeriaonline.features.trading.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum Profession {

	FARMER(Material.COMPOSTER),
	LEATHERWORKER(Material.CAULDRON),
	FISHERMAN(Material.BARREL),
	LIBRARIAN(Material.LECTERN),
	ARMORER(Material.BLAST_FURNACE),
	TOOLSMITH(Material.SMITHING_TABLE),
	SHEPHERD(Material.LOOM),
	BUTCHER(Material.SMOKER),
	CLERIC(Material.BREWING_STAND),
	CARTOGRAPHER(Material.CARTOGRAPHY_TABLE),
	MASON(Material.STONECUTTER),
	WEAPONSMITH(Material.GRINDSTONE),
	FLETCHER(Material.FLETCHING_TABLE),
	WANDERING_TRADER(Material.WANDERING_TRADER_SPAWN_EGG);

	@Getter
	Material material;

}
