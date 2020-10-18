package me.wakka.valeriaonline.features.chat.commands;

import lombok.NonNull;
import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Aliases("cc")
@Permission("group.staff")
public class ClearChatCommand extends CustomCommand {

	public ClearChatCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path
	void run() {
		for (Player player : Bukkit.getOnlinePlayers())
			if (!player.hasPermission("group.staff"))
				line(player, 40);

		Chat.broadcast("Chat has been cleared, sorry for any inconvenience.");
	}


}
