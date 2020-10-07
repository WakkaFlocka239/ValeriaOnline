package me.wakka.valeriaonline.features.commands;

import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.setting.Setting;
import me.wakka.valeriaonline.models.setting.SettingService;
import me.wakka.valeriaonline.utils.WorldGuardUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@NoArgsConstructor
@Aliases("wgedit")
@Permission("group.staff")
public class WorldGuardEditCommand extends CustomCommand implements Listener {
	SettingService service = new SettingService();
	Setting setting;

	public WorldGuardEditCommand(CommandEvent event) {
		super(event);
	}

	@Path("[enable]")
	@Description("")
	void toggle(Boolean enable) {
		setting = service.get(player(), "worldGuardEdit");
		if (enable == null)
			enable = !setting.getBoolean();

		if (enable)
			on();
		else
			off();
	}

	private void on() {
		setting = service.get(player(), "worldGuardEdit");
		setting.setBoolean(true);
		service.save(setting);

		send("&eWorldGuard editing &aenabled");
	}

	private void off() {
		setting = service.get(player(), "worldGuardEdit");
		setting.setBoolean(false);
		service.save(setting);

		send("&eWorldGuard editing &cdisabled");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		setting = service.get(event.getPlayer(), "worldGuardEdit");
		if (setting.getBoolean()) {
			setting.setBoolean(false);
			service.save(setting);
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		if (cancelEvent(event.getPlayer()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if (cancelEvent(event.getPlayer()))
			event.setCancelled(true);
	}

	private boolean cancelEvent(Player player) {
		if (!player.hasPermission("group.staff"))
			return false;

		WorldGuardUtils WGUtils = new WorldGuardUtils(player);
		if (WGUtils.getRegionsAt(player.getLocation()).size() == 0)
			return false;

		return !service.get(player, "worldGuardEdit").getBoolean();
	}

}
