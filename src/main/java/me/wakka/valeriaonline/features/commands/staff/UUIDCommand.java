package me.wakka.valeriaonline.features.commands.staff;


import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.OfflinePlayer;

@Permission("group.staff")
public class UUIDCommand extends CustomCommand {

	public UUIDCommand(CommandEvent event) {
		super(event);
	}

	@Path("<player>")
	@Description("get uuid of player")
	void uuid(@Arg("self") OfflinePlayer player) {
		send(json("&d" + player.getUniqueId())
				.hover("&7Shift+Click to insert into your chat")
				.insert(player.getUniqueId().toString()));
	}

}
