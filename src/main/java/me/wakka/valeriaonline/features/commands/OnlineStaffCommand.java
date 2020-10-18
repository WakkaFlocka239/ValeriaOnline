package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Allow vanished players to be displayed, but only tho those who can also see them
public class OnlineStaffCommand extends CustomCommand {

	public OnlineStaffCommand(CommandEvent event) {
		super(event);
	}

	@Path
	void onlineStaff() {
		List<Player> onlineStaff = Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("group.staff")).collect(Collectors.toList());
		List<Player> unvanishedStaff = onlineStaff.stream().filter(player -> !Utils.isVanished(player)).collect(Collectors.toList());

		List<String> onlineStaffNames = new ArrayList<>();
		unvanishedStaff.forEach(staff -> onlineStaffNames.add(staff.getName()));
		String staffMembers = String.join(", ", onlineStaffNames);

		long vanished = onlineStaff.size() - unvanishedStaff.size();
		long online = unvanishedStaff.size();
		boolean canSeeVanished = player().hasPermission("pv.see");
		String counts = online + ((canSeeVanished && vanished > 0) ? " &7+ &d" + vanished : "");

		line();
		send("&7There are &d" + counts + " &7staff members online");
		send("&d" + staffMembers);
		line();
		send("&7View a full list of staff members with &c/staff");
		line();
	}
}
