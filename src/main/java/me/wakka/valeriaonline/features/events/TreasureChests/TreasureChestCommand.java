package me.wakka.valeriaonline.features.events.TreasureChests;

import me.wakka.valeriaonline.features.menus.MenuUtils;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.setting.Setting;
import me.wakka.valeriaonline.models.setting.SettingService;
import me.wakka.valeriaonline.utils.Utils;

@Permission("group.dev")
public class TreasureChestCommand extends CustomCommand {
	SettingService service = new SettingService();

	public TreasureChestCommand(CommandEvent event) {
		super(event);
	}

	@Path("clearDatabase")
	void clearDatabase() {
		if (!player().equals(Utils.wakka()))
			error("only wakka can run the command.");

		MenuUtils.ConfirmationMenu.builder()
				.onConfirm(e -> {
					for (Setting setting : service.getFromType(TreasureChests.setting)) {
						String name = Utils.getPlayer(setting.getId()).getName();
						String value = setting.getValue();
						send("Deleted Entry: " + name + ": " + value);

						service.delete(setting);
					}
				})
				.open(player());
	}


}
