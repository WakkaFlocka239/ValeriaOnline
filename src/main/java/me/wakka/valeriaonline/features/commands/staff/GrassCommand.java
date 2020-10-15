package me.wakka.valeriaonline.features.commands.staff;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.BlockUtils;
import me.wakka.valeriaonline.utils.MaterialTag;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

@Permission("group.admin")
public class GrassCommand extends CustomCommand {
	public GrassCommand(CommandEvent event) {
		super(event);
	}

	@Path("[radius]")
	void run(@Arg("5") int radius) {
		Tasks.async(() -> {
			final int finalRadius = Math.max(1, Math.min(radius, 25));
			List<Block> placeGrassOn = new ArrayList<>();
			List<Block> blocks = BlockUtils.getBlocksInRadius(player().getLocation(), finalRadius);

			for (Block block : blocks) {
				Block above = block.getRelative(BlockFace.UP);
				if (MaterialTag.DIRTS.isTagged(block.getType()) && Utils.isNullOrAir(above))
					placeGrassOn.add(above);
			}

			Tasks.sync(() -> {
				for (Block block : placeGrassOn) {
					block.setType(Material.GRASS);
				}
			});
		});
	}
}
