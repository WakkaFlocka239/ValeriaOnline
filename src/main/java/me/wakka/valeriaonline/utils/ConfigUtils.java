package me.wakka.valeriaonline.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigUtils {

	private static final File settingsFile;
	@Getter
	private static final YamlConfiguration settings;

	static {
		settingsFile = getFile("settings.yml");
		settings = getConfig(settingsFile);
	}

	@SneakyThrows
	public static void setupConfig() {
		if (!ValeriaOnline.getInstance().getDataFolder().exists())
			ValeriaOnline.getInstance().getDataFolder().mkdir();

		File settingsFile = new File(ValeriaOnline.getInstance().getDataFolder(), "settings.yml");
		if (!settingsFile.exists()) {
			ValeriaOnline.log("settings.yml not found, creating!");

			if(!settingsFile.createNewFile()) {
				ValeriaOnline.severe("Could not generate settings.yml");
				return;
			}

			setupDefaultSettings(settingsFile);
			ValeriaOnline.log("settings.yml generated.");
		}
	}

	@SneakyThrows
	public static File getFile(String path){
		File file = Paths.get("plugins/ValeriaOnline/" + path).toFile();
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	@SneakyThrows
	public static YamlConfiguration getConfig(String path) {
		return getConfig(getFile(path));
	}

	@SneakyThrows
	public static YamlConfiguration getConfig(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}

	@SneakyThrows
	private static void setupDefaultSettings(File settingsFile) {
		YamlConfiguration settings = getSettings();
		settings.set("prefix", "&f&l[&bValeriaOnline&f&l] ");
		settings.set("endPortalRegions", Arrays.asList("", ""));

		settings.set("databases.mysql.host", "localhost");
		settings.set("databases.mysql.port", 3306);
		settings.set("databases.mysql.username", "root");
		settings.set("databases.mysql.password", "password");
		settings.set("databases.mysql.prefix", "");

		settings.set("autorestart.warnTimes", Arrays.asList(10.0, 5.0, 1.0));
		settings.set("autorestart.prefix", "[AutoRestart] ");
		settings.set("autorestart.warnMsg", "Server will restart in... <minutes> minutes");
		settings.set("autorestart.restartMsg", "Server is restarting!");
		settings.set("autorestart.interval", 6);
		settings.set("autorestart.startTime", 6);
		settings.set("autorestart.cancelTime", 30.0);
		settings.set("autorestart.timezone", "GMT+2");

		settings.save(settingsFile);
	}


}
