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

//	@SneakyThrows
//	public static void setConfigValue(String filePath, String path, Object value){
//		File file = getFile(filePath);
//		YamlConfiguration config = getConfig(file);
//		config.set(path, value);
//		config.save(file);
//	}

//	@SneakyThrows
//	public static void setSettingsValue(String path, Object value){
//		settings.set(path, value);
//		settings.save(settingsFile);
//	}

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

		settings.save(settingsFile);
	}


}
