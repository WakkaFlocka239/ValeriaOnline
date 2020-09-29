package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.warps.WarpType;

@Aliases({"staffwarp", "sw"})
@Permission("group.staff")
public class StaffWarpsCommand extends _WarpCommand {

	public StaffWarpsCommand(CommandEvent event) {
		super(event);
	}

	@Override
	public WarpType getWarpType() {
		return WarpType.STAFF;
	}

}
