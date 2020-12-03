package me.wakka.valeriaonline.features.pocketmobs.mount.mounts;

import me.wakka.valeriaonline.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

public class LlamaMount extends Mount {
	public static final ItemStack white_carpet = new ItemBuilder(Material.WHITE_CARPET).build();
	public static final ItemStack orange_carpet = new ItemBuilder(Material.ORANGE_CARPET).build();
	public static final ItemStack magenta_carpet = new ItemBuilder(Material.MAGENTA_CARPET).build();
	public static final ItemStack lightblue_carpet = new ItemBuilder(Material.LIGHT_BLUE_BANNER).build();
	public static final ItemStack yellow_carpet = new ItemBuilder(Material.YELLOW_CARPET).build();
	public static final ItemStack lime_carpet = new ItemBuilder(Material.LIME_CARPET).build();
	public static final ItemStack pink_carpet = new ItemBuilder(Material.PINK_CARPET).build();
	public static final ItemStack gray_carpet = new ItemBuilder(Material.GRAY_CARPET).build();
	public static final ItemStack lightgray_carpet = new ItemBuilder(Material.LIGHT_GRAY_CARPET).build();
	public static final ItemStack cyan_carpet = new ItemBuilder(Material.CYAN_CARPET).build();
	public static final ItemStack purple_carpet = new ItemBuilder(Material.PURPLE_CARPET).build();
	public static final ItemStack blue_carpet = new ItemBuilder(Material.BLUE_CARPET).build();
	public static final ItemStack brown_carpet = new ItemBuilder(Material.BROWN_CARPET).build();
	public static final ItemStack green_carpet = new ItemBuilder(Material.GREEN_CARPET).build();
	public static final ItemStack red_carpet = new ItemBuilder(Material.RED_CARPET).build();
	public static final ItemStack black_carpet = new ItemBuilder(Material.BLACK_CARPET).build();

	Llama llama;

	Llama.Color color;
	LlamaInventory inventory = null;
	ItemStack carpet;
}
