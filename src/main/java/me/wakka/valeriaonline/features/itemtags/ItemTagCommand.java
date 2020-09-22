package me.wakka.valeriaonline.features.itemtags;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.RandomUtils;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.wakka.valeriaonline.features.itemtags.ItemTagUtils.addCondition;
import static me.wakka.valeriaonline.features.itemtags.ItemTagUtils.addRarity;
import static me.wakka.valeriaonline.features.itemtags.ItemTagUtils.finalizeItem;
import static me.wakka.valeriaonline.features.itemtags.ItemTagUtils.updateItem;

@Aliases({"itemtags"})
@Permission("group.creator")
public class ItemTagCommand extends CustomCommand {
	public ItemTagCommand(CommandEvent event) {
		super(event);
	}

	@Path("get")
	@Description("Get item tags on held item")
	void getTags(){
		ItemStack tool = Utils.getToolRequired(player());

		send("");
		send("Item Tags: ");
		Condition condition = Condition.of(tool);
		if (condition != null)
			send(condition.getTag());

		Rarity rarity = Rarity.of(tool);
		if(rarity != null)
			send(rarity.getTag());
		send("");
	}

	@Path("update")
	@Description("Update item tags on held item")
	void update(){
		ItemStack tool = Utils.getToolRequired(player());

		ItemStack updated = updateItem(tool);
		int heldSlot = player().getInventory().getHeldItemSlot();
		player().getInventory().setItem(heldSlot, updated);
	}

	@Path("setRarity <rarity>")
	void setRarity(Rarity rarity){
		ItemStack tool = Utils.getToolRequired(player());

		ItemStack updated = finalizeItem(addRarity(tool, rarity, true));
		int heldSlot = player().getInventory().getHeldItemSlot();
		player().getInventory().setItem(heldSlot, updated);
	}

	@Path("setCondition <condition>")
	void setCondition(Condition condition){
		ItemStack tool = Utils.getToolRequired(player());

		ItemStack updated = finalizeItem(addCondition(tool, condition, true));
		Utils.setDurability(updated, RandomUtils.randomInt(condition.getMin(), condition.getMax()));

		int heldSlot = player().getInventory().getHeldItemSlot();
		player().getInventory().setItem(heldSlot, updated);
	}

	// Tab Completers

	@ConverterFor(Rarity.class)
	Rarity convertToRarity(String value) {
		return Rarity.valueOf(value);
	}

	@TabCompleterFor(Rarity.class)
	List<String> tabCompleteRarity(String filter) {
		return Arrays.stream(Rarity.values())
				.filter(rarity -> rarity.name().toLowerCase().startsWith(filter.toLowerCase()))
				.map(Enum::name)
				.collect(Collectors.toList());
	}

	@ConverterFor(Condition.class)
	Condition convertToCondition(String value) {
		return Condition.valueOf(value);
	}

	@TabCompleterFor(Condition.class)
	List<String> tabCompleteCondition(String filter) {
		return Arrays.stream(Condition.values())
				.filter(condition -> condition.name().toLowerCase().startsWith(filter.toLowerCase()))
				.map(Enum::name)
				.collect(Collectors.toList());
	}
}
