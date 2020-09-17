package me.wakka.valeriaonline.features.cooldown;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cooldown {
	@NonNull
	private UUID uuid;
	@NonNull
	private Map<String, LocalDateTime> cooldowns = new HashMap<>();

	public boolean exists(String type) {
		return cooldowns.containsKey(type);
	}

	public LocalDateTime getTime(String type) {
		return cooldowns.getOrDefault(type, null);
	}

	public boolean check(String type) {
		return !exists(type) || cooldowns.get(type).isBefore(LocalDateTime.now());
	}

	public Cooldown create(String type, double ticks) {
		cooldowns.put(type, LocalDateTime.now().plusSeconds((long) ticks / 20));
		return this;
	}

	public void clear(String type) {
		cooldowns.remove(type);
	}
}
