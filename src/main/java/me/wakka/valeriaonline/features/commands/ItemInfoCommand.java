package me.wakka.valeriaonline.features.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.wakka.valeriaonline.Utils.StringUtils;
import me.wakka.valeriaonline.Utils.Utils;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.wakka.valeriaonline.Utils.StringUtils.colorize;
import static me.wakka.valeriaonline.Utils.Utils.isNullOrAir;


@Aliases({"nbt"})
@Permission("group.staff")
public class ItemInfoCommand extends CustomCommand {

	public ItemInfoCommand(CommandEvent event) {
		super(event);
	}

	@Path("[material]")
	@Description("Get material and nbt from held item")
	void itemInfo(Material material) {
		ItemStack tool = Utils.getTool(player());

		if (material != null)
			tool = new ItemStack(material);
		else if (isNullOrAir(tool)) {
			error("Must be holding an item");
			return;
		}

		material = tool.getType();

		send("");
		send("Material: " + material + " (" + material.ordinal() + ")");

		if (!isNullOrAir(tool)) {
			final String nbtString = getNBTString(tool);

			if (nbtString != null && !"{}".equals(nbtString)) {
					send("NBT: " + colorize(nbtString));
					send(json("&e&l[Click to Copy NBT]").suggest(nbtString));
			}
		}
	}

	private String getNBTString(ItemStack itemStack) {
		NBTItem nbtItem = new NBTItem(itemStack);
		String nbtString = null;

		if (nbtItem.hasNBTData()) {
			nbtString = nbtItem.toString();
			nbtString = StringUtils.stripColor(nbtString);
		}

		if (nbtString != null) {
			// highlight keywords
			nbtString = nbtString.replaceAll("run_command", "&crun_command&f");
			nbtString = nbtString.replaceAll("suggest_command", "&csuggest_command&f");
			nbtString = nbtString.replaceAll("insert_command", "&cinsert_command&f");
			nbtString = nbtString.replaceAll("open_url", "&copen_url&f");
			nbtString = nbtString.replaceAll("open_file", "&copen_file&f");

			nbtString = nbtString.replaceAll("clickEvent", "&cclickEvent&f");
			nbtString = nbtString.replaceAll("hoverEvent", "&choverEvent&f");

			// clean up of garbage
			nbtString = nbtString.replaceAll("\"\"", "");
			nbtString = nbtString.replaceAll("\\{\"\"text\"\":\"\"\\n\"\"},", "");
			nbtString = nbtString.replaceAll("\\n", "");
			nbtString = nbtString.replaceAll("\\\\", "");
		}
		return nbtString;
	}

}
