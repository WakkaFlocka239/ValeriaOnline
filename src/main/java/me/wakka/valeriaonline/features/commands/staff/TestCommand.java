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
		ValeriaOnline.getDiscordSRV().getMainTextChannel().sendMessage("Test Message").submit();
		Map<String, String> channels = ValeriaOnline.getDiscordSRV().getChannels();
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

//	@Path("setSkin")
//	void setSkin(){
//		PlayerProfile profile = player().getPlayerProfile();
//		profile.setProperty(new ProfileProperty(
//				"textures",
//				"ewogICJ0aW1lc3RhbXAiIDogMTYwMzY3NjAyMTg4MiwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NlNjY2OTdmMzFmMGNiZGE0YzZmYTE4NGNmOTgyOGQ1ZTg5YmRmYTQ3MGRlODQ3NWUxMWIwMTU5Yzg4MGRhYTgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ",
//				"hR+PlD1orh1qLAJTRoh2hFlZ11s5GxkwgIMqAU95c/9T1Tk8Io8+k5df+I6NcLyfazbpraXpss+aNM8iKgUYK/pPCxGa3AaERbbnSjcnlZFXGgPmprzUMBt/anWyeVXJLQT9UKL0qOMXaHnPQkPralPdSmNhich836cggDgfU4qxPaFiQrrHdQ8KLzjK5dYxZq2NLjVZbsGYyIjwQw/r+pAdoxrxVk/4whE7loTnAH2mSlq+FEf3pOVfwGnIvpofeGmQSTcVjJcp8nRpryKizN3jR2iNgg3dxWbS91DAxihxGP1sZMamohohyY4oh6nmqWQecLZMvoDC7RlYNT/pKJdtt3PdFeHxfMI9zYYI6D4xGwdvMueKvHqDWlfzPdn6epctmpf2E7JkOBdDbd3MWWeXShc/aVSVjET2SSmh9e0RF1hnE08CR6CwEjTwi10eH3f8FEbnJh4vr1QYVpgsjUUv/NIng1yIQFEeYj4Uu94AOZ7ReSj3t3cPBhQP3ExiMFD6kzQIX8QcGMVLDHTmQYH7NV4y+Rb4m8he/ULoGtvAX6C0iZWcKC4xleKiu0yvGsUqlybHC+Q5eCg2EqAE0tLLFbM5g1n6+fgELANc3dfAA0JRvfkOCbQwmlICE8LfipSNl1GI/NAoSLySg7QxN7NxgBkHvYGwKMDleFMPQdY="));
//		profile.complete(true, true);
//		player().setPlayerProfile(profile);
//
//	}

}
