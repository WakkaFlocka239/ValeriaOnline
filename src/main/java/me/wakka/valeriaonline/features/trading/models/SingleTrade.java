package me.wakka.valeriaonline.features.trading.models;

import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("SingleTrade")
public class SingleTrade extends Trade {

	public SingleTrade(Map<String, Object> map) {
		super(map);
	}

	@Override
	public Map<String, Object> serialize() {
		return super.serialize();
	}
}
