package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.utils.RandomUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Villagers implements Listener {
	private static final List<UUID> cancelInteraction = new ArrayList<>();

	@EventHandler
	public void onProfessionChange(VillagerCareerChangeEvent event) {
		Villager villager = event.getEntity();
		Villager.Profession profession = event.getProfession();
		if (profession.equals(Villager.Profession.NONE))
			return;

		List<Trade> trades = Trading.getTrades(Profession.valueOf(profession.name()), villager.getVillagerLevel());
		if (trades.size() == 0)
			return;

		Trade trade = null;

		for (int i = 0; i < 50; i++) {
			trade = RandomUtils.randomElement(trades);
			if (!Utils.isNullOrAir(trade.getResult()))
				break;
		}

		if (Utils.isNullOrAir(trade.getResult()))
			return;

		cancelInteraction.add(villager.getUniqueId());

		MerchantRecipe recipe = new MerchantRecipe(trade.getResult(), trade.getStock());
		recipe.setExperienceReward(true); // player
		recipe.setVillagerExperience(1); // villager
		recipe.addIngredient(trade.getIngredient1());
		if (trade.getIngredient2() != null)
			recipe.addIngredient(trade.getIngredient2());

		Tasks.wait(1, () -> {
			villager.setRecipes(Collections.singletonList(recipe));
			cancelInteraction.remove(villager.getUniqueId());
		});

	}

	@EventHandler
	public void onWanderingTraderSpawn(EntitySpawnEvent event) {
		if (!event.getEntityType().equals(EntityType.WANDERING_TRADER))
			return;

		List<Trade> trades = Trading.getTrades(Profession.WANDERING_TRADER, 1);
		Trade trade = null;

		for (int i = 0; i < 50; i++) {
			trade = RandomUtils.randomElement(trades);
			if (!Utils.isNullOrAir(trade.getResult()))
				break;
		}

		if (Utils.isNullOrAir(trade.getResult()))
			return;

		WanderingTrader trader = (WanderingTrader) event.getEntity();
		cancelInteraction.add(trader.getUniqueId());

		MerchantRecipe recipe = new MerchantRecipe(trade.getResult(), trade.getStock());
		recipe.setExperienceReward(true); // player
		recipe.setVillagerExperience(1); // villager
		recipe.addIngredient(trade.getIngredient1());
		if (trade.getIngredient2() != null)
			recipe.addIngredient(trade.getIngredient2());

		Tasks.wait(1, () -> {
			trader.setRecipes(Collections.singletonList(recipe));
			cancelInteraction.remove(trader.getUniqueId());
		});
	}

	@EventHandler
	public void onClickVillager(PlayerInteractEntityEvent event) {
		EntityType type = event.getRightClicked().getType();
		if (!type.equals(EntityType.VILLAGER) && type.equals(EntityType.WANDERING_TRADER))
			return;

		if (cancelInteraction.contains(event.getRightClicked().getUniqueId()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onVillagerBreed(EntityBreedEvent event) {
		if (!event.getEntity().getType().equals(EntityType.VILLAGER))
			return;

		if (RandomUtils.chanceOf(50)) {
			event.setCancelled(true);
		}
	}

	//		Villager villager = (Villager) event.getRightClicked();
//		Villager.Profession profession = villager.getProfession();
//		int level = villager.getVillagerLevel();
//		int exp = villager.getVillagerExperience();
//		Villager.Type type = villager.getVillagerType();
//
//		Utils.send(event.getPlayer(), "");
//		Utils.send(event.getPlayer(), "Profession: " + profession);
//		Utils.send(event.getPlayer(), "Level: " + level);
//		Utils.send(event.getPlayer(), "Experience: " + exp);
//		Utils.send(event.getPlayer(), "Type: " + type);
//
//		NBTEntity nbtE = new NBTEntity(villager);
//		String rep = "none";
//		try {
//			rep = nbtE.getCompoundList("Gossips").get(0).getString("Type");
//		} catch (Exception ignored) { }
//
//		Utils.send(event.getPlayer(), "Reputation: " + rep);
}
