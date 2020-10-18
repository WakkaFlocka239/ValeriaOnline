package me.wakka.valeriaonline.features.commands;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Cooldown;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.Time;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@NoArgsConstructor
@Cooldown(value = @Cooldown.Part(value = Time.HOUR, x = 6), bypass = "group.staff")
public class RecipeBookCommand extends CustomCommand implements Listener {

	public RecipeBookCommand(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("[player]")
	void recipeBook(@Arg("self") Player player) {
		String name = player.getName();
		runCommandAsOp(player, "customcrafting:customcrafting give " + name + " customcrafting:knowledge_book");
	}

	@EventHandler
	public void onPlayerFirstJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPlayedBefore())
			recipeBook(event.getPlayer());
	}
}
