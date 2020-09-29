package me.wakka.valeriaonline.features.altars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Altar {
	World world;
	String regionID;
	boolean active;

	public Altar(String world, String regionID) {
		this.world = Bukkit.getWorld(world);
		this.regionID = regionID;
		active = false;
	}
}


