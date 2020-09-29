package me.wakka.valeriaonline.features.teleportrequests;

import lombok.Getter;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Requests {
	@Getter
	private static final List<Request> activeRequests = new ArrayList<>();

	static {
		Tasks.repeatAsync(Time.SECOND.x(5), Time.MINUTE, () -> {
			List<Request> requests = new ArrayList<>(getActiveRequests());
			for (Request request : requests) {
				if (request.getTimeSent().isBefore(LocalDateTime.now().minusSeconds(120))) {
					request.setExpired(true);
					activeRequests.remove(request);
				}
			}
		});
	}

	public static void add(Request teleport) {
		activeRequests.add(teleport);
	}

	public static Request getBySender(Player sender) {
		Optional<Request> requestOptional = activeRequests.stream()
				.filter(request -> request.getSenderUUID().equals(sender.getUniqueId()))
				.findFirst();

		return requestOptional.orElse(null);
	}

	public static Request getByReceiver(Player receiver) {
		Optional<Request> requestOptional = activeRequests.stream()
				.filter(request -> request.getReceiverUUID().equals(receiver.getUniqueId()))
				.findFirst();

		return requestOptional.orElse(null);
	}

	public static void remove(Request request) {
		activeRequests.remove(request);
	}
}
