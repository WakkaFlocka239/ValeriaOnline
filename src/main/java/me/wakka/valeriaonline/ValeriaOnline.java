package me.wakka.valeriaonline;

import com.earth2me.essentials.Essentials;
import com.gmail.nossr50.mcMMO;
import github.scarsz.discordsrv.DiscordSRV;
import io.lumine.xikage.mythicmobs.MythicMobs;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.Getter;
import me.wakka.valeriaonline.features.autorestart.AutoRestart;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.features.placeholders.Placeholders;
import me.wakka.valeriaonline.framework.commands.Commands;
import me.wakka.valeriaonline.framework.features.Features;
import me.wakka.valeriaonline.framework.persistence.MongoDBPersistence;
import me.wakka.valeriaonline.framework.persistence.MySQLPersistence;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.Env;
import me.wakka.valeriaonline.utils.SignMenuFactory;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import me.wakka.valeriaonline.utils.WorldGuardFlagUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TimeZone;
import java.util.UUID;

import static me.wakka.valeriaonline.utils.StringUtils.stripColor;

public class ValeriaOnline extends JavaPlugin {
	private Commands commands;
	private Features features;
	private static ValeriaOnline instance;
	@Getter
	private final static UUID UUID0 = new UUID(0, 0);

	public ValeriaOnline() {
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

	public static Env getEnv() {
		String env = ConfigUtils.getSettings().getString("env", Env.DEV.name()).toUpperCase();
		try {
			return Env.valueOf(env);
		} catch (IllegalArgumentException ex) {
			ValeriaOnline.severe("Could not parse environment variable " + env + ", options are: " + String.join(", ", Utils.EnumUtils.valueNameList(Env.class)));
			ValeriaOnline.severe("Defaulting to " + Env.DEV.name() + " environment");
			return Env.DEV;
		}
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
		new Time.Timer("Enable", () -> {
			ConfigUtils.setupConfig();
			new Time.Timer(" Databases", this::databases);
			new Time.Timer(" Hooks", this::hooks);
			new Time.Timer(" Features", () -> {
				features = new Features(this, "me.wakka.valeriaonline.features");
				features.register(Chat.class); // prioritize
				features.registerAll();
			});
			new Time.Timer(" Commands", () -> {
				commands = new Commands(this, "me.wakka.valeriaonline.features");
				commands.registerAll();
			});
		});
	}

	@Override
	public void onLoad() {
		WorldGuardFlagUtils.CustomFlags.register();
	}


	// @formatter:off
	@Override
	public void onDisable() {
		log("Disabling...");

		try { broadcastReload(); 									} catch (Throwable ex) { ex.printStackTrace(); }
		try { Utils.runCommandAsConsole("save-all");	} catch (Throwable ex) { ex.printStackTrace(); }
		try { cron.stop();											} catch (Throwable ex) { ex.printStackTrace(); }

		try { commands.unregisterAll();								} catch (Throwable ex) { ex.printStackTrace(); }
		try { features.unregisterExcept(Chat.class);				} catch (Throwable ex) { ex.printStackTrace(); }
		try { features.unregister(Chat.class);						} catch (Throwable ex) { ex.printStackTrace(); }
		try { MySQLPersistence.shutdown(); 							} catch (Throwable ex) { ex.printStackTrace(); }
		try { MongoDBPersistence.shutdown();							} catch (Throwable ex) { ex.printStackTrace(); }

		log("Disabled.");
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
	private static final DiscordSRV discordSRV = DiscordSRV.getPlugin();
	@Getter
	private static SignMenuFactory signMenuFactory;
	@Getter
	private static Essentials essentials;
	@Getter
	private static final MythicMobs mythicMobs = MythicMobs.inst();
	@Getter
	private static mcMMO mcmmo;
	@Getter
	private static Economy econ = null;
	@Getter
	private static Permission perms = null;

	@Getter
	// http://www.sauronsoftware.it/projects/cron4j/manual.php
	private static final Scheduler cron = new Scheduler();

	private void databases() {
		//
	}

	private void hooks() {
		signMenuFactory = new SignMenuFactory(this);
		essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

		cron.setTimeZone(TimeZone.getTimeZone(AutoRestart.zone));
		cron.start();

		econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
		mcmmo = mcMMO.p;

		new Placeholders().register();
	}


}
