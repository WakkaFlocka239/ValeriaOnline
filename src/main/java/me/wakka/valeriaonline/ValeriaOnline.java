package me.wakka.valeriaonline;

import me.wakka.valeriaonline.Utils.ConfigUtils;
import me.wakka.valeriaonline.Utils.Utils;
import me.wakka.valeriaonline.features.altars.Altars;
import me.wakka.valeriaonline.features.autorestart.AutoRestart;
import me.wakka.valeriaonline.features.itemtags.ItemTags;
import me.wakka.valeriaonline.features.listeners.Listeners;
import me.wakka.valeriaonline.framework.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static me.wakka.valeriaonline.Utils.StringUtils.stripColor;

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
		try { Utils.runCommandAsConsole("save-all");				} catch (Throwable ex) { ex.printStackTrace(); }
		try { AutoRestart.shutdown();											} catch (Throwable ex) { ex.printStackTrace(); }
	}
	// @formatter:on

	public static void registerListener(Listener listener) {
		if (getInstance().isEnabled()) {
			getInstance().getServer().getPluginManager().registerEvents(listener, getInstance());
		} else
			log("Could not register listener " + listener.toString() + "!");
	}

	private void enableFeatures(){
		new Listeners();
		new ItemTags();
		new Altars();
		new AutoRestart();
	}


}
