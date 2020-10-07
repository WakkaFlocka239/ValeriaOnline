package me.wakka.valeriaonline.features.commands;


import me.wakka.valeriaonline.features.autorestart.AutoRestart;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.Material;

@Permission("group.dev")
public class TestCommand extends CustomCommand {

	public TestCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void run() {
		AutoRestart.closeDungeons.getBlock().setType(Material.REDSTONE_BLOCK);

		Tasks.wait(Time.SECOND.x(5), () -> {
			AutoRestart.closeDungeons.getBlock().setType(Material.AIR);
			Tasks.wait(Time.SECOND.x(1), () -> AutoRestart.openDungeons.getBlock().setType(Material.AIR));
		});


	}


}
