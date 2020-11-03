package me.wakka.valeriaonline.models.statusbar;


import me.wakka.valeriaonline.framework.persistence.annotations.PlayerClass;
import me.wakka.valeriaonline.models.MongoService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@PlayerClass(StatusBar.class)
public class StatusBarService extends MongoService {
	private final static Map<UUID, StatusBar> cache = new HashMap<>();

	public Map<UUID, StatusBar> getCache() {
		return cache;
	}

}
