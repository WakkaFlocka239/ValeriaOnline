package me.wakka.valeriaonline.Utils;

import com.google.common.base.Strings;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import me.wakka.valeriaonline.framework.exceptions.InvalidInputException;
import me.wakka.valeriaonline.framework.exceptions.PlayerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;

import java.util.*;
import java.util.stream.Collectors;

import static me.wakka.valeriaonline.Utils.StringUtils.camelCase;

// TODO - split up
public class Utils {

	public static void broadcast(String restartMessage) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			send(player, restartMessage);
		}
	}

	public static ItemStack addGlowing(ItemStack itemStack) {
		itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static void send(Player player, String message){
		player.sendMessage(StringUtils.colorize(message));
	}

	public static void giveItem(Player player, Material material) {
		giveItem(player, material, 1);
	}

	public static void giveItem(Player player, Material material, String nbt) {
		giveItem(player, material, 1, nbt);
	}

	public static void giveItem(Player player, Material material, int amount) {
		giveItem(player, material, amount, null);
	}

	public static void giveItem(Player player, Material material, int amount, String nbt) {
		if (material == Material.AIR)
			throw new InvalidInputException("Cannot spawn air");

		if (amount > 64) {
			for (int i = 0; i < (amount / 64); i++)
				giveItem(player, new ItemStack(material, 64), nbt);
			giveItem(player, new ItemStack(material, amount % 64), nbt);
		} else {
			giveItem(player, new ItemStack(material, amount), nbt);
		}
	}

	public static void giveItem(Player player, ItemStack item) {
		giveItems(player, Collections.singletonList(item));
	}

	public static void giveItem(Player player, ItemStack item, String nbt) {
		giveItems(player, Collections.singletonList(item), nbt);
	}

	public static void giveItems(Player player, Collection<ItemStack> items) {
		giveItems(player, items, null);
	}

	public static void giveItems(Player player, Collection<ItemStack> items, String nbt) {
		List<ItemStack> finalItems = new ArrayList<>(items);
		if (!Strings.isNullOrEmpty(nbt)) {
			finalItems.clear();
			NBTContainer nbtContainer = new NBTContainer(nbt);
			for (ItemStack item : new ArrayList<>(items)) {
				NBTItem nbtItem = new NBTItem(item);
				nbtItem.mergeCompound(nbtContainer);
				finalItems.add(nbtItem.getItem());
			}
		}

		for (ItemStack item : finalItems) {
			Map<Integer, ItemStack> excess = player.getInventory().addItem(item);
			if (!excess.isEmpty())
				excess.values().forEach(itemStack -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
		}
	}

	public static void runCommand(CommandSender sender, String commandNoSlash) {
		Bukkit.dispatchCommand(sender, commandNoSlash);
	}

	public static void runCommandAsOp(CommandSender sender, String commandNoSlash) {
		boolean deop = !sender.isOp();
		sender.setOp(true);
		runCommand(sender, commandNoSlash);
		if (deop)
			sender.setOp(false);
	}

	public static void runCommandAsConsole(String commandNoSlash) {
		runCommand(Bukkit.getConsoleSender(), commandNoSlash);
	}

	public static ItemStack getToolRequired(Player player) {
		ItemStack item = getTool(player);
		if (isNullOrAir(item))
			throw new InvalidInputException("You are not holding anything");
		return item;
	}

	public static ItemStack getTool(Player player) {
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if (!isNullOrAir(mainHand))
			return mainHand;
		else if (!isNullOrAir(offHand))
			return offHand;
		return null;
	}

	public static boolean isNullOrAir(Block block) {
		return block == null || block.getType().equals(Material.AIR);
	}

	public static boolean isNullOrAir(ItemStack itemStack) {
		return itemStack == null || itemStack.getType().equals(Material.AIR);
	}

	public static boolean isNullOrAir(Material material) {
		return material == null || material.equals(Material.AIR);
	}

	public static OfflinePlayer getPlayer(UUID uuid) {
		return Bukkit.getOfflinePlayer(uuid);
	}

	public static OfflinePlayer getPlayer(String partialName) {
		if (partialName == null || partialName.length() == 0)
			throw new InvalidInputException("No player name given");

		String original = partialName;
		partialName = partialName.toLowerCase().trim();

		if (partialName.length() == 36)
			return getPlayer(UUID.fromString(partialName));

		for (Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(partialName))
				return player;
		for (Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().contains((partialName)))
				return player;

		throw new PlayerNotFoundException(original);
	}

	public static boolean canSee(OfflinePlayer viewer, OfflinePlayer target) {
		if (!viewer.isOnline() || !target.isOnline()) return false;
		return (canSee(viewer.getPlayer(), target.getPlayer()));
	}

	public static boolean canSee(Player viewer, Player target) {
		return !isVanished(target) || viewer.hasPermission("pv.see");
	}

	public static boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished"))
			return (meta.asBoolean());
		return false;
	}

	public static ItemStack setDurability(ItemStack item, int percentage){
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable) {
			Damageable damageable = (Damageable) meta;
			double maxDurability = item.getType().getMaxDurability();
			double damage = (percentage/100.0) * maxDurability;
			damageable.setDamage((int) damage);

			item.setItemMeta((ItemMeta) damageable);
		}

		return item;
	}

	public static class EnumUtils {
		public static <T> T valueOf(Class<? extends T> clazz, String value) {
			T[] values = clazz.getEnumConstants();
			for (T enumValue : values)
				if (((Enum<?>) enumValue).name().equalsIgnoreCase(value))
					return enumValue;
			throw new IllegalArgumentException();
		}

		public static <T> T next(Class<? extends T> clazz, int ordinal) {
			T[] values = clazz.getEnumConstants();
			return values[Math.min(values.length - 1, ordinal + 1 % values.length)];
		}

		public static <T> T previous(Class<? extends T> clazz, int ordinal) {
			T[] values = clazz.getEnumConstants();
			return values[Math.max(0, ordinal - 1 % values.length)];
		}

		public static <T> T nextWithLoop(Class<? extends T> clazz, int ordinal) {
			T[] values = clazz.getEnumConstants();
			int next = ordinal + 1 % values.length;
			return next >= values.length ? values[0] : values[next];
		}

		public static <T> T previousWithLoop(Class<? extends T> clazz, int ordinal) {
			T[] values = clazz.getEnumConstants();
			int previous = ordinal - 1 % values.length;
			return previous < 0 ? values[values.length - 1] : values[previous];
		}

		public static String prettyName(String name) {
			if (!name.contains("_"))
				return camelCase(name);

			List<String> words = new ArrayList<>(Arrays.asList(name.split("_")));

			String first = words.get(0);
			String last = words.get(words.size() - 1);
			words.remove(0);
			words.remove(words.size() - 1);

			StringBuilder result = new StringBuilder(camelCase(first));
			for (String word : words) {
				String character = interpolate(word);
				if (character != null)
					result.append(character);
				else if (word.toLowerCase().matches("and|for|the|a|or|of|from|in|as"))
					result.append(" ").append(word.toLowerCase());
				else
					result.append(" ").append(camelCase(word));
			}

			String character = interpolate(last);
			if (character != null)
				result.append(character);
			else
				result.append(" ").append(last.charAt(0)).append(last.substring(1).toLowerCase());
			return result.toString().trim();
		}

		private static String interpolate(String word) {
			String character = null;
			switch (word.toLowerCase()) {
				case "period":
					character = ".";
					break;
				case "excl":
					character = "!";
					break;
				case "comma":
					character = ",";
					break;
			}
			return character;
		}
	}

	public interface IteratableEnum {
		int ordinal();

		String name();

		default <T extends Enum<?>> T next() {
			return (T) EnumUtils.next(this.getClass(), ordinal());
		}

		default <T extends Enum<?>> T previous() {
			return (T) EnumUtils.previous(this.getClass(), ordinal());
		}

		default <T extends Enum<?>> T nextWithLoop() {
			return (T) EnumUtils.nextWithLoop(this.getClass(), ordinal());
		}

		default <T extends Enum<?>> T previousWithLoop() {
			return (T) EnumUtils.previousWithLoop(this.getClass(), ordinal());
		}
	}

}
