package me.wakka.valeriaonline.features.pocketmobs.mount.mounts;

import com.google.common.base.Strings;
import me.wakka.valeriaonline.features.pocketmobs.mount.MountType;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class HorseMount extends AbstractHorseMount {

	public static final ItemStack leather_armor = new ItemBuilder(Material.LEATHER_HORSE_ARMOR).build();
	public static final ItemStack gold_armor = new ItemBuilder(Material.GOLDEN_HORSE_ARMOR).build();
	public static final ItemStack iron_armor = new ItemBuilder(Material.IRON_HORSE_ARMOR).build();
	public static final ItemStack diamond_armor = new ItemBuilder(Material.DIAMOND_HORSE_ARMOR).build();

	Horse horse;
	Horse.Color color;
	Horse.Style style;
	ItemStack armor;
	double jumpStrength = 0.847552919762898; // 4 blocks

	public HorseMount(Player owner, Horse horse, boolean godmode, Horse.Color color, Horse.Style style, ItemStack armor) {
		String name = !Strings.isNullOrEmpty(entity.getCustomName()) ? entity.getCustomName() : "";
		MountType type = MountType.valueOf(entity.getType().name());

		new HorseMount(owner, name, horse.getUniqueId(), type, godmode, color, style, armor);
	}

	public HorseMount(Player owner, String name, UUID uuid, MountType type, boolean godmode, Horse.Color color, Horse.Style style, ItemStack armor) {
		super(owner, name, uuid, type, godmode);

		this.color = color;
		this.style = style;
		this.armor = armor;
	}

	@Override
	public boolean spawn() {
		if (!super.spawn())
			return false;

		horse = (Horse) entity;
		if (horse == null) {
			return false;
		}

		// Player customizable
		horse.setColor(color);
		horse.setStyle(style);
		//
		horse.setJumpStrength(jumpStrength);
		horse.setTamed(true);
		horse.setOwner(getOwningPlayer());
		horse.setBreed(false);
		horse.setCustomName(name); // Mount
		horse.setInvulnerable(godmode); // Mount

		HorseInventory inv = horse.getInventory();
		inv.setContents(inventory.getContents());

		if (!Utils.isNullOrAir(armor))
			inv.setArmor(armor);

		return true;
	}

	@Override
	public void saveMount() {
		super.saveMount();

		this.color = horse.getColor();
		this.style = horse.getStyle();
		this.name = horse.getName();
		this.inventory = horse.getInventory();
	}
}
