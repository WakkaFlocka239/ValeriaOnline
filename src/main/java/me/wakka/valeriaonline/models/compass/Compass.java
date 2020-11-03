package me.wakka.valeriaonline.models.compass;

import dev.morphia.annotations.Converters;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.features.misc.KingdomCompass;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.UUIDConverter;
import me.wakka.valeriaonline.models.PlayerOwnedObject;
import me.wakka.valeriaonline.models.statusbar.StatusBar;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.UUID;

@Data
@Builder
@Entity("compass")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Converters(UUIDConverter.class)
public class Compass extends PlayerOwnedObject {
	@Id
	@NonNull
	private UUID uuid;
	private boolean enabled;
	private boolean kingdom;

	private transient int taskId;
	private transient BossBar bossBar;

	public void start() {
		if (!enabled)
			return;

		if (!getOfflinePlayer().isOnline())
			return;

		stop();

		bossBar = Bukkit.createBossBar(getCompass(), BarColor.BLUE, BarStyle.SEGMENTED_6);
		bossBar.addPlayer(getPlayer());

		taskId = Tasks.repeat(0, 1, () -> {
			if (!isOnline())
				stop();
			else if (bossBar != null) {

				bossBar.setColor(StatusBar.getColor(uuid));
				bossBar.setTitle(getCompass());
			}
		});
	}

	public void stop() {
		Tasks.cancel(taskId);
		if (bossBar != null) {
			bossBar.setVisible(false);
			bossBar.removeAll();
			bossBar = null;
		}
	}

	public String getCompass() {
		if (kingdom) {
			Location target = KingdomCompass.getTarget(getPlayer());
			if (target != null)
				return StringUtils.compassTarget(getPlayer(), 14, 6, target);
		}

		return StringUtils.compass(getPlayer(), 14, 6);
	}
}
