package me.wakka.valeriaonline.models.scoreboard;

import dev.morphia.annotations.Converters;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.features.scoreboard.ScoreboardLine;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.UUIDConverter;
import me.wakka.valeriaonline.models.PlayerOwnedObject;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.VOScoreboard;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Strings.isNullOrEmpty;

@Data
@Entity("scoreboard_user")
@NoArgsConstructor
@AllArgsConstructor
@Converters(UUIDConverter.class)
public class ScoreboardUser extends PlayerOwnedObject {
	@Id
	@NonNull
	private UUID uuid;
	private Map<ScoreboardLine, Boolean> lines = new HashMap<>();
	private boolean active = true;

	private transient VOScoreboard scoreboard;
	private transient ListOrderedMap<ScoreboardLine, String> rendered = new ListOrderedMap<>();
	private transient int headerTaskId = -1;
	private transient Map<ScoreboardLine, Integer> taskIds = new HashMap<>();

	public static final int HEADER_UPDATE_INTERVAL = 2;
	public static final int UPDATE_INTERVAL = 40;

	public ScoreboardUser(UUID uuid) {
		this.uuid = uuid;
		if (lines.isEmpty())
			lines = ScoreboardLine.getDefaultLines(getPlayer());
	}

	public void on() {
		off();
		if (scoreboard == null)
			scoreboard = new VOScoreboard("bnsb-" + uuid.toString().replace("-", ""), "&e> &3Bear Nation &e<", getPlayer());
		else
			scoreboard.subscribe(getPlayer());
		active = true;
		Tasks.cancel(headerTaskId);
		headerTaskId = Tasks.repeatAsync(0, (ScoreboardLine.getHeaderFrames().size() + 1) * HEADER_UPDATE_INTERVAL, new Header(getPlayer()));
		startTasks();
	}

	public void off() {
		active = false;
		pause();
	}

	public void pause() {
		if (scoreboard != null) {
			if (getOfflinePlayer().isOnline())
				scoreboard.unsubscribe(getPlayer());
			scoreboard.delete();
			scoreboard = null;
		}
		rendered = new ListOrderedMap<>();
		Tasks.cancel(headerTaskId);
		headerTaskId = -1;
		cancelTasks();
	}

	public void cancelTasks() {
		taskIds.values().forEach(Tasks::cancel);
		taskIds.clear();
	}

	private String getRenderedText(ScoreboardLine line) {
		return rendered.getOrDefault(line, null);
	}

	private int getScore(ScoreboardLine line) {
		List<ScoreboardLine> renderedOrder = new ArrayList<>();
		for (ScoreboardLine toRender : ScoreboardLine.values())
			if (lines.containsKey(toRender) && lines.get(toRender))
				renderedOrder.add(toRender);
		return renderedOrder.size() - renderedOrder.indexOf(line) - 1;
	}

	public void startTasks() {
		cancelTasks();
		Arrays.asList(ScoreboardLine.values()).forEach(line -> {
			if (lines.containsKey(line) && lines.get(line))
				taskIds.put(line, Tasks.repeatAsync(5, line.getInterval(), () -> render(line)));
		});
	}

	public void remove(ScoreboardLine line) {
		removeLine(getRenderedText(line));
		rendered.remove(line);
		if (taskIds.containsKey(line))
			Tasks.cancel(taskIds.get(line));
	}

	public void render(ScoreboardLine line) {
		if (!getOfflinePlayer().isOnline()) {
			pause();
			return;
		}

		String oldText = getRenderedText(line);
		if (lines.containsKey(line) && lines.get(line)) {
			String newText = line.render(getPlayer());

			if (!isNullOrEmpty(newText)) {
				if (newText.equals(oldText))
					if (scoreboard.getLines().containsKey(oldText) && scoreboard.getLines().get(oldText) == getScore(line))
						return;

				removeLine(oldText);
				rendered.put(line, newText);
				scoreboard.setLine(newText, getScore(line));
			} else {
				removeLine(oldText);
			}
		} else {
			removeLine(oldText);
		}
	}

	private void removeLine(String oldText) {
		try {
			scoreboard.removeLine(oldText);
		} catch (NullPointerException ignore) {
		}
	}

	public static class Header implements Runnable {
		private final ScoreboardUser user;

		public Header(Player player) {
			user = new ScoreboardService().get(player);
		}

		@Override
		public void run() {
			AtomicInteger wait = new AtomicInteger(0);
			ScoreboardLine.getHeaderFrames().iterator().forEachRemaining(header ->
					Tasks.waitAsync(wait.getAndAdd(HEADER_UPDATE_INTERVAL), () -> {
						if (user.isActive() && user.getScoreboard() != null)
							user.getScoreboard().setTitle(header);
					}));
		}
	}
}
