package me.wakka.valeriaonline.features.prefixtags;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.fame.PrefixTag;
import me.wakka.valeriaonline.utils.StringUtils;

@Aliases("tags")
@Permission("group.staff")
public class PrefixTagsCommand extends CustomCommand {

	public PrefixTagsCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void list() {
		send("All Tags:");
		for (PrefixTag activeTag : PrefixTags.getActiveTags()) {
			String name = activeTag.getName();
			String format = StringUtils.colorize(activeTag.getFormat());
			int cost = activeTag.getCost();
			String permission = activeTag.getPermission();
			String description = StringUtils.colorize(activeTag.getDescription());

			send(json(" - " + name)
					.hover("Format: " + format + "\n"
							+ "Cost: " + cost + "\n"
							+ "Perm: " + permission + "\n"
							+ "Desc: " + description));
		}
		send("");
	}
}
