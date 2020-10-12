package me.wakka.valeriaonline.features.fame;

import me.wakka.valeriaonline.features.fame.menu.FameMenu;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import org.bukkit.OfflinePlayer;

public class FameCommand extends CustomCommand {
	FameService service = new FameService();

	public FameCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void openMenu() {
		FameMenu.openMain(player());
	}

	@Path("give <player> <amount> <type>")
	@Permission("group.admin")
	void giveFame(OfflinePlayer player, int amount, FameService.FameType type) {
		Fame fame = service.get(player);
		fame.addPoints(type, amount);
		service.save(fame);
	}


}
