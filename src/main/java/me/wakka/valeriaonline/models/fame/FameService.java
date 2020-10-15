package me.wakka.valeriaonline.models.fame;

import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.models.MySQLService;
import me.wakka.valeriaonline.utils.Tasks;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
		return database.select("*").results(Fame.class);
	}

	public void delete(Fame fame) {
		database.table("fame").where("uuid = ? ", fame.getUuid()).delete();
	}

	public enum FameType {
		QUEST,
		GUILD
	}

	@ConverterFor(FameType.class)
	FameType convertToFameType(String value) {
		return FameType.valueOf(value);
	}

	@TabCompleterFor(FameType.class)
	List<String> tabCompleteFameType(String filter) {
		return Arrays.stream(FameType.values())
				.filter(fameType -> fameType.name().toLowerCase().startsWith(filter.toLowerCase()))
				.map(Enum::name)
				.collect(Collectors.toList());
	}
}
