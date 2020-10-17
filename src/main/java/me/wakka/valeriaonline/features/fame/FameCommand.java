package me.wakka.valeriaonline.features.fame;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.fame.menu.FameMenu;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.models.fame.PrefixTag;
import me.wakka.valeriaonline.utils.Tasks;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FameCommand extends CustomCommand {
	FameService service = new FameService();

	public FameCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void openMenu() {
		FameMenu.openMain(player());
	}

	@Path("giveTag <player> <tag>")
	@Permission("group.admin")
	void giveTag(OfflinePlayer player, PrefixTag tag) {
		ValeriaOnline.getPerms().playerAdd(player.getPlayer(), tag.getPermission());
		send("Gave " + player.getName() + " tag: " + tag.getName());
		if (player.isOnline())
			PrefixTags.obtainedMessage(player, Collections.singletonList(tag));
	}

	@Path("give <player> <amount> <type>")
	@Permission("group.admin")
	void giveFame(OfflinePlayer player, int amount, FameService.FameType type) {
		Fame fame = service.get(player);
		fame.addPoints(type, amount);
		service.save(fame);

		Tasks.wait(5, () -> PrefixTags.updatePrefixTags(player));
	}

	@Path("reset <player>")
	@Permission("group.admin")
	void reset(OfflinePlayer player) {
		Fame fame = service.get(player);
		service.delete(fame);
		for (PrefixTag activeTag : PrefixTags.getActiveTags()) {
			ValeriaOnline.getPerms().playerRemove(player.getPlayer(), activeTag.getPermission());
		}
	}

	@Path("clearDatabase")
	@Permission("group.sev")
	void clearDatabase() {
		FameService service = new FameService();
		service.deleteAll();
	}


	@ConverterFor(PrefixTag.class)
	PrefixTag convertToPrefixTag(String value) {
		Optional<PrefixTag> prefixTag = PrefixTags.getActiveTags().stream()
				.filter(tag -> tag.getName().equalsIgnoreCase(value))
				.findFirst();
		if (!prefixTag.isPresent()) {
			error("Tag " + value + " not found");
			return null;
		}

		return prefixTag.get();
	}

	@TabCompleterFor(PrefixTag.class)
	List<String> tabCompletePrefixTag(String filter) {
		return PrefixTags.getActiveTags().stream()
				.filter(tag -> tag.getName().toLowerCase().startsWith(filter.toLowerCase()))
				.map(PrefixTag::getName)
				.collect(Collectors.toList());
	}
}
