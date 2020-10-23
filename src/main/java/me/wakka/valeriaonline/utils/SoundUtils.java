package me.wakka.valeriaonline.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collection;

//@SuppressWarnings({"ConstantConditions", "UnusedAssignment"})
public class SoundUtils {

	public static void playSoundAll(Sound sound, float volume, float pitch) {
		Bukkit.getOnlinePlayers().forEach(player -> playSound(player, sound, volume, pitch));
	}

	public static void playSound(Player player, Sound sound, float volume, float pitch) {
		playSound(player, sound, SoundCategory.MASTER, volume, pitch);
	}

	public static void playSound(Player player, Sound sound, SoundCategory category, float volume, float pitch) {
		player.playSound(player.getLocation(), sound, category, volume, pitch);
	}

	public static void playSound(Player player, SoundArgs soundArgs) {
		if (soundArgs.getCategory() == null)
			soundArgs.setCategory(SoundCategory.MASTER);

		playSound(player, soundArgs.getSound(), soundArgs.getCategory(), soundArgs.getVolume(), soundArgs.getPitch());
	}

	public static void stopSound(Player player, Sound sound) {
		stopSound(player, sound, null);
	}

	public static void stopSound(Player player, Sound sound, SoundCategory category) {
		player.stopSound(sound, category);
	}

	public enum Jingle {
		PING {
			@Override
			public void play(Player player) {
				player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
			}
		},

		RANKUP {
			@Override
			public void play(Player player) {
				int wait = 0;
				Tasks.wait(wait += 0, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, 1, 0.749154F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, 1, 0.749154F);
				});
				Tasks.wait(wait += 4, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, 1, 0.561231F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, 1, 0.561231F);
				});
				Tasks.wait(wait += 4, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, 1, 0.629961F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, 1, 0.629961F);
				});
				Tasks.wait(wait += 2, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, 1, 0.707107F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, 1, 0.707107F);
				});
				Tasks.wait(wait += 2, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, 1, 0.840896F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, 1, 0.840896F);
				});
				Tasks.wait(wait += 2, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, SoundCategory.RECORDS, 1, 1.122462F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, 1, 1.122462F);
				});
			}
		},

		FIRST_JOIN {
			@Override
			public void play(Player player) {
				int wait = 0;
				Tasks.wait(wait += 0, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.RECORDS, .3F, 0.561231F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, .3F, 0.561231F);
				});
				Tasks.wait(wait += 2, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.RECORDS, .3F, 0.629961F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, .3F, 0.629961F);
				});
				Tasks.wait(wait += 2, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.RECORDS, .3F, 0.561231F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, .3F, 0.561231F);
				});
				Tasks.wait(wait += 2, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.RECORDS, .3F, 0.840896F);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.RECORDS, .3F, 0.840896F);
				});
			}
		},

		JOIN {
			@Override
			public void play(Player player) {
				int wait = 0;
				Tasks.wait(wait += 0, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.5F));
				Tasks.wait(wait += 2, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.667420F));
				Tasks.wait(wait += 2, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.749154F));
				Tasks.wait(wait += 2, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 1F));
			}
		},

		QUIT {
			@Override
			public void play(Player player) {
				int wait = 0;
				Tasks.wait(wait += 0, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.707107F));
				Tasks.wait(wait += 4, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.629961F));
				Tasks.wait(wait += 4, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.707107F));
				Tasks.wait(wait += 4, () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.RECORDS, .5F, 0.529732F));
			}
		};

		public abstract void play(Player player);

		public void play(Collection<? extends Player> players) {
			players.forEach(this::play);
		}

		public void playAll() {
			play(Bukkit.getOnlinePlayers());
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@RequiredArgsConstructor
	public static class SoundArgs {
		@Nonnull
		Sound sound;
		SoundCategory category;
		@Nonnull
		Float volume;
		@Nonnull
		Float pitch;
		int delay = 0;

		public SoundArgs(@NotNull Sound sound, float volume, float pitch, int delay) {
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
			this.delay = delay;
		}

		public SoundArgs(Sound sound, SoundCategory category, float volume, float pitch) {
			this.sound = sound;
			this.category = category;
			this.volume = volume;
			this.pitch = pitch;
		}
	}

}
