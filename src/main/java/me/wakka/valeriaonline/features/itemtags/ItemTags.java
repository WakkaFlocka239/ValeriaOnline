package me.wakka.valeriaonline.features.itemtags;

import de.tr7zw.nbtapi.NBTItem;
import lombok.SneakyThrows;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTags {
	private static final Map<Enchantment, List<Level>> enchantsConfigMap = new HashMap<>();
	private static final Map<Enchantment, Integer> customEnchantsConfigMap = new HashMap<>();
	private static final Map<String, Integer> armorConfigMap = new HashMap<>();
	private static final Map<String, Integer> toolConfigMap = new HashMap<>();
	private final YamlConfiguration config;
	private final File configFile;

	public ItemTags() {
		configFile = ConfigUtils.getFile("itemtags.yml");
		config = ConfigUtils.getConfig(configFile);

		if (!config.isConfigurationSection("ItemTags"))
			createConfig();

		loadConfigMaps();

		new Listeners();
	}

	public static int getEnchantVal(Enchantment enchant, int lvl) {
		List<Level> levels = enchantsConfigMap.get(enchant);
		if (levels == null || levels.size() == 0)
			return 0;

		if (lvl > enchant.getMaxLevel())
			return levels.get(levels.size() - 1).getValue();

		for (Level level : levels) {
			if (level.getName().equalsIgnoreCase(lvl + ""))
				return level.getValue();
		}

		return 0;
	}

	public static int getCustomEnchantVal(Enchantment enchant) {
		if (!customEnchantsConfigMap.containsKey(enchant))
			return 0;

		return customEnchantsConfigMap.get(enchant);
	}

	public static Integer getArmorMaterialVal(Material material) {
		String type = parseMaterial(material.name());
		if (!armorConfigMap.containsKey(type))
			return null;

		return armorConfigMap.get(type);
	}

	public static Integer getToolMaterialVal(Material material) {
		String type = parseMaterial(material.name());
		if(!toolConfigMap.containsKey(type))
			return null;

		return toolConfigMap.get(type);
	}

	private void loadConfigMaps(){
		ConfigurationSection enchantments = config.getConfigurationSection("ItemTags.Enchantments");
		if(enchantments != null) {
			for (String key : enchantments.getKeys(false)) {
				List<Level> levels = new ArrayList<>();
				Enchantment enchant = parseEnchantment(key);

				ConfigurationSection section = config.getConfigurationSection(enchantments.getCurrentPath() + "." + key);
				if(section != null) {
					for (String sectionKey : section.getKeys(false)) {
						int value = section.getInt(sectionKey);
						levels.add(new Level(sectionKey, value));
					}
				}

				enchantsConfigMap.put(enchant, levels);
			}
		}

		ConfigurationSection armorMaterials = config.getConfigurationSection("ItemTags.Material.Armor");
		if(armorMaterials != null) {
			for (String key : armorMaterials.getKeys(false)) {
				String material = parseMaterial(key);
				int value = armorMaterials.getInt(key);
				armorConfigMap.put(material, value);
			}
		}

		ConfigurationSection toolMaterials = config.getConfigurationSection("ItemTags.Material.Tool");
		if (toolMaterials != null) {
			for (String key : toolMaterials.getKeys(false)) {
				String material = parseMaterial(key);
				int value = toolMaterials.getInt(key);
				toolConfigMap.put(material, value);
			}
		}

		ConfigurationSection customEnchants = config.getConfigurationSection("ItemTags.CustomEnchants");
		if (customEnchants != null) {
			for (String key : customEnchants.getKeys(false)) {
				Enchantment enchant = parseEnchantment(key);
				int value = customEnchants.getInt(key);

				customEnchantsConfigMap.put(enchant, value);
			}
		}
	}

	private Enchantment parseEnchantment(String value){
		for (Enchantment enchantment : Enchantment.values()) {
			String key = StringUtils.camelCaseWithUnderscores(enchantment.getKey().getKey());
			if(key.equalsIgnoreCase(value))
				return enchantment;
		}
		return null;
	}

	public static String parseMaterial(String value){
		String[] strings = value.toLowerCase().split("_");
		String ingot = strings[0];

		// Special cases
		if(ingot.equalsIgnoreCase("golden"))
			ingot = "gold";
		else if (ingot.equalsIgnoreCase("turtle"))
			ingot = "turtle_shell";
		else if (ingot.equalsIgnoreCase("wooden"))
			ingot = "wood";
		//

		return StringUtils.camelCase(ingot).replace(" ", "_");
	}

	@SneakyThrows
	private void createConfig(){
		config.createSection("ItemTags");
		config.createSection("ItemTags.Enchantments");
		ConfigurationSection section;

		// Enchantments
		Enchantment[] enchants = Enchantment.values();
		Arrays.sort(enchants, (enchant1, enchant2) -> {
			String ench1 = StringUtils.camelCaseWithUnderscores(enchant1.getKey().getKey());
			String ench2 = StringUtils.camelCaseWithUnderscores(enchant2.getKey().getKey());
			return ench1.compareTo(ench2);
		});

		for (Enchantment enchant : enchants) {
			String enchantName = StringUtils.camelCaseWithUnderscores(enchant.getKey().getKey());
			section = config.createSection("ItemTags.Enchantments." + enchantName);

			int maxLvl = enchant.getMaxLevel();
			for (int i = 1; i <= maxLvl; i++) {
				String level = i + "";
				section.set(level, 0);
			}
			section.set("above", 99);

		}

		// Armor Materials
		section = config.createSection("ItemTags.Material.Armor");
		section.set("Gold", 0);
		section.set("Leather", 0);
		section.set("Turtle_Shell", 0);
		section.set("Elytra", 0);
		section.set("Chainmail", 0);
		section.set("Iron", 0);
		section.set("Diamond", 0);
		section.set("Netherite", 0);

		// Tool Materials
		section = config.createSection("ItemTags.Material.Tool");
		section.set("Bow", 0);
		section.set("Shield", 0);
		section.set("Fishing_Rod", 0);
		section.set("Crossbow", 0);
		section.set("Trident", 0);
		section.set("Wood", 0);
		section.set("Stone", 0);
		section.set("Gold", 0);
		section.set("Iron", 0);
		section.set("Diamond", 0);
		section.set("Netherite", 0);

		section = config.createSection("ItemTags.CustomEncahnts");
		section.set("Paralyze", 0);
		section.set("Double_Strike", 0);
		section.set("Divine_Touch", 0);
		section.set("Ender_Bow", 0);

		config.save(configFile);
	}

	public static boolean isArmor(ItemStack itemStack){
		String name = itemStack.getType().name().toLowerCase();
		if(itemStack.getType().equals(Material.ELYTRA))
			return true;

		return name.endsWith("_helmet")
				|| name.endsWith("_chestplate")
				|| name.endsWith("_leggings")
				|| name.endsWith("_boots");
	}

	public static boolean isTool(ItemStack itemStack){
		Material type = itemStack.getType();
		List<Material> uniqueTools = Arrays.asList(Material.BOW, Material.CROSSBOW,
				Material.TRIDENT, Material.SHIELD, Material.FISHING_ROD);

		if (uniqueTools.contains(type))
			return true;

		String name = type.name().toLowerCase();
		return name.endsWith("_sword")
				|| name.endsWith("_shovel")
				|| name.endsWith("_pickaxe")
				|| name.endsWith("_axe")
				|| name.endsWith("_hoe");
	}

	public static boolean isMythicMobsItem(ItemStack itemStack) {
		List<String> lore = itemStack.getLore();
		if (lore != null && lore.size() > 0) {
			for (String line : lore) {
				if (StringUtils.stripColor(line).equalsIgnoreCase("----⫷Unique⫸----"))
					return true;
			}
		}

		NBTItem nbtItem = new NBTItem(itemStack);
		String nbtString;
		if (nbtItem.hasNBTData()) {
			nbtString = nbtItem.toString();
			if (nbtString.contains("MYTHIC_TYPE"))
				return true;
		}

		return false;
	}
}
