package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.Utils.Tasks;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class BalanceElytra implements Listener {

	@EventHandler
	public void onAnvilEnchantElytra(PrepareAnvilEvent event) {
		handledEvent(event);
	}

	public static boolean handledEvent(PrepareAnvilEvent event){
		if(cancelEvent(event)) {
			Player player = (Player) event.getView().getPlayer();

			if (event.getResult() != null) {
				event.getResult().setType(Material.AIR);
				Tasks.sync(() -> event.getResult().setType(Material.AIR));
			}
			player.updateInventory();

			return true;
		}

		return false;
	}

	private static boolean cancelEvent(PrepareAnvilEvent event){
		ItemStack first = event.getInventory().getFirstItem();
		ItemStack second = event.getInventory().getSecondItem();
		ItemStack book = null;

		if (first == null || second == null)
			return false;

		if (first.getType().equals(Material.ELYTRA)) {
			if(!second.getType().equals(Material.ENCHANTED_BOOK))
				return false;
			book = second.clone();
		}else if (second.getType().equals(Material.ELYTRA)) {
			if(!first.getType().equals(Material.ENCHANTED_BOOK))
				return false;
			book = first.clone();
		}

		if(book == null)
			return false;


		EnchantmentStorageMeta enchantedBook = (EnchantmentStorageMeta) book.getItemMeta();
		for (Enchantment enchantment : enchantedBook.getStoredEnchants().keySet()) {
			if(enchantment.equals(Enchantment.MENDING))
				return true;
		}

		return false;
	}


}
