package me.wakka.valeriaonline.features.commands;


import io.lumine.xikage.mythicmobs.items.MythicItem;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

import java.util.Optional;

@Permission("group.dev")
public class TestCommand extends CustomCommand {

	public TestCommand(CommandEvent event) {
		super(event);
	}

//	@Path("resetDungeon")
//	void resetDungeon() {
//		AutoRestart.closeDungeons.getBlock().setType(Material.REDSTONE_BLOCK);
//
//		Tasks.wait(Time.SECOND.x(5), () -> {
//			AutoRestart.closeDungeons.getBlock().setType(Material.AIR);
//			Tasks.wait(Time.SECOND.x(1), () -> AutoRestart.openDungeons.getBlock().setType(Material.AIR));
//		});
//	}

	@Path("mmitem <string>")
	void getMMItem(String string) {
		Optional<MythicItem> item = ValeriaOnline.getMythicMobs().getItemManager().getItem(string);
		if (!item.isPresent()) {
			error("Unknown Item");
			return;
		}

		MythicItem mythicItem = item.get();
		send(mythicItem.toString());
	}

//	@Path("mcmmo")
//	void mcmmo(){
//		Objective powerObjective;
//		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
//		String POWER_OBJECTIVE = "McMMO_PL_OBJ";
//
//		powerObjective = scoreboard.registerNewObjective(POWER_OBJECTIVE, "dummy", POWER_OBJECTIVE);
//		powerObjective.setDisplayName("Power Level: ");
//		powerObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
//		McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player().getPlayer());
//
//		if (mcMMOPlayer != null) {
//			powerObjective.getScore(mcMMOPlayer.getProfile().getPlayerName()).setScore(mcMMOPlayer.getPowerLevel());
//			player().setScoreboard(scoreboard);
//		}
//	}
}
