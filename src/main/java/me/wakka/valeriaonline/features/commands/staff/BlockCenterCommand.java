package me.wakka.valeriaonline.features.commands.staff;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Location;

import static me.wakka.valeriaonline.features.commands.staff.admin.LocationCodeCommand.asJava;


@Aliases("lookcenter")
@Permission("group.staff")
public class BlockCenterCommand extends CustomCommand {
	Location centered;

	public BlockCenterCommand(CommandEvent event) {
		super(event);
		centered = Utils.getCenteredLocation(player().getLocation());
	}

	@Path
	void center() {
		player().teleport(centered);
	}

	@Path("yaw")
	void yaw() {
		Location newLocation = player().getLocation().clone();
		newLocation.setYaw(centered.getYaw());
		player().teleport(newLocation);
	}

	@Path("pitch")
	void pitch() {
		Location newLocation = player().getLocation().clone();
		newLocation.setPitch(centered.getPitch());
		player().teleport(newLocation);
	}

	@Path("look")
	void look() {
		Location newLocation = player().getLocation().clone();
		newLocation.setYaw(centered.getYaw());
		newLocation.setPitch(centered.getPitch());
		player().teleport(newLocation);
	}

	@Path("corner")
	void corner() {
		centered.setX(Math.round(player().getLocation().getX()));
		centered.setZ(Math.round(player().getLocation().getZ()));
		player().teleport(centered);
	}

	@Path("java")
	void java() {
		send(asJava(Utils.getCenteredLocation(player().getLocation())));
	}
}
