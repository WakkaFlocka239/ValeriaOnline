package me.wakka.valeriaonline.models.freeze;

import me.wakka.valeriaonline.framework.persistence.annotations.PlayerClass;
import me.wakka.valeriaonline.models.MongoService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@PlayerClass(Freeze.class)
public class FreezeService extends MongoService {
	private final static Map<UUID, Freeze> cache = new HashMap<>();

	public Map<UUID, Freeze> getCache() {
		return cache;
	}

}
