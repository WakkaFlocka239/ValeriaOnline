package me.wakka.valeriaonline.features.itemtags;

import com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent;
import com.gmail.nossr50.events.skills.repair.McMMOPlayerRepairCheckEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.listeners.BalanceElytra;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static me.wakka.valeriaonline.features.itemtags.ItemTagUtils.updateItem;

public class Listeners implements Listener {

	public Listeners(){
		ValeriaOnline.registerListener(this);
	}

	@EventHandler
	public void onEnchantItem(EnchantItemEvent event){
		if (!(event.getView().getPlayer() instanceof Player)) return;

		ItemStack result = event.getItem();
		if(Utils.isNullOrAir(result)) return;

		ItemStack updated = updateItem(result);

		result.setItemMeta(updated.getItemMeta());
		Tasks.sync(() -> result.setItemMeta(updated.getItemMeta()));
	}

	@EventHandler
	public void onCraftItem(PrepareItemCraftEvent event){
		if (!(event.getView().getPlayer() instanceof Player)) return;

		ItemStack result = event.getInventory().getResult();
		if(Utils.isNullOrAir(result)) return;

		ItemStack updated = updateItem(result);

		event.getInventory().setResult(updated);
		Tasks.sync(() -> event.getInventory().setResult(updated));
	}

	@EventHandler
	public void onAnvilCraftItem(PrepareAnvilEvent event) {
		if(BalanceElytra.handledEvent(event))
			return;

		if (!(event.getView().getPlayer() instanceof Player)) return;

		ItemStack result = event.getInventory().getResult();
		if(Utils.isNullOrAir(result)) return;

		ItemStack updated = updateItem(result);

		event.getInventory().setResult(updated);
		Tasks.sync(() -> event.getInventory().setResult(updated));
	}

	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event){
		ItemStack result = event.getItem();
		if(Utils.isNullOrAir(result)) return;

		ItemStack updated = updateItem(result);

		event.getItem().setItemMeta(updated.getItemMeta());
	}

	@EventHandler
	public void onGrindstoneCraftItem(PrepareGrindstoneEvent event){
		if (!(event.getView().getPlayer() instanceof Player)) return;

		ItemStack result = event.getInventory().getResult();
		if(Utils.isNullOrAir(result)) return;

		ItemStack updated = updateItem(result);

		event.getInventory().setResult(updated);
		Tasks.sync(() -> event.getInventory().setResult(updated));
	}

	@EventHandler
	public void onSmithingTableCraftItem(PrepareSmithingEvent event){
		if (!(event.getView().getPlayer() instanceof Player)) return;

		ItemStack result = event.getResult();
		if(Utils.isNullOrAir(result)) return;

		ItemStack updated = updateItem(result);

		event.setResult(updated);
		Tasks.sync(() -> event.setResult(updated));
	}

	@EventHandler
	public void onBreakItemFrame(EntityDamageByEntityEvent event){
		if(!event.getEntityType().equals(EntityType.ITEM_FRAME))
			return;

		ItemFrame itemFrame = (ItemFrame) event.getEntity();
		ItemStack item = itemFrame.getItem();
		if(Utils.isNullOrAir(item)) return;

		ItemStack updated = updateItem(item);

		itemFrame.setItem(updated);
	}

	@EventHandler
	public void onItemFrameBreak(HangingBreakEvent event){
		if (!(event.getEntity().getType().equals(EntityType.ITEM_FRAME)))
			return;

		if (event.isCancelled())
			return;

		ItemFrame itemFrame = (ItemFrame) event.getEntity();
		ItemStack item = itemFrame.getItem();
		if (Utils.isNullOrAir(item)) return;

		ItemStack updated = updateItem(item);

		itemFrame.setItem(updated);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		int ndx = 0;
		for (ItemStack drop : new ArrayList<>(event.getDrops())) {
			ItemStack updated = updateItem(drop);
			event.getDrops().set(ndx, updated);
			++ndx;
		}
	}

	@EventHandler
	public void onMythicMMobEntityDeath(MythicMobDeathEvent event) {
		int ndx = 0;
		for (ItemStack drop : new ArrayList<>(event.getDrops())) {
			ItemStack updated = updateItem(drop);
			event.getDrops().set(ndx, updated);
			++ndx;
		}
	}

	@EventHandler
	public void onFishingLoot(PlayerFishEvent event) {
		if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))
			return;

		Entity caught = event.getCaught();
		if (!(caught instanceof Item)) return;

		Item item = (Item) caught;
		ItemStack itemStack = item.getItemStack();
		updateItem(itemStack);
	}

	@EventHandler
	public void onMcMMORepair(McMMOPlayerRepairCheckEvent event) {
		ItemStack repaired = event.getRepairedObject().clone();
		updateItem(repaired);
		event.getRepairedObject().setItemMeta(repaired.getItemMeta());
	}

}


