package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.inventory.ItemStack;

@Permission("group.creator")
public class FixLoreCommand extends CustomCommand {

	public FixLoreCommand(CommandEvent event){
		super(event);
	}

	@Path()
	@Description("Fix lore on held item")
	void fixHand(){
		ItemStack tool = Utils.getToolRequired(player());

		if(!tool.hasItemMeta())
			error("Item doesn't have meta");

		ItemStack fixed = new ItemBuilder(tool).loreize(true).lore(tool.getLore()).build();
		int heldSlot = player().getInventory().getHeldItemSlot();
		player().getInventory().setItem(heldSlot, fixed);
		send(PREFIX + "Lore fixed!");

	}

	@Path("inv")
	@Description("Fix lore on all items in inventory")
	void fixInv(){
		ItemStack[] contents = player().getInventory().getContents();
		int count = 0;
		for (ItemStack item : contents) {
			if(Utils.isNullOrAir(item) || item.getLore() == null)
				continue;

			ItemStack fixed = new ItemBuilder(item).loreize(true).lore(item.getLore()).build();
			item.setLore(fixed.getLore());
			++count;
		}

		send(PREFIX + count + " items fixed!");
	}
}
