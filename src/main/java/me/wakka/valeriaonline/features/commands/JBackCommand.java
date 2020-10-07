package me.wakka.valeriaonline.features.commands;

import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.back.Back;
import me.wakka.valeriaonline.models.back.BackService;
import me.wakka.valeriaonline.utils.CitizensUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

//@Aliases("return")
@NoArgsConstructor
public class JBackCommand extends CustomCommand implements Listener {
	BackService service = new BackService();
	Back back;

	public JBackCommand(CommandEvent event) {
		super(event);
		back = service.get(player());
	}

	@Path()
	void back() {
		Location location = back.getLocation();
		if (location == null) {
			error("You have no back location");
			return;
		}

		player().teleport(location, TeleportCause.COMMAND);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		Location location = event.getFrom();

		if (CitizensUtils.isNPC(player)) return;
		if (TeleportCause.COMMAND != event.getCause()) return;

		Back back = new BackService().get(player);
		back.setLocation(location);
		new BackService().save(back);
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if (!player.hasPermission("group.staff")) return;
		if (CitizensUtils.isNPC(player)) return;

		Back back = new BackService().get(player);
		back.setLocation(player.getLocation());
		new BackService().save(back);
	}

}
