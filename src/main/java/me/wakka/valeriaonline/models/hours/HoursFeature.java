package me.wakka.valeriaonline.models.hours;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.features.Feature;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HoursFeature extends Feature {
	final int INTERVAL = 5;

	@Override
	public void startup() {

		scheduler();

		ValeriaOnline.getCron().schedule("00 00 * * *", () -> new HoursService().endOfDay());
		ValeriaOnline.getCron().schedule("00 00 * * 1", () -> new HoursService().endOfWeek());
		ValeriaOnline.getCron().schedule("00 00 1 * *", () -> new HoursService().endOfMonth());
	}

	private void scheduler() {
		Tasks.repeatAsync(10, Time.SECOND.x(INTERVAL), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (ValeriaOnline.getEssentials().getUser(player).isAfk()) continue;

					HoursService service = new HoursService();
					Hours hours = service.get(player);
					hours.increment(INTERVAL);
					service.save(hours);

				} catch (Exception ex) {
					ValeriaOnline.warn("Error in Hours scheduler: " + ex.getMessage());
				}
			}
		});
	}

}
