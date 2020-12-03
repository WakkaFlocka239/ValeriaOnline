package me.wakka.valeriaonline.features.pocketmobs;

import me.wakka.valeriaonline.features.pocketmobs.mount.Mounts;
import me.wakka.valeriaonline.framework.features.Feature;

public class PocketMobs extends Feature {

	@Override
	public void startup() {
		new Mounts();
	}
}
