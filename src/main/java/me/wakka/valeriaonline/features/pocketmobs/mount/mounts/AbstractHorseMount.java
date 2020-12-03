package me.wakka.valeriaonline.features.pocketmobs.mount.mounts;

import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.features.pocketmobs.mount.MountType;
import me.wakka.valeriaonline.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@NoArgsConstructor
public class AbstractHorseMount extends Mount {
	public static final ItemStack saddleItem = new ItemBuilder(Material.SADDLE).build();

	AbstractHorse abstractHorse;
	AbstractHorseInventory inventory;

	public AbstractHorseMount(Player owner, AbstractHorse abstractHorse, boolean godmode) {
		String name = !Strings.isNullOrEmpty(entity.getCustomName()) ? entity.getCustomName() : "";
		MountType type = MountType.valueOf(entity.getType().name());

		new AbstractHorseMount(owner, name, abstractHorse.getUniqueId(), type, godmode);
	}

	public AbstractHorseMount(Player owner, String name, UUID uuid, MountType type, boolean godmode) {
		super(owner, name, uuid, type, godmode);
	}

	@Override
	public boolean spawn() {
		if (!super.spawn())
			return false;

		abstractHorse = (AbstractHorse) entity;
		if (abstractHorse == null)
			return false;

		inventory.setSaddle(saddleItem);

		return true;
	}
}
