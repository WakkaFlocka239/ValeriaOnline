package me.wakka.valeriaonline.models.chat;

import me.wakka.valeriaonline.models.MySQLService;
import me.wakka.valeriaonline.utils.Tasks;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ChatService extends MySQLService {
	private final static Map<String, Chatter> cache = new HashMap<>();

	public Map<String, Chatter> getCache() {
		return cache;
	}

	@NotNull
	public Chatter get(OfflinePlayer player) {
		return get(player.getUniqueId().toString());
	}

	@NotNull
	public Chatter get(Player player) {
		return get(player.getUniqueId().toString());
	}

	@NotNull
	public Chatter get(String uuid) {
		cache.computeIfAbsent(uuid, $ -> {
			DatabaseChatter chatter = database.where("uuid = ?", uuid).first(DatabaseChatter.class);
			if (chatter == null || chatter.getUuid() == null) {
				Chatter _chatter = new Chatter(uuid);
				save(_chatter);
				return _chatter;
			}
			return chatter.deserialize();
		});

		return cache.get(uuid);
	}

	public void save(Chatter chatter) {
		Tasks.async(() -> saveSync(chatter));
	}

	public void saveSync(Chatter chatter) {
		saveSync(new DatabaseChatter(chatter));
	}

}
