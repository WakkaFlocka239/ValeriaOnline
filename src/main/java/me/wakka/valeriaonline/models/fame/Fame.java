package me.wakka.valeriaonline.models.fame;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.OfflinePlayer;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Fame {
	@Id
	@NonNull
	String uuid;
	int points_quest;
	int points_guild;
	String activeTag;

	public OfflinePlayer getPlayer() {
		return Utils.getPlayer(uuid);
	}

	public void addPoints(FameService.FameType type, int amount) {
		if (type.equals(FameService.FameType.QUEST))
			points_quest += amount;
		else if (type.equals(FameService.FameType.GUILD))
			points_guild += amount;
	}

	public int getPoints(FameService.FameType filter) {
		if (filter.equals(FameService.FameType.QUEST))
			return points_quest;
		else
			return points_guild;
	}
}
