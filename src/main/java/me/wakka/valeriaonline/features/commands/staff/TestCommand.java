package me.wakka.valeriaonline.features.commands.staff;


import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

	@Path("giveAllMMItems")
	void allMMItems() {
		List<ItemStack> items = new ArrayList<>();
		for (MythicItem item : ValeriaOnline.getMythicMobs().getItemManager().getItems()) {
			items.add(BukkitAdapter.adapt((item).generateItemStack(1)));
		}
		Utils.giveItems(player(), items);

	}

	@Path("hex <string...>")
	void hex(String string) {
		player().chat(StringUtils.colorize(string));
	}

	@Path("discord")
	void discord() {
		ValeriaOnline.discordSRV.getMainTextChannel().sendMessage("Test Message").submit();
		Map<String, String> channels = ValeriaOnline.discordSRV.getChannels();
		Utils.wakka(String.valueOf(channels));
	}

	@Path("getSkullOwner")
	void skullOwner() {
		ItemStack itemStack = Utils.getToolRequired(player());
		if (!itemStack.getType().equals(Material.PLAYER_HEAD))
			error("Must be holding a player head!");

		ItemMeta itemMeta = itemStack.getItemMeta();
		SkullMeta skullMeta = (SkullMeta) itemMeta;


		if (skullMeta.getPlayerProfile() == null)
			error("Skull does not have an owning player");

		if (skullMeta.getPlayerProfile().getId() == null)
			error("UUID is null");

		String uuid = skullMeta.getPlayerProfile().getId().toString();

		send(json("Click to copy: ").group().next(uuid).suggest(uuid));
	}

}
