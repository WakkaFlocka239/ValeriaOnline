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
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Villagers implements Listener {
	private static final List<UUID> cancelInteraction = new ArrayList<>();

	@EventHandler
	public void onAcquireTrade(VillagerAcquireTradeEvent event) {
		if (!event.getEntity().getType().equals(EntityType.VILLAGER))
			return;

		Villager villager = (Villager) event.getEntity();

		cancelInteraction.add(villager.getUniqueId());
		MerchantRecipe newRecipe = getRecipe(villager, 0);

		if (newRecipe != null)
			event.setRecipe(newRecipe);

		cancelInteraction.remove(villager.getUniqueId());
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

		if (RandomUtils.chanceOf(50))
			event.setCancelled(true);
	}

	private MerchantRecipe getRecipe(Villager villager, int tries) {
		String profession = villager.getProfession().name();

		int level = villager.getVillagerLevel();
		List<Trade> trades = Trading.getTrades(Profession.valueOf(profession), level);

		int ndx = 0;
		for (Trade trade : new ArrayList<>(trades)) {
			if (Utils.isNullOrAir(trade.getResult()))
				trades.remove(ndx);
			++ndx;
		}

		if (trades.size() < 2)
			return null;

		Trade trade = RandomUtils.randomElement(trades);

		MerchantRecipe recipe = new MerchantRecipe(trade.getResult(), trade.getStock());
		recipe.setExperienceReward(true);
		recipe.setVillagerExperience(1);
		recipe.addIngredient(trade.getIngredient1());

		if (trade.getIngredient2() != null)
			recipe.addIngredient(trade.getIngredient2());

		if (containsRecipe(recipe, villager.getRecipes())) {
			if (tries <= trades.size()) {
				return getRecipe(villager, ++tries);
			} else {
				return null;
			}
		}

		return recipe;
	}

	private boolean containsRecipe(MerchantRecipe recipe, List<MerchantRecipe> recipes) {
		ItemStack result = recipe.getResult();
		ItemStack ingredient1 = recipe.getIngredients().get(0);
		ItemStack ingredient2 = null;
		if (recipe.getIngredients().size() == 2)
			ingredient2 = recipe.getIngredients().get(1);

		for (MerchantRecipe villagerRecipe : recipes) {
			ItemStack _result = villagerRecipe.getResult();
			ItemStack _ingredient1 = villagerRecipe.getIngredients().get(0);
			ItemStack _ingredient2 = null;
			if (villagerRecipe.getIngredients().size() == 2)
				_ingredient2 = villagerRecipe.getIngredients().get(1);

			if (result.equals(_result) && ingredient1.equals(_ingredient1)) {
				if (ingredient2 == null && _ingredient2 == null)
					return true;
				if (ingredient2 != null && ingredient2.equals(_ingredient2))
					return true;
			}
		}

		return false;
	}
}
