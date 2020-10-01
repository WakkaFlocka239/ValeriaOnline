package me.wakka.valeriaonline.features.teleportrequests;

import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Redirects;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.framework.exceptions.PlayerNotOnlineException;
import me.wakka.valeriaonline.utils.ConfigUtils;
import me.wakka.valeriaonline.utils.MenuUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@NoArgsConstructor
@Aliases({"tpa", "tpask"})
@Redirects.Redirect(from = {"/tp2p", "/etp2p", "/etpa", "/etpask", "/etp"}, to = "/teleport")
@Redirects.Redirect(from = {"/tpno", "/etpno", "/tpdeny", "/etpdeny"}, to = "/teleport deny")
@Redirects.Redirect(from = {"/tpyes", "/etpyes", "/tpaccept", "/etpaccept"}, to = "/teleport accept")
@Redirects.Redirect(from = {"/tpcancel", "/etpcancel", "/tpacancel", "/etpacancel"}, to = "/teleport cancel")
public class TeleportCommand extends CustomCommand {
	public static final String PREFIX = StringUtils.getPrefix("Teleport");
	public static final double COST = ConfigUtils.getSettings().getDouble("teleportCost");

	public TeleportCommand(CommandEvent event) {
		super(event);
	}


	@Path("<player>")
	void teleport(Player target) {
		if (target == player())
			error("You cannot tp to yourself");

		Request request = new Request(player(), target, Request.TeleportType.TELEPORT);
		MenuUtils.ConfirmationMenu.builder()
				.title("Teleport to " + target.getName())
				.confirmLore("&cCosts: &6" + COST + " Crowns, if accepted")
				.onConfirm(e -> {
					Requests.add(request);

					send(player(), json(PREFIX + "&dtp &7request sent to &d" + target.getName() + "&7. ")
							.next("&cClick to cancel")
							.command("/teleport cancel"));

					send(target, PREFIX + "&d" + player().getName() + " &7is asking to tp to you");
					send(target, json("&7  Click one  ||  &a&lAccept")
							.command("/teleport accept")
							.hover("&dClick &7to accept")
							.group()
							.next("  &7||  &7")
							.group()
							.next("&c&lDeny")
							.command("/teleport deny")
							.hover("&dClick &7to deny.")
							.group()
							.next("&7  ||"));
				})
				.open(player());
	}

	@Path("confirm")
	void tphereConfirm(String type) {
		Request request = Requests.getByReceiver(player());
		OfflinePlayer sender = request.getSender();

		if (sender == null) {
			error(player(), PREFIX + "&cYou do not have any pending tp requests");
			return;
		}

		MenuUtils.ConfirmationMenu.builder()
				.title("Teleport to " + sender.getName())
				.confirmLore("&cCosts: &6" + COST + " Crowns, if accepted")
				.onConfirm(e -> runCommand("teleport accept"))
				.open(player());
	}

	@Path("cancel")
	void cancel() {
		cancel(player());
	}

	@Path("accept")
	void accept() {
		accept(player());
	}

	@Path("deny")
	void deny() {
		deny(player());
	}


	public void cancel(Player sender) {
		Request request = Requests.getBySender(sender);
		if (request == null || request.isExpired()) {
			error(sender, PREFIX + "&cYou do not have any pending tp requests");
			return;
		}

		request.setExpired(true);
		Requests.remove(request);

		OfflinePlayer toPlayer = request.getReceiver();
		OfflinePlayer fromPlayer = request.getSender();

		if (request.getType() == Request.TeleportType.TELEPORT_HERE) {
			toPlayer = request.getSender();
			fromPlayer = request.getReceiver();
		}

		if (!fromPlayer.isOnline())
			throw new PlayerNotOnlineException(fromPlayer);

		if (request.getType() == Request.TeleportType.TELEPORT) {
			send(toPlayer.getPlayer(), PREFIX + "&d" + fromPlayer.getName() + " &7canceled their tp request");
			send(fromPlayer.getPlayer(), PREFIX + "&7You canceled your tp request to &d" + toPlayer.getName());
		} else {
			send(fromPlayer.getPlayer(), PREFIX + "&d" + toPlayer.getName() + " &7canceled their tphere request");
			send(toPlayer.getPlayer(), PREFIX + "&7You canceled your tphere request to &d" + fromPlayer.getName());
		}

	}

	public void accept(Player receiver) {
		Request request = Requests.getByReceiver(receiver);
		if (request == null || request.isExpired()) {
			error(receiver, PREFIX + "&cYou do not have any pending tp requests");
			return;
		}

		request.setExpired(true);
		Requests.remove(request);

		OfflinePlayer toPlayer = request.getReceiver();
		OfflinePlayer fromPlayer = request.getSender();
		if (request.getType() == Request.TeleportType.TELEPORT_HERE) {
			toPlayer = request.getSender();
			fromPlayer = request.getReceiver();
		}

		if (!fromPlayer.isOnline() || fromPlayer.getPlayer() == null)
			throw new PlayerNotOnlineException(fromPlayer);

		if (!toPlayer.isOnline() || toPlayer.getPlayer() == null)
			throw new PlayerNotOnlineException(toPlayer);

		if (!ValeriaOnline.getEcon().has(fromPlayer, COST))
			error(fromPlayer, PREFIX + "&cYou don't have enough Crowns!");

		if (request.getType() == Request.TeleportType.TELEPORT) {
			fromPlayer.getPlayer().teleport(toPlayer.getPlayer());

			send(toPlayer.getPlayer(), PREFIX + "&7You accepted &d" + fromPlayer.getName() + "'s &7tp request");
			send(fromPlayer.getPlayer(), PREFIX + "&d" + toPlayer.getName() + " &7accepted your tp request");

		} else {
			fromPlayer.getPlayer().teleport(request.getLocation());

			send(fromPlayer.getPlayer(), PREFIX + "&7You accepted &d" + toPlayer.getName() + "'s &7tphere request");
			send(toPlayer.getPlayer(), PREFIX + "&d" + fromPlayer.getName() + " &7accepted your tphere request");

		}

		Utils.withdraw(fromPlayer, COST, PREFIX);
	}

	public void deny(Player receiver) {
		Request request = Requests.getByReceiver(receiver);
		if (request == null || request.isExpired()) {
			error(receiver, PREFIX + "&cYou do not have any pending tp requests");
			return;
		}

		request.setExpired(true);
		Requests.remove(request);

		OfflinePlayer toPlayer = request.getReceiver();
		OfflinePlayer fromPlayer = request.getSender();
		if (request.getType() == Request.TeleportType.TELEPORT_HERE) {
			toPlayer = request.getSender();
			fromPlayer = request.getReceiver();
		}

		if (!fromPlayer.isOnline())
			throw new PlayerNotOnlineException(fromPlayer);

		if (request.getType() == Request.TeleportType.TELEPORT) {
			send(toPlayer.getPlayer(), PREFIX + "&7You denied &d" + fromPlayer.getName() + "'s &7tp request");
			send(fromPlayer.getPlayer(), PREFIX + "&d" + toPlayer.getName() + " &7denied your tp request");
		} else {
			send(fromPlayer.getPlayer(), PREFIX + "&7You denied &d" + toPlayer.getName() + "'s &7tphere request");
			send(toPlayer.getPlayer(), PREFIX + "&d" + fromPlayer.getName() + " &7denied your tphere request");
		}
	}
}
