package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Permission("procosmetics.open")
public class CosmeticCommand extends CustomCommand {
	public CosmeticCommand(CommandEvent event) {
		super(event);
	}

	@Path("[menu]")
	void open(@Arg("MAIN") CosmeticMenuType menuType) {
		runCommandAsOp("procosmetics:cosmetic open " + menuType.name().toLowerCase());
	}

	public enum CosmeticMenuType {
		MAIN,
		PARTICLE_EFFECTS,
		ARROW_EFFECTS,
		DEATH_EFFECTS,
		BALLOONS,
		MUSIC,
		GADGETS,
		EMOTES,
		MOUNTS,
		STATUSES,
		PETS,
		MINIATURES,
		BANNERS,
		MORPHS
	}

	@ConverterFor(CosmeticMenuType.class)
	CosmeticMenuType convertToCosmeticMenuType(String value) {
		return CosmeticMenuType.valueOf(value);
	}

	@TabCompleterFor(CosmeticMenuType.class)
	List<String> tabCompleteCosmeticMenuType(String filter) {
		return Arrays.stream(CosmeticMenuType.values())
//				.filter(menuType -> menuType.name().toLowerCase().startsWith(filter.toLowerCase()))
				.map(menuType -> menuType.name().toLowerCase())
				.collect(Collectors.toList());
	}
}
