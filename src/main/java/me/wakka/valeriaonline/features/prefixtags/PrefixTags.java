package me.wakka.valeriaonline.features.prefixtags;

import lombok.Getter;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.models.fame.PrefixTag;
import me.wakka.valeriaonline.utils.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixTags {
	@Getter
	public static List<PrefixTag> activeTags = new ArrayList<>();
	private static Map<String, String> groupFormats = new HashMap<>();
	private static File configFile;
	@Getter
	private static YamlConfiguration config;

	public PrefixTags() {
		configFile = ConfigUtils.getFile("prefixtags.yml");
		config = ConfigUtils.getConfig(configFile);

		loadTags();
		loadGroupFormats();
	}

	private void loadGroupFormats() {
		ConfigurationSection section = config.getConfigurationSection("groupformats");
		if (section == null)
			return;

		for (String key : section.getKeys(false)) {
			String groupKey = key.toUpperCase();
			String groupValue = section.getString(key);
			groupFormats.put(groupKey, groupValue);
		}
	}

	private void loadTags() {
		ConfigurationSection section = config.getConfigurationSection("prefixtags");
		if (section == null)
			return;

		for (String key : section.getKeys(false)) {
			ConfigurationSection tagSection = config.getConfigurationSection("prefixtags." + key);
			if (tagSection == null)
				continue;

			try {
				String name = tagSection.getName();
				String format = tagSection.getString("format");
				int cost = tagSection.getInt("cost");
				PrefixTagType type = PrefixTagType.valueOf(tagSection.getString("type"));

				activeTags.add(new PrefixTag(name, format, cost, type));
			} catch (Exception ignored) {
				ValeriaOnline.warn("Failed to load PrefixTag: " + tagSection.getName());
			}
		}

	}

	public static PrefixTag parseTag(String tagName) {
		for (PrefixTag activeTag : getActiveTags()) {
			if (activeTag.getName().equalsIgnoreCase(tagName))
				return activeTag;
		}

		return null;
	}

	public static List<PrefixTag> getTags(Player player) {
		List<PrefixTag> result = new ArrayList<>();
		for (PrefixTag prefixTag : PrefixTags.getActiveTags()) {
			String permission = prefixTag.getPermission();
			if (!player.hasPermission(permission))
				continue;

			result.add(prefixTag);
		}
		return result;
	}

	public static void setActiveTag(Player player, @Nullable PrefixTag tag) {
		FameService service = new FameService();
		Fame fame = service.get(player);
		if (tag == null)
			fame.setActiveTag(null);
		else
			fame.setActiveTag(tag.getName());
		service.save(fame);
	}

	public static String getGroupFormat(Player player) {
		String group = ValeriaOnline.getPerms().getPrimaryGroup(player);
		return groupFormats.get(group.toUpperCase());
	}

	public enum PrefixTagType {
		QUEST,
		GUILD,
		OTHER
	}
}
