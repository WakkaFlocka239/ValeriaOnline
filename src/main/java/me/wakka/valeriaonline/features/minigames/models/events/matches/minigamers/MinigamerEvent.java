package me.wakka.valeriaonline.features.minigames.models.events.matches.minigamers;

import lombok.Getter;
import lombok.NonNull;
import me.wakka.valeriaonline.features.minigames.models.Minigamer;
import me.wakka.valeriaonline.features.minigames.models.events.matches.MatchEvent;

public class MinigamerEvent extends MatchEvent {
	@Getter
	@NonNull
	Minigamer minigamer;

	public MinigamerEvent(Minigamer minigamer) {
		super(minigamer.getMatch());
		this.minigamer = minigamer;
	}

}
