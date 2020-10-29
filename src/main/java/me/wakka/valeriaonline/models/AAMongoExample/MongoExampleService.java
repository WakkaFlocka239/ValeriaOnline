package me.wakka.valeriaonline.models.AAMongoExample;


import me.wakka.valeriaonline.framework.persistence.annotations.PlayerClass;
import me.wakka.valeriaonline.models.MongoService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@PlayerClass(MongoExample.class)
public class MongoExampleService extends MongoService {
	private final static Map<UUID, MongoExample> cache = new HashMap<>();

	public Map<UUID, MongoExample> getCache() {
		return cache;
	}

}
