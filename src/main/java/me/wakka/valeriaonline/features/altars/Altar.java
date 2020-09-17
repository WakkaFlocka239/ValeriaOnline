package me.wakka.valeriaonline.features.altars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.World;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Altar {
	World world;
	String regionID;
	boolean active;

	public Altar(World world, String regionID){
		this.world = world;
		this.regionID = regionID;
		active = false;
	}
}


