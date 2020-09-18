package me.wakka.valeriaonline.features.trading.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.LinkedHashMap;
import java.util.Map;


@SerializableAs("Trade")
@Data
public class Trade implements ConfigurationSerializable {

	int stock;
	@NonNull
	ItemStack ingredient1;
	ItemStack ingredient2;
	ItemStack result;

	public Trade(Map<String, Object> map) {
		this.stock = (int) map.getOrDefault("stock", 1);
		this.ingredient1 = (ItemStack) map.getOrDefault("ingredient1", null);
		this.ingredient2 = (ItemStack) map.getOrDefault("ingredient2", null);
		this.result = (ItemStack) map.getOrDefault("result", null);
	}

	@Override
	public Map<String, Object> serialize() {
		return new LinkedHashMap<String, Object>() {{
			put("stock", stock);
			put("ingredient1", ingredient1);
			put("ingredient2", ingredient2);
			put("result", result);
		}};
	}

}
