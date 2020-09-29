package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.features.trading.Trading;
import me.wakka.valeriaonline.features.trading.models.Profession;
import me.wakka.valeriaonline.features.trading.models.Trade;
import me.wakka.valeriaonline.features.trading.models.Type;
import me.wakka.valeriaonline.utils.RandomUtils;
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
import java.util.List;
import java.util.UUID;

public class Villagers implements Listener {
	private static final List<UUID> cancelInteraction = new ArrayList<>();
	private static final int[][] tradeExp = {{1, 2}, {4, 6}, {9, 11}, {14, 18}, {22, 26}};

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

		WanderingTrader trader = (WanderingTrader) event.getEntity();
		cancelInteraction.add(trader.getUniqueId());
		List<MerchantRecipe> newRecipes = getWanderingTraderRecipes();

		if (newRecipes != null && newRecipes.size() > 0)
			trader.setRecipes(newRecipes);

		cancelInteraction.remove(trader.getUniqueId());
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
		Type type = Type.valueOf(String.valueOf(villager.getVillagerType()));

		List<Trade> trades = Trading.getTrades(Profession.valueOf(profession), level);

		int ndx = 0;
		for (Trade trade : new ArrayList<>(trades)) {
			if (Utils.isNullOrAir(trade.getResult()))
				trades.remove(ndx);

			if (!trade.getTypes().contains(type)) {
				if (!trade.getTypes().contains(Type.ALL))
					trades.remove(ndx);
			}
			++ndx;
		}

		if (trades.size() < 2)
			return null;

		Trade trade = RandomUtils.randomElement(trades);

		MerchantRecipe recipe = new MerchantRecipe(trade.getResult(), trade.getStock());
		recipe.setExperienceReward(true);

		int exp = RandomUtils.randomInt(tradeExp[level - 1][0], tradeExp[level - 1][1]);
		recipe.setVillagerExperience(exp);
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

	private List<MerchantRecipe> getWanderingTraderRecipes() {
		List<Trade> trades = Trading.getTrades(Profession.WANDERING_TRADER, 1);

		int ndx = 0;
		for (Trade trade : new ArrayList<>(trades)) {
			if (Utils.isNullOrAir(trade.getResult()))
				trades.remove(ndx);
			++ndx;
		}

		if (trades.size() < 4)
			return null;

		List<MerchantRecipe> selected = new ArrayList<>();
		int tradeCount = RandomUtils.randomInt(4, trades.size());
		int safety = 0;
		for (int i = 0; i < tradeCount; i++) {
			Trade trade = RandomUtils.randomElement(trades);

			MerchantRecipe recipe = new MerchantRecipe(trade.getResult(), trade.getStock());
			recipe.setExperienceReward(true);
			recipe.setVillagerExperience(1);
			recipe.addIngredient(trade.getIngredient1());
			if (trade.getIngredient2() != null)
				recipe.addIngredient(trade.getIngredient2());

			if (containsRecipe(recipe, selected)) {
				--i;
			} else {
				selected.add(recipe);
			}

			if (++safety > 50)
				break;
		}

		return selected;
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
