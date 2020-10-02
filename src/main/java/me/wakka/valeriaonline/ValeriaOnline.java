package me.wakka.valeriaonline;

import com.earth2me.essentials.Essentials;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.Getter;
import me.wakka.valeriaonline.features.altars.Altars;
import me.wakka.valeriaonline.features.autorestart.AutoRestart;
import me.wakka.valeriaonline.features.chat.ChannelManager;
import me.wakka.valeriaonline.features.compass.Compass;
import me.wakka.valeriaonline.features.itemtags.ItemTags;
import me.wakka.valeriaonline.features.listeners.AmbientSounds;
import me.wakka.valeriaonline.features.listeners.Listeners;
import me.wakka.valeriaonline.features.placeholders.Placeholders;
import me.wakka.valeriaonline.features.playershops.PlayerShops;
import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.framework.commands.Commands;
import me.wakka.valeriaonline.framework.persistence.MySQLPersistence;
import me.wakka.valeriaonline.models.hours.HoursFeature;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.SignMenuFactory;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardFlagUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TimeZone;

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

	@Override
	public void onLoad() {
		WorldGuardFlagUtils.CustomFlags.register();
	}


	// @formatter:off
	@Override
	public void onDisable() {
		try { Utils.runCommandAsConsole("save-all");	} catch (Throwable ex) { ex.printStackTrace(); }
		try { AmbientSounds.shutdown();								} catch (Throwable ex) { ex.printStackTrace(); }
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
	// http://www.sauronsoftware.it/projects/cron4j/manual.php
	private static Scheduler cron = new Scheduler();

	@Getter
	private static Economy econ = null;
	@Getter
	private static Permission perms = null;

	private void enableFeatures() {
		new Listeners();
		new ItemTags();
		new Altars();
		new AutoRestart();
		new Trading();
		new Compass();
		new PlayerShops();
		new AmbientSounds();
		new HoursFeature();

		new Placeholders().register();
		new ChannelManager();

		signMenuFactory = new SignMenuFactory(this);
		essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

		cron.setTimeZone(TimeZone.getTimeZone(AutoRestart.zone));
		cron.start();

		econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
	}


}
