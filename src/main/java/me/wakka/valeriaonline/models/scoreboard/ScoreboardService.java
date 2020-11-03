package me.wakka.valeriaonline.models.scoreboard;

import me.wakka.valeriaonline.framework.persistence.annotations.PlayerClass;
import me.wakka.valeriaonline.models.MongoService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@PlayerClass(ScoreboardUser.class)
public class ScoreboardService extends MongoService {
	private final static Map<UUID, ScoreboardUser> cache = new HashMap<>();

	public Map<UUID, ScoreboardUser> getCache() {
		return cache;
	}

	public void delete(ScoreboardUser user) {
		if (user.getScoreboard() != null)
			user.getScoreboard().delete();
		super.delete(user);
	}

}
