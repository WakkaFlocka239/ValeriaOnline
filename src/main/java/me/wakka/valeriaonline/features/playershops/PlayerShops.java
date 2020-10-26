package me.wakka.valeriaonline.features.playershops;

import me.wakka.valeriaonline.framework.features.Feature;
import me.wakka.valeriaonline.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;

public class PlayerShops extends Feature {
	public static final double setupCost = ConfigUtils.getSettings().getDouble("shopSetupCost");
	public static final double resetCost = ConfigUtils.getSettings().getDouble("shopResetCost");
	public static final List<World> allowedWorlds = Arrays.asList(Bukkit.getWorld("world"), Bukkit.getWorld("world_nether"));

	@Override
	public void startup() {

	}
}
