package me.wakka.valeriaonline.features.commands;

import fr.minuskube.inv.SmartInvsPlugin;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.features.compass.Compass;
import me.wakka.valeriaonline.features.listeners.TeleportScrolls;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

@Permission("group.dev")
@NoArgsConstructor
public class ValeriaOnlineCommand extends CustomCommand implements Listener {

	public ValeriaOnlineCommand(CommandEvent event) {
		super(event);
	}

	@Path("reload")
	@Description("reload the plugin")
	void reload() {
		File file = Paths.get("plugins/ValeriaOnline.jar").toFile();
		if (!file.exists())
			error("ValeriaOnline.jar doesn't exist, cannot reload");

		try {
			new ZipFile(file).entries();
		} catch (IOException ex) {
			error("ValeriaOnline.jar is not complete, cannot reload");
		}

		long invCount = Bukkit.getOnlinePlayers().stream().filter(player -> SmartInvsPlugin.manager().getInventory(player).isPresent()).count();
		if (invCount > 0)
			error("There are " + invCount + " SmartInvs menus open, cannot reload");

		runCommand("plugman reload ValeriaOnline");
	}

	@Path("getItem <item...>")
	void getItem(String item) {
		if (item.equalsIgnoreCase("compass"))
			Utils.giveItem(player(), Compass.getItem());

		if (item.equalsIgnoreCase("valeria scroll"))
			Utils.giveItem(player(), TeleportScrolls.scroll_valeria);

		if (item.equalsIgnoreCase("eredhil scroll"))
			Utils.giveItem(player(), TeleportScrolls.scroll_eredhil);

		if (item.equalsIgnoreCase("maldun scroll"))
			Utils.giveItem(player(), TeleportScrolls.scroll_maldun);
	}
}
