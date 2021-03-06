package me.wakka.valeriaonline.features.scoreboard;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.features.menus.BookBuilder.WrittenBookMenu;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.HideFromHelp;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleteIgnore;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.models.scoreboard.ScoreboardService;
import me.wakka.valeriaonline.models.scoreboard.ScoreboardUser;
import me.wakka.valeriaonline.utils.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

import java.util.stream.Collectors;

@NoArgsConstructor
@Aliases({"status", "sidebar", "sb", "featherboard"})
@Permission("group.dev") // TODO
//@Disabled
public class ScoreboardCommand extends CustomCommand implements Listener {
	private final ScoreboardService service = new ScoreboardService();
	private ScoreboardUser user;

//	static {
//		Bukkit.getOnlinePlayers().forEach(player -> {
//			ScoreboardUser user = new ScoreboardService().get(player);
//			if (user.isActive())
//				user.on();
//		});
//	}

	public ScoreboardCommand(@NonNull CommandEvent event) {
		super(event);
		user = service.get(player());
	}

	@Description("Turn the scoreboard on or off")
	@Path("[on/off]")
	void toggle(Boolean enable) {
		user.setActive((enable != null ? enable : !user.isActive()));
		if (!user.isActive())
			user.off();
		else {
			if (user.getLines().isEmpty())
				user.setLines(ScoreboardLine.getDefaultLines(player()));
			user.on();
		}
		service.save(user);
	}

	@Description("Reset settings to default")
	@Path("reset")
	void reset() {
		user.setActive(true);
		user.setLines(ScoreboardLine.getDefaultLines(player()));
		user.on();
		service.save(user);
	}

	@Description("Control which lines you want to see")
	@Path("edit")
	void book() {
		WrittenBookMenu builder = new WrittenBookMenu();

		int index = 0;
		JsonBuilder json = new JsonBuilder();
		for (ScoreboardLine line : ScoreboardLine.values()) {
			if (!line.isOptional()) continue;
			if (!line.hasPermission(player())) continue;
			json.next((user.getLines().containsKey(line) && user.getLines().get(line)) ? "&a✔" : "&c✗")
					.command("/scoreboard edit toggle " + line.name().toLowerCase())
					.hover("&eClick to toggle")
					.next(" ").group();
			json.next("&3" + camelCase(line))
					.hover(line.render(player()));
			json.newline();

			if (++index % 14 == 0) {
				builder.addPage(json);
				json = new JsonBuilder();
			}
		}

		builder.addPage(json).open(player());
	}

	@HideFromHelp
	@TabCompleteIgnore
	@Path("edit toggle <type> [enable]")
	void toggle(ScoreboardLine line, Boolean enable) {
		if (enable == null)
			enable = !user.getLines().containsKey(line) || !user.getLines().get(line);
		user.getLines().put(line, enable);
		if (!enable)
			user.remove(line);
		user.startTasks();
		service.save(user);
		book();
	}

	@Permission("group.staff")
	@Path("list")
	void list() {
		String collect = Bukkit.getOnlinePlayers().stream()
				.map(player -> (ScoreboardUser) new ScoreboardService().get(player))
				.filter(ScoreboardUser::isActive)
				.map(user -> user.getPlayer().getName())
				.collect(Collectors.joining("&3, &e"));
		send(PREFIX + "Active scoreboards: ");
		send("&e" + collect);
	}

	@Permission("group.staff")
	@Path("view <player>")
	void view(OfflinePlayer player) {
		send(service.get(player).toString());
	}

	@Permission("group.dev")
	@Path("resetAll")
	void resetAll() {
		service.deleteAll();
		send("Reset all scoreboards");
	}

//	@EventHandler
//	public void onJoin(PlayerJoinEvent event) {
//		ScoreboardService service = new ScoreboardService();
//		ScoreboardUser user = service.get(event.getPlayer());
//		if (user.isActive())
//			user.on();
//	}
//
//	@EventHandler
//	public void onQuit(PlayerQuitEvent event) {
//		ScoreboardService service = new ScoreboardService();
//		ScoreboardUser user = service.get(event.getPlayer());
//		user.pause();
//	}

//	@EventHandler
//	public void onMatchQuit(MatchQuitEvent event) {
//		ScoreboardService service = new ScoreboardService();
//		ScoreboardUser user = service.get(event.getMinigamer().getPlayer());
//		if (user.isActive() && user.getOfflinePlayer().isOnline())
//			user.on();
//	}

//	@EventHandler
//	public void onMatchEnd(MatchEndEvent event) {
//		ScoreboardService service = new ScoreboardService();
//		event.getMatch().getMinigamers().forEach(minigamer -> {
//			ScoreboardUser user = service.get(minigamer.getPlayer());
//			if (user.isActive() && user.getOfflinePlayer().isOnline())
//				user.on();
//		});
//	}

//	@EventHandler
//	public void onMatchJoin(MatchJoinEvent event) {
//		ScoreboardService service = new ScoreboardService();
//		ScoreboardUser user = service.get(event.getMinigamer().getPlayer());
//		user.pause();
//	}

//	@EventHandler
//	public void onMcMMOScoreboardEnd(McMMOScoreboardRevertEvent event) {
//		ScoreboardService service = new ScoreboardService();
//		ScoreboardUser user = service.get(event.getTargetPlayer());
//		if (user.isActive() && user.getOfflinePlayer().isOnline())
//			user.on();
//	}

}
