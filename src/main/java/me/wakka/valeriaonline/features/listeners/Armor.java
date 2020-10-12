package me.wakka.valeriaonline.features.listeners;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.items.ItemManager;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class Armor implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().equals(Utils.wakka()))
			return;

		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		ItemStack item = event.getItem();
		if (Utils.isNullOrAir(item))
			return;

		if (!isMythicItem(item))
			return;
	}

	private boolean isMythicItem(ItemStack item) {
		MythicItem mythicItem;
		try {
			mythicItem = getMythicItem(item);
			if (mythicItem == null)
				return false;
		} catch (Exception ignored) {
			return false;
		}

		Utils.wakka("Mythic Item: " + mythicItem.getInternalName());
		return true;
	}

	private MythicItem getMythicItem(ItemStack item) {
		ItemManager itemManager = ValeriaOnline.getMythicMobs().getItemManager();
		ItemStack itemStack = item.clone();

		if (itemStack.getAmount() > 1)
			itemStack.setAmount(1);

		NBTItem nbtItem = new NBTItem(itemStack);
		String nbtString = nbtItem.toString();
		if (nbtString.contains("MYTHIC_TYPE")) {
			String mythicType = nbtItem.getString("MYTHIC_TYPE");
			Optional<MythicItem> mythicItem = itemManager.getItem(mythicType);
			return mythicItem.orElse(null);
		}

		for (MythicItem mythicItem : itemManager.getItems()) {
			String mythicName = StringUtils.stripColor(mythicItem.getDisplayName());
			String itemName = StringUtils.stripColor(item.getItemMeta().getDisplayName());
			if (mythicName == null)
				continue;

			if (mythicName.equalsIgnoreCase(itemName))
				return mythicItem;
		}

		return null;

	}
}
