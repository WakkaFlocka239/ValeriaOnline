package me.wakka.valeriaonline;

import com.earth2me.essentials.Essentials;
import lombok.Getter;
import me.wakka.valeriaonline.features.altars.Altars;
import me.wakka.valeriaonline.features.autorestart.AutoRestart;
import me.wakka.valeriaonline.features.itemtags.ItemTags;
import me.wakka.valeriaonline.features.listeners.Listeners;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.framework.commands.Commands;
import me.wakka.valeriaonline.framework.persistence.MySQLPersistence;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.SignMenuFactory;
import me.wakka.valeriaonline.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static me.wakka.valeriaonline.utils.StringUtils.stripColor;

public class ValeriaOnline extends JavaPlugin {
	private Commands commands;
	private static ValeriaOnline instance;

	public ValeriaOnline(){
		if (instance == null)
			instance = this;
		else
			Bukkit.getServer().getLogger().info("ValeriaOnline could not be initialized");
	}

	public static ValeriaOnline getInstance() {
		if (instance == null) {
			Bukkit.getServer().getLogger().info("ValeriaOnline could not be initialized");
		}
		return instance;
	}

	public static void log(String message) {
		getInstance().getLogger().info(stripColor(message));
	}

	public static void warn(String message) {
		getInstance().getLogger().warning(stripColor(message));
	}

	public static void severe(String message) {
		getInstance().getLogger().severe(stripColor(message));
	}

	@Override
	public void onEnable() {
		ConfigUtils.setupConfig();
		enableFeatures();
		commands = new Commands(this, "me.wakka.valeriaonline.features");
		commands.registerAll();
	}

	// @formatter:off
	@Override
	public void onDisable() {
		try { Utils.runCommandAsConsole("save-all");	} catch (Throwable ex) { ex.printStackTrace(); }
		try { AutoRestart.shutdown();								} catch (Throwable ex) { ex.printStackTrace(); }

		try { broadcastReload(); 									} catch (Throwable ex) { ex.printStackTrace(); }
		try { MySQLPersistence.shutdown(); 							} catch (Throwable ex) { ex.printStackTrace(); }
	}
	// @formatter:on

	public void broadcastReload() {
		String message = "&c&l! &c&l! &7Reloading ValeriaOnline &c&l! &c&l!";
		Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.hasPermission("group.admin"))
				.forEach(player -> Utils.send(player, Commands.VO_PREFIX + message));
	}

	public static void registerListener(Listener listener) {
		if (getInstance().isEnabled()) {
			getInstance().getServer().getPluginManager().registerEvents(listener, getInstance());
		} else
			log("Could not register listener " + listener.toString() + "!");
	}

	@Getter
	private static SignMenuFactory signMenuFactory;
	@Getter
	private static Essentials essentials;
	@Getter
	private static Economy econ = null;

	private void enableFeatures() {
		new Listeners();
		new ItemTags();
		new Altars();
		new AutoRestart();
		new Trading();

		signMenuFactory = new SignMenuFactory(this);
		essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
		econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
	}


}
