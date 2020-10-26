package me.wakka.valeriaonline.features.events;

import me.wakka.valeriaonline.features.events.TreasureChests.TreasureChests;
import me.wakka.valeriaonline.framework.annotations.Environments;
import me.wakka.valeriaonline.framework.features.Feature;
import me.wakka.valeriaonline.utils.Env;

@Environments(Env.PROD)
public class Events extends Feature {

	@Override
	public void startup() {
		new TreasureChests();
	}

}
