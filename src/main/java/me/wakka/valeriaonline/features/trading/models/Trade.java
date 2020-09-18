package me.wakka.valeriaonline.features.trading.models;

import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@SerializableAs("Trade")
public class Trade implements ConfigurationSerializable {

	int id;
	int stock;
	ItemStack ingredient1;
	ItemStack ingredient2;
	ItemStack result;
	List<Type> types = Arrays.asList(Type.values());

	public Trade(int id) {
		this.id = id;
	}

	public Trade(Map<String, Object> map) {
		this.stock = (int) map.getOrDefault("stock", 1);
		this.ingredient1 = (ItemStack) map.getOrDefault("ingredient1", null);
		this.ingredient2 = (ItemStack) map.getOrDefault("ingredient2", null);
		this.result = (ItemStack) map.getOrDefault("result", null);
		this.types =  new ArrayList<Type>() {{ map.getOrDefault("types", Arrays.asList(Type.values())); }};
	}

	@Override
	public Map<String, Object> serialize() {
		return new LinkedHashMap<String, Object>() {{
			put("stock", stock);
			put("ingredient1", ingredient1);
			put("ingredient2", ingredient2);
			put("result", result);
			put("types", types);
		}};
	}

}
