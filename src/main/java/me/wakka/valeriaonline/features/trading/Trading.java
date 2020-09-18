package me.wakka.valeriaonline.features.trading;

import me.wakka.valeriaonline.Utils.ConfigUtils;
import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.reflections.Reflections;

public class Trading {
	
	public Trading() {
		registerSerializables();
	}

	private String getPath() {
		return this.getClass().getPackage().getName();
	}

	public static YamlConfiguration getConfig() {
		return ConfigUtils.getConfig("trading.yml");
	}

	private void registerSerializables() {
		new Reflections(getPath()).getTypesAnnotatedWith(SerializableAs.class).forEach(clazz -> {
			String alias = clazz.getAnnotation(SerializableAs.class).value();
			ConfigurationSerialization.registerClass((Class<? extends ConfigurationSerializable>) clazz, alias);
		});
	}

}
