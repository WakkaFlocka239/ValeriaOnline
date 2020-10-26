package me.wakka.valeriaonline.features.trading;

import lombok.Getter;
import lombok.SneakyThrows;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.framework.features.Feature;
import me.wakka.valeriaonline.utils.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.reflections.Reflections;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Trading extends Feature {
	private static File configFile;
	@Getter
	private static YamlConfiguration config;

	@Override
	public void startup() {

		registerSerializables();
		configFile = ConfigUtils.getFile("trading.yml");
		config = ConfigUtils.getConfig(configFile);
	}

	//Config Save Structure:
	// profession.level.id.trade
	// 	id is only used to reference in the file
	// 	a simple loop over config sections will do
	// 	to get the trades.

	// Get All Trades Per Profession and Level
	public static List<Trade> getTrades(Profession profession, int level) {
		List<Trade> trades = new ArrayList<>();
		if (getConfig().getConfigurationSection(profession.name().toLowerCase() + "." + level) == null)
			return trades;

		Set<String> sections = getConfig().getConfigurationSection(profession.name().toLowerCase() + "." + level).getKeys(false);
		if (sections.isEmpty())
			return trades;

		for (String section : sections) {
			Trade trade = (Trade) getConfig().get(profession.name().toLowerCase() + "." + level + "." + section);
			trade.setId(Integer.parseInt(section));
			trades.add(trade);
		}
		return trades;
	}

	@SneakyThrows
	public static void save() {
		config.save(configFile);
	}

	public static int getNextID(Profession profession, int level) {
		int id = 0;
		if (getConfig().getConfigurationSection(profession.name().toLowerCase() + "." + level) == null)
			return id;
		for (String string : getConfig().getConfigurationSection(profession.name().toLowerCase() + "." + level).getKeys(false)) {
			if (id <= Integer.parseInt(string))
				id = Integer.parseInt(string) + 1;
		}
		return id;
	}

	private String getPath() {
		return this.getClass().getPackage().getName();
	}

	private void registerSerializables() {
		new Reflections(getPath()).getTypesAnnotatedWith(SerializableAs.class).forEach(clazz -> {
			String alias = clazz.getAnnotation(SerializableAs.class).value();
			ConfigurationSerialization.registerClass((Class<? extends ConfigurationSerializable>) clazz, alias);
		});
	}

}
