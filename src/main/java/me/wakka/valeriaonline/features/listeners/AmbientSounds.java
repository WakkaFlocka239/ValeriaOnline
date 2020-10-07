package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.features.altars.Altars;
import me.wakka.valeriaonline.features.dungeons.Dungeons;
import me.wakka.valeriaonline.utils.RandomUtils;
import me.wakka.valeriaonline.utils.SoundUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmbientSounds {
	int startDelay = Time.SECOND.x(5);
	//
	private final static Map<Player, Integer> altarTaskMap = new HashMap<>();
	private final static Sound altarLoop = Sound.AMBIENT_CRIMSON_FOREST_LOOP;
	private final static List<Sound> altarSounds = Arrays.asList(Sound.ENTITY_VEX_AMBIENT,
			Sound.AMBIENT_CAVE, Sound.AMBIENT_CRIMSON_FOREST_ADDITIONS, Sound.AMBIENT_CRIMSON_FOREST_MOOD);
	//
	private final static Map<Player, Integer> dungeonTaskMap = new HashMap<>();
	private final static Sound dungeonLoop = Sound.AMBIENT_CRIMSON_FOREST_LOOP;
	private final static List<Sound> dungeonSounds = Arrays.asList(Sound.ENTITY_VEX_AMBIENT,
			Sound.AMBIENT_CAVE, Sound.AMBIENT_CRIMSON_FOREST_ADDITIONS, Sound.AMBIENT_CRIMSON_FOREST_MOOD);

	public static void shutdown() {
		altarTaskMap.forEach((player, integer) -> SoundUtils.stopSound(player, altarLoop));
		dungeonTaskMap.forEach((player, integer) -> SoundUtils.stopSound(player, dungeonLoop));
	}

	public AmbientSounds() {
		// Random sounds
		Tasks.repeat(startDelay, Time.SECOND.x(15), () -> {
			if (RandomUtils.chanceOf(50)) {
				Sound sound = RandomUtils.randomElement(altarSounds);
				Map<Player, Integer> tempMap = new HashMap<>(altarTaskMap);

				tempMap.forEach((player, integer) ->
						SoundUtils.playSound(player, sound, SoundCategory.AMBIENT, 0.5F, 0.1F));
			}

			if (RandomUtils.chanceOf(50)) {
				Sound sound = RandomUtils.randomElement(dungeonSounds);
				Map<Player, Integer> tempMap = new HashMap<>(dungeonTaskMap);

				tempMap.forEach((player, integer) ->
						SoundUtils.playSound(player, sound, SoundCategory.AMBIENT, 0.5F, 0.1F));
			}
		});

		// Looping Sound Management
		Tasks.repeat(startDelay, Time.SECOND.x(1), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {

				boolean inArea;
				boolean onList;

				// Altars
				inArea = Altars.isInAltar(player);
				onList = altarTaskMap.containsKey(player);

				if (inArea && !onList) {
					startLoop(player, AmbientSoundType.ALTAR);

				} else if (!inArea && onList) {
					stopLoop(player, AmbientSoundType.ALTAR);
				}
				// Dungeons
				inArea = Dungeons.isInDungeon(player);
				onList = dungeonTaskMap.containsKey(player);

				if (inArea && !onList) {
					startLoop(player, AmbientSoundType.DUNGEON);

				} else if (!inArea && onList) {
					stopLoop(player, AmbientSoundType.DUNGEON);
				}
			}
		});
	}

	private void startLoop(Player player, AmbientSoundType type) {
		if (type.equals(AmbientSoundType.ALTAR)) {
			int taskId = Tasks.repeat(0, Time.SECOND.x(37), () ->
					SoundUtils.playSound(player, altarLoop, SoundCategory.AMBIENT, 2F, 1F));

			altarTaskMap.put(player, taskId);
		} else {
			int taskId = Tasks.repeat(0, Time.SECOND.x(37), () ->
					SoundUtils.playSound(player, dungeonLoop, SoundCategory.AMBIENT, 2F, 1F));

			dungeonTaskMap.put(player, taskId);
		}
	}

	private void stopLoop(Player player, AmbientSoundType type) {
		Integer taskId;
		Sound sound;
		if (type.equals(AmbientSoundType.ALTAR)) {
			taskId = altarTaskMap.get(player);
			altarTaskMap.remove(player);

			sound = altarLoop;
		} else {
			taskId = dungeonTaskMap.get(player);
			dungeonTaskMap.remove(player);

			sound = dungeonLoop;
		}

		if (taskId != null)
			Tasks.cancel(taskId);

		SoundUtils.stopSound(player, sound, SoundCategory.AMBIENT);
	}

	public enum AmbientSoundType {
		ALTAR,
		DUNGEON
	}

}
