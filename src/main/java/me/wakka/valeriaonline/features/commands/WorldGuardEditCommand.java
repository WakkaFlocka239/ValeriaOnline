package me.wakka.valeriaonline.features.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@NoArgsConstructor
@Aliases("wgedit")
@Permission("group.staff")
public class WorldGuardEditCommand extends CustomCommand implements Listener {
	@Getter
	public static String permission = "worldguard.region.bypass.*";

	public WorldGuardEditCommand(CommandEvent event) {
		super(event);
	}

	@Path("[enable]")
	void toggle(Boolean enable) {
		if (enable == null) enable = !player().hasPermission(permission);

		if (enable)
			on();
		else
			off();
	}

	private void on() {
		ValeriaOnline.getPerms().playerAdd(player(), permission);
		send("&eWorldGuard editing &aenabled");
	}

	private void off() {
		ValeriaOnline.getPerms().playerRemove(player(), permission);
		send("&eWorldGuard editing &cdisabled");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission(permission))
			ValeriaOnline.getPerms().playerRemove(event.getPlayer(), permission);
	}
}
