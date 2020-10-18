package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.features.chat.Chat;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.wakka.valeriaonline.utils.StringUtils.colorize;

public class JoinQuit implements Listener {
	private static final String firstJoinMessage = "&7Welcome &b[player] &7to Valeria Online!";
	private static final String joinMessage = "&7[&a&l+&7] &b[player] &7joined the server.";
	private static final String quitMessage = "&7[&c&l-&7] &b[player] &7left the server.";
	private static final String firstJoinTitle = "&eWelcome";
	private static final String firstJoinSubtitle = "&b[player] &7to Valeria Online";
	private static final String joinTitle = "&eWelcome Back";
	private static final String joinSubtitle = "&b[player] &7to Valeria Online";
	private static final String line = "&7&m                                                  ";

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore())
			firstJoin(player);
		else if (!Utils.isVanished(player))
			join(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!Utils.isVanished(player))
			quit(player);
	}

	private void firstJoin(Player player) {
		Chat.broadcast(replacePlayer(player, firstJoinMessage));
		String title = colorize(replacePlayer(player, firstJoinTitle));
		String subtitle = colorize(replacePlayer(player, firstJoinSubtitle));

		Tasks.wait(Time.SECOND.x(2), () -> {
			player.sendTitle(title, subtitle, 30, 60, 30);
			motd(player);
		});

	}

	private void join(Player player) {
		Chat.broadcast(replacePlayer(player, joinMessage));
		String title = colorize(replacePlayer(player, joinTitle));
		String subtitle = colorize(replacePlayer(player, joinSubtitle));

		Tasks.wait(Time.SECOND.x(2), () -> {
			player.sendTitle(title, subtitle, 30, 60, 30);
			motd(player);
		});
	}

	private void quit(Player player) {
		Chat.broadcast(replacePlayer(player, quitMessage));
	}

	private String replacePlayer(Player player, String message) {
		return message.replaceAll("\\[player]", player.getName());
	}

	private void motd(Player player) {
		int online = (int) Bukkit.getOnlinePlayers().stream().filter(_player -> !Utils.isVanished(_player)).count();
		Utils.send(player,
				line + "\n"
						+ replacePlayer(player, "&7Welcome to &bValeria Online &7[player]&7!") + "\n \n"
						+ "&7There are currently &b" + online + " &7players online!" + "\n \n"
						+ "&7Please remember to Vote by using &b/Vote&7!" + "\n \n"
						+ "&7If you have any questions please join our Discord by using &b/Discord&7!" + "\n"
						+ line
		);
	}
}
