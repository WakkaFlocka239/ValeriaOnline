package me.wakka.valeriaonline.features.trading;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum Professions {

		FARMER(Material.COMPOSTER),
		LEATHER_WORKER(Material.CAULDRON),
		FISHERMEN(Material.BARREL),
		LIBRARIAN(Material.LECTERN),
		ARMORER(Material.BLAST_FURNACE),
		TOOL_SMITH(Material.SMITHING_TABLE),
		SHEPPARD(Material.LOOM),
		BUTCHER(Material.SMOKER),
		CLERIC(Material.BREWING_STAND),
		CARTOGRAPHER(Material.CARTOGRAPHY_TABLE),
		MASON(Material.STONECUTTER),
		WEAPON_SMITH(Material.GRINDSTONE),
		FLETCHER(Material.FLETCHING_TABLE),
		WANDERING_TRADER(Material.WANDERING_TRADER_SPAWN_EGG);

		@Getter
		Material material;

}
