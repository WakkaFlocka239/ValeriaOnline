package me.wakka.valeriaonline.models.fame;

import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.models.MySQLService;
import me.wakka.valeriaonline.utils.Tasks;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class FameService extends MySQLService {

	@Override
	public Fame get(OfflinePlayer offlinePlayer) {
		return get(offlinePlayer.getUniqueId().toString());
	}

	@Override
	public Fame get(String uuid) {
		Fame fame = database.where("uuid = ?", uuid).first(Fame.class);
		if (fame.getUuid() == null) {
			fame = new Fame(uuid);
			save(fame);
			Tasks.wait(5, () -> PrefixTags.updatePrefixTags(uuid));
		}
		return fame;
	}

	public List<Fame> getAll() {
		return database.results(Fame.class);
	}

	public void delete(Fame fame) {
		database.delete(fame);
	}

	public enum FameType {
		QUEST,
		GUILD
	}
}
