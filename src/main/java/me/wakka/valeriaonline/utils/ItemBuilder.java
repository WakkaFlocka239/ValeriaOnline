package me.wakka.valeriaonline.utils;

import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;

public class ItemBuilder implements Cloneable {
	private final ItemStack itemStack;
	private final ItemMeta itemMeta;
	private int damage = 0;
	private List<String> lore = new ArrayList<>();
	private boolean doLoreize = true;

	public ItemBuilder(Material material) {
		this(new ItemStack(material));
	}

	public ItemBuilder(Material material, int amount) {
		this(new ItemStack(material, amount));
	}

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack.clone();
		this.itemMeta = itemStack.getItemMeta();
	}

	public ItemBuilder material(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder amount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder color(ColorType colorType) {
		itemStack.setType(colorType.switchColor(itemStack.getType()));
		return this;
	}

//	@Deprecated
//	public ItemBuilder durability(int durability) {
//		return durability(Integer.valueOf(durability).shortValue());
//	}

//	@Deprecated
//	public ItemBuilder durability(short durability) {
//		itemStack.setDurability(durability);
//		return this;
//	}

	public ItemBuilder damage(int damage) {
		this.damage = damage;
		return this;
	}

	public ItemBuilder name(String displayName) {
		itemMeta.setDisplayName(colorize(displayName));
		return this;
	}

	public ItemBuilder lore(String... lore) {
		return lore(Arrays.asList(lore));
	}

	public ItemBuilder lore(List<String> lore) {
		this.lore.addAll(lore);
		return this;
	}

	public ItemBuilder loreize(boolean doLoreize) {
		this.doLoreize = doLoreize;
		return this;
	}

	public ItemBuilder enchant(Enchantment enchantment) {
		return enchant(enchantment, 1);
	}

	public ItemBuilder enchant(Enchantment enchantment, int level) {
		return enchant(enchantment, level, true);
	}

	public ItemBuilder enchantMax(Enchantment enchantment) {
		return enchant(enchantment, enchantment.getMaxLevel(), true);
	}

	public ItemBuilder enchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
		if (itemStack.getType() == Material.ENCHANTED_BOOK) {
			((EnchantmentStorageMeta) itemMeta).addStoredEnchant(enchantment, level, ignoreLevelRestriction);
		} else {
			itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
		}

		return this;
	}

	public ItemBuilder glow() {
		enchant(Enchantment.ARROW_INFINITE);
		itemFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	public ItemBuilder unbreakable() {
		itemMeta.setUnbreakable(true);
		return this;
	}

	public ItemBuilder itemFlags(ItemFlag... flags) {
		itemMeta.addItemFlags(flags);
		return this;
	}

	/**
	 * Custom meta types
	 */

	public ItemBuilder armorColor(Color color) {
		((LeatherArmorMeta) itemMeta).setColor(color);
		return this;
	}

	// Potions

	public ItemBuilder potionType(PotionType potionType) {
		return potionType(potionType, false, false);
	}

	public ItemBuilder potionType(PotionType potionType, boolean extended, boolean upgraded) {
		((PotionMeta) itemMeta).setBasePotionData(new PotionData(potionType, extended, upgraded));
		return this;
	}

	public ItemBuilder potionEffect(PotionEffectType potionEffectType) {
		return potionEffect(potionEffectType, 1, 1);
	}

	public ItemBuilder potionEffect(PotionEffectType potionEffectType, int seconds) {
		return potionEffect(potionEffectType, seconds, 1);
	}

	public ItemBuilder potionEffect(PotionEffectType potionEffectType, int seconds, int amplifier) {
		return potionEffect(new PotionEffect(potionEffectType, seconds * 20, amplifier - 1));
	}

	public ItemBuilder potionEffect(PotionEffect potionEffect) {
		((PotionMeta) itemMeta).addCustomEffect(potionEffect, true);
		return this;
	}

	public ItemBuilder potionEffectColor(Color color) {
		((PotionMeta) itemMeta).setColor(color);
		return this;
	}

	// Fireworks

	public ItemBuilder fireworkPower(int power) {
		((FireworkMeta) itemMeta).setPower(power);
		return this;
	}

	public ItemBuilder fireworkEffect(FireworkEffect... effect) {
		((FireworkMeta) itemMeta).addEffects(effect);
		return this;
	}

	// Skulls

	public ItemBuilder skullOwner(OfflinePlayer offlinePlayer) {
		((SkullMeta) itemMeta).setOwningPlayer(offlinePlayer);
		return this;
	}

	@Deprecated
	public ItemBuilder skullOwner(String name) {
		((SkullMeta) itemMeta).setOwner(name);
		return this;
	}

	// Banners

	public ItemBuilder pattern(DyeColor color, PatternType pattern) {
		return pattern(new Pattern(color, pattern));
	}

	public ItemBuilder pattern(Pattern pattern) {
		BannerMeta bannerMeta = (BannerMeta) itemMeta;
		bannerMeta.addPattern(pattern);
		return this;
	}

	public ItemBuilder symbolBanner(char character, DyeColor patternDye) {
		return symbolBanner(SymbolBanner.Symbol.of(character), patternDye);
	}

	public ItemBuilder symbolBanner(SymbolBanner.Symbol symbol, DyeColor patternDye) {
		if (symbol == null)
			return this;
		return symbol.get(this, ColorType.of(itemStack.getType()).getDyeColor(), patternDye);
	}

	// Shulker Boxes

	public ItemBuilder shulkerBox(ItemBuilder... builders) {
		for (ItemBuilder builder : builders)
			shulkerBox(builder.build());
		return this;
	}

	public ItemBuilder shulkerBox(ItemStack... items) {
		BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
		ShulkerBox box = (ShulkerBox) blockStateMeta.getBlockState();
		for (ItemStack item : items)
			box.getInventory().addItem(item);
		blockStateMeta.setBlockState(box);
		return this;
	}

	/**
	 * Building
	 */

	public ItemStack build() {
		ItemStack result = itemStack.clone();
		buildLore();
		setDamage();
		result.setItemMeta(itemMeta.clone());
		return result;
	}

	private void setDamage() {
		((Damageable) itemMeta).setDamage(damage);
	}

	public void buildLore() {
		List<String> colorized = new ArrayList<>();
		for (String line : lore)
			if (doLoreize)
				colorized.addAll(Arrays.asList(StringUtils.loreize(colorize(line)).split("\\|\\|")));
			else
				colorized.addAll(Arrays.asList(colorize(line).split("\\|\\|")));
		itemMeta.setLore(colorized);
	}

	public ItemBuilder clone() {
		itemStack.setItemMeta(itemMeta);
		ItemBuilder builder = new ItemBuilder(itemStack.clone());
		builder.lore(lore);
		builder.loreize(doLoreize);
		return builder;
	}

	/**
	 * Static helpers
	 */

	public static ItemStack setName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		if (name == null)
			meta.setDisplayName(null);
		else
			meta.setDisplayName(colorize("&f" + name));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack addItemFlags(ItemStack item, ItemFlag... flags) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(flags);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack addLore(ItemStack item, String... lore) {
		return addLore(item, Arrays.asList(lore));
	}

	public static ItemStack addLore(ItemStack item, List<String> lore) {
		lore = lore.stream().map(StringUtils::colorize).collect(Collectors.toList());
		ItemMeta meta = item.getItemMeta();
		List<String> existing = meta.getLore();
		if (existing == null) existing = new ArrayList<>();
		existing.addAll(lore);
		meta.setLore(existing);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setLoreLine(ItemStack item, int line, String text) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null)
			lore = new ArrayList<>();
		while (lore.size() < line)
			lore.add("");

		lore.set(line - 1, colorize(text));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack removeLoreLine(ItemStack item, int line) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) throw new InvalidInputException("Item does not have lore");
		if (line - 1 > lore.size()) throw new InvalidInputException("Line " + line + " does not exist");

		lore.remove(line - 1);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
