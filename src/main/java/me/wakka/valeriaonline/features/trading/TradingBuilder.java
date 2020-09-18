package me.wakka.valeriaonline.features.trading;

import lombok.Data;
import me.wakka.valeriaonline.features.trading.models.Trade;

import java.util.List;
import java.util.Map;

@Data
public class TradingBuilder {

	public Professions profession;
	public Map<Integer, List<Trade>> trades;
}
