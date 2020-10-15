package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Cooldown;
import me.wakka.valeriaonline.framework.commands.models.annotations.Fallback;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Time;

@Fallback("essentials")
@Permission("essentials.back")
@Cooldown(value = @Cooldown.Part(value = Time.MINUTE, x = 2), bypass = "group.staff")
public class BackCommand extends CustomCommand {

	public BackCommand(CommandEvent event) {
		super(event);
	}

	@Path()
	void back() {
		fallback();
	}
}
