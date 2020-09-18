package me.wakka.valeriaonline.features.trading.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TradingBuilder {

	public Profession profession;
	public Map<Integer, List<Trade>> trades;

}
