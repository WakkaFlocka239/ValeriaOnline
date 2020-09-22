package me.wakka.valeriaonline.features.itemtags;

import lombok.Getter;
import me.wakka.valeriaonline.utils.ColorType;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Rarity {
	// @formatter:off
	COMMON(ColorType.LIGHT_GREEN.getChatColor(),	 0 ,10),
	UNCOMMON(ColorType.GREEN.getChatColor(), 		11, 19),
	RARE(ColorType.PINK.getChatColor(),				20, 24),
	EPIC(ColorType.PURPLE.getChatColor(),			25, 29),
	LEGENDARY(ColorType.ORANGE.getChatColor(),		30, 40),
	EXOTIC(ColorType.YELLOW.getChatColor()),
	MYTHIC(ColorType.CYAN.getChatColor()),
	ARTIFACT(ColorType.LIGHT_RED.getChatColor());
	// @formatter:on

	@Getter
	private final ChatColor chatColor;
	@Getter
	private final boolean craftable;
	@Getter
	private final Integer min;
	@Getter
	private final Integer max;

	Rarity(ChatColor chatColor, int min, int max){
		this.chatColor = chatColor;
		this.craftable = true;
		this.min = min;
		this.max = max;
	}

	Rarity(ChatColor chatColor) {
		this.chatColor = chatColor;
		this.craftable = false;
		this.min = null;
		this.max = null;
	}

	public static Rarity of(ItemStack tool) {
		if(!ItemTags.isArmor(tool) && !ItemTags.isTool(tool))
			return null;

		Rarity currentRarity = getRarityFromLore(tool.getLore());

		// Calculate new rarity, if current rarity is craftable
		if(currentRarity == null || currentRarity.isCraftable()) {
			Integer val_material = getMaterialVal(tool);
			int val_enchants = getEnchantsVal(tool);
			int val_customEnchants = getCustomEnchantsVal(tool);

			if (val_material == null)
				return null;

			int sum = val_material + val_enchants + val_customEnchants;

			if (sum <= COMMON.getMin())
				return COMMON;

			if (sum >= LEGENDARY.getMax())
				return LEGENDARY;

			for (Rarity rarity : Rarity.values()) {
				int min = rarity.getMin();
				int max = rarity.getMax();

				if (sum >= min && sum <= max)
					return rarity;
			}
		}

		// Item is not craftable, return current rarity
		return currentRarity;
	}

	public String getTag(){
		return chatColor == null ? "" : chatColor + "[" + StringUtils.camelCase(this.name()) + "]";
	}


	private static Integer getMaterialVal(ItemStack itemStack){

		if(ItemTags.isArmor(itemStack))
			return ItemTags.getArmorMaterialVal(itemStack.getType());

		return ItemTags.getToolMaterialVal(itemStack.getType());
	}

	private static int getEnchantsVal(ItemStack itemStack){
		int result = 0;

		ItemMeta meta = itemStack.getItemMeta();
		if(meta.hasEnchants()) {
			Map<Enchantment, Integer> enchantMap = meta.getEnchants();
			Set<Enchantment> enchants = enchantMap.keySet();
			for (Enchantment enchant : enchants) {
				int level = enchantMap.get(enchant);
				int val = ItemTags.getEnchantVal(enchant, level);

				result += val;
			}
		}

		return result;
	}

	private static int getCustomEnchantsVal(ItemStack itemStack) {
		int result = 0;

		ItemMeta meta = itemStack.getItemMeta();
		if (meta.hasEnchants()) {
			Map<Enchantment, Integer> enchantMap = meta.getEnchants();
			Set<Enchantment> enchants = enchantMap.keySet();
			for (Enchantment enchant : enchants) {
				int val = ItemTags.getCustomEnchantVal(enchant);

				result += val;
			}
		}

		return result;
	}

	private static Rarity getRarityFromLore(List<String> lore) {
		if (lore == null || lore.size() == 0)
			return null;

		for (Rarity rarity : Rarity.values()) {
			String tag = rarity.getTag();
			for (String line : lore) {
				if(tag.equalsIgnoreCase(line)) {
					return rarity;
				}
			}
		}

		return null;
	}
}
