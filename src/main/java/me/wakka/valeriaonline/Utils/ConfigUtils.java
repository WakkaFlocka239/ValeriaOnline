package me.wakka.valeriaonline.Utils;

import lombok.SneakyThrows;
import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigUtils {

	@SneakyThrows
	public static void setupConfig() {
		if (!ValeriaOnline.getInstance().getDataFolder().exists())
			ValeriaOnline.getInstance().getDataFolder().mkdir();

		File settingsFile = new File(ValeriaOnline.getInstance().getDataFolder(), "settings.yml");
		if(!settingsFile.exists()){
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
	public static YamlConfiguration getSettings(){
		return getConfig("settings.yml");
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
	private static void setupDefaultSettings(File settingsFile){
		YamlConfiguration settings = getSettings();
		settings.set("prefix", "&f[&bValeriaOnline&f] ");
		settings.set("endPortalRegions", Arrays.asList("", ""));

		settings.save(settingsFile);
	}


}
