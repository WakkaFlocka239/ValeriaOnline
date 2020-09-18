package me.wakka.valeriaonline.features.trading;

import me.wakka.valeriaonline.features.trading.menu.TradeEditorMenus;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

public class TradeEditorCommand extends CustomCommand {

	public TradeEditorCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void menu() {
		TradeEditorMenus.openMain(player());
	}

}
