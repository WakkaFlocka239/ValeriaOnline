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
import java.util.stream.Collectors;

@Data
@SerializableAs("Trade")
public class Trade implements ConfigurationSerializable {

	int id;
	int stock;
	ItemStack ingredient1;
	ItemStack ingredient2;
	ItemStack result;
	List<Type> types = new ArrayList<Type>() {{ addAll(Arrays.stream(Type.values()).filter(type -> type != Type.ALL).collect(Collectors.toList())); }};

	public Trade(int id) {
		this.id = id;
	}

	public Trade(Map<String, Object> map) {
		this.stock = (int) map.getOrDefault("stock", 1);
		this.ingredient1 = (ItemStack) map.getOrDefault("ingredient1", null);
		this.ingredient2 = (ItemStack) map.getOrDefault("ingredient2", null);
		this.result = (ItemStack) map.getOrDefault("result", null);
		List<Object> list =  (ArrayList<Object>) map.getOrDefault("types", new ArrayList<Type>() {{ addAll(Arrays.stream(Type.values()).filter(type -> type != Type.ALL).collect(Collectors.toList())); }});
		List<Type> types = new ArrayList<>();
		for (Object object : list) {
			if (object instanceof String)
				types.add(Type.valueOf((String) object));
			else
				types.add((Type) object);
		}
		this.types = types;
	}

	@Override
	public Map<String, Object> serialize() {
		return new LinkedHashMap<String, Object>() {{
			put("stock", stock);
			put("ingredient1", ingredient1);
			put("ingredient2", ingredient2);
			put("result", result);
			List<String> list = new ArrayList<>();
			for (Type type : types)
				list.add(type.name());
			put("types", list);
		}};
	}

}
