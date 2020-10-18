package me.wakka.valeriaonline.features.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Aliases("nearby")
public class NearCommand extends CustomCommand {

	public NearCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("[player]")
	void run(@Arg("self") Player player) {
		List<Player> nearby = Bukkit.getOnlinePlayers().stream()
				.filter(_player -> player.getUniqueId() != _player.getUniqueId()
						&& player.getWorld() == _player.getWorld()
						&& getDistance(player, _player) <= Chat.getLocalRadius()
						&& Utils.canSee(player(), _player))
				.collect(Collectors.toList());

		boolean showDistance = player.hasPermission("near.distance");

		String message = "&dPlayers nearby" + (isSelf(player) ? "" : " " + player.getName()) + "";
		if (nearby.size() == 0)
			send(message + ": &fNone");
		else
			send(message + " (&7" + nearby.size() + "&d): &f" + nearby.stream()
					.map(_player -> {
						if (showDistance)
							return _player.getName() + " (&7" + getDistance(player, _player) + "m&f)";
						else
							return _player.getName();
					})
					.collect(Collectors.joining(", ")));
	}

	private long getDistance(@Arg("self") Player player, Player _player) {
		return Math.round(player.getLocation().distance(_player.getLocation()));
	}

}
