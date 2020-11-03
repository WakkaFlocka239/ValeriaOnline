package me.wakka.valeriaonline.models.compass;

import me.wakka.valeriaonline.framework.persistence.annotations.PlayerClass;
import me.wakka.valeriaonline.models.MongoService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@PlayerClass(Compass.class)
public class CompassService extends MongoService {
	private final static Map<UUID, Compass> cache = new HashMap<>();

	public Map<UUID, Compass> getCache() {
		return cache;
	}

}
