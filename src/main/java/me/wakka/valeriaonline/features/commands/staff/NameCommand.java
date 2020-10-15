package me.wakka.valeriaonline.features.commands.staff;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.OfflinePlayer;

@Permission("group.staff")
public class NameCommand extends CustomCommand {

	public NameCommand(CommandEvent event) {
		super(event);
	}

	@Path("<uuid>")
	@Description("get player from uuid")
	void uuid(OfflinePlayer player) {
		send(json("&d" + player.getName())
				.hover("&7Shift+Click to insert into your chat")
				.insert(player.getName()));
	}

}
