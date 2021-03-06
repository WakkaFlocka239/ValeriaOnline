package me.wakka.valeriaonline.models.alerts;

import com.dieselpoint.norm.Transaction;
import me.wakka.valeriaonline.models.MySQLService;
import me.wakka.valeriaonline.utils.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlertsService extends MySQLService {
	@Override
	public Alerts get(String uuid) {
		List<Alerts.Highlight> highlights = database.where("uuid = ?", uuid).results(Alerts.Highlight.class);
		return new Alerts(uuid, highlights);
	}

	public List<Alerts> getAll(List<String> uuids) {
		List<Alerts.Highlight> highlights = database.where("uuid in (" + asList(uuids) + ")").results(Alerts.Highlight.class);

		Map<String, Alerts> alertsMap = new HashMap<>();
		for (Alerts.Highlight highlight : highlights) {
			if (alertsMap.get(highlight.getUuid()) != null) continue;

			List<Alerts.Highlight> playerHighlights = highlights.stream()
					.filter(_highlight -> highlight.getUuid().equals(_highlight.getUuid()))
					.collect(Collectors.toList());

			alertsMap.put(highlight.getUuid(), new Alerts(highlight.getUuid(), playerHighlights));
		}

		return new ArrayList<>(alertsMap.values());
	}

	public void save(Alerts alerts) {
		Tasks.async(() -> {
			Transaction trans = database.startTransaction();
			database.transaction(trans).table("alerts").where("uuid = ?", alerts.getUuid()).delete();
			alerts.getHighlights().forEach(highlight -> database.transaction(trans).insert(highlight));
			trans.commit();
		});
	}

}
