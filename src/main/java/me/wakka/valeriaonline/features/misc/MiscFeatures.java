package me.wakka.valeriaonline.features.misc;

import me.wakka.valeriaonline.framework.features.Feature;

public class MiscFeatures extends Feature {
	@Override
	public void startup() {

		new KingdomCompass();
		new ArmorStandStalker();
		new AmbientSounds();
	}
}
