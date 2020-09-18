package me.wakka.valeriaonline.features.trading.models;

import org.bukkit.configuration.serialization.SerializableAs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("GroupTrade")
public class GroupTrade extends Trade {

	public List<Trade> trades;

	public GroupTrade(Map<String, Object> map) {
		super(map);
		this.trades = (List<Trade>) map.getOrDefault("trades", new ArrayList<>());
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) super.serialize();
		map.put("trades", trades);
		return map;
	}
}
