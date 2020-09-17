package me.wakka.valeriaonline.features.altars;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

public class AltarCommand extends CustomCommand {
	public AltarCommand(CommandEvent event) {
		super(event);
	}

	@Path("activate")
	void activate(){
		Altars.set(player().getLocation(), true);
	}

	@Path("deactivate")
	void deactivate(){
		Altars.set(player().getLocation(), false);
	}
}
