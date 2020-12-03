//package me.wakka.valeriaonline.features.discord.commands;
//
//import com.jagrosh.jdautilities.command.Command;
//import com.jagrosh.jdautilities.command.CommandEvent;
//import me.wakka.valeriaonline.features.discord.Bot;
//import me.wakka.valeriaonline.features.discord.Bot.HandledBy;
//import me.wakka.valeriaonline.features.discord.DiscordId.Channel;
//import me.wakka.valeriaonline.features.discord.DiscordId.Role;
//import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
//import me.wakka.valeriaonline.models.discord.DiscordService;
//import me.wakka.valeriaonline.models.discord.DiscordUser;
//import me.wakka.valeriaonline.utils.Tasks;
//import me.wakka.valeriaonline.utils.Utils;
//import org.bukkit.OfflinePlayer;
//
//import static me.wakka.valeriaonline.utils.StringUtils.stripColor;
//
//@HandledBy(Bot.RELAY)
//public class TicketsDiscordCommand extends Command {
//
//	public TicketsDiscordCommand() {
//		this.name = "tickets";
//		this.requiredRole = Role.STAFF.name();
//		this.guildOnly = true;
//	}
//
//	protected void execute(CommandEvent event) {
//		if (!event.getChannel().getId().equals(Channel.STAFF_BRIDGE.getId()))
//			return;
//
//		Tasks.async(() -> {
//			final String PREFIX = "**[Tickets]** ";
//			try {
//				DiscordUser user = new DiscordService().checkVerified(event.getAuthor().getId());
//				OfflinePlayer player = Utils.getPlayer(user.getUuid());
//
//				String[] args = event.getArgs().split(" ");
//				if (args.length == 0 || !args[0].toLowerCase().matches("(view|close|reopen)"))
//					throw new InvalidInputException("Correct usage: `/tickets <view|close|reopen> <ticketId>`");
//
//				String id = args[1];
//				if (!Utils.isInt(id))
//					throw new InvalidInputException("Ticket ID must be a number");
//
//				final String nl = System.lineSeparator();
//				final TicketService service = new TicketService();
//				final Ticket ticket = service.get(Integer.parseInt(id));
//
//				switch (args[0].toLowerCase()) {
//					case "view":
//						String message = PREFIX + "**#" + ticket.getId() + "** ";
//						message += ticket.isOpen() ? "(Open)" : "(Closed)";
//						message += nl + "**Owner:** " + ticket.getOwnerName();
//						message += nl + "**When:** " + ticket.getTimespan() + " ago";
//						message += nl + "**Description:** " + ticket.getDescription();
//						event.reply(message);
//						break;
//					case "close": {
//						if (!ticket.isOpen())
//							throw new InvalidInputException("Ticket already closed");
//
//						ticket.setOpen(false);
//						service.save(ticket);
//
//						Tickets.broadcast(ticket,null, player.getName() + " closed ticket #" + ticket.getId());
//						break;
//					}
//					case "reopen": {
//						if (ticket.isOpen())
//							throw new InvalidInputException("Ticket already open");
//
//						ticket.setOpen(true);
//						service.save(ticket);
//
//						Tickets.broadcast(ticket,null, player.getName() + " reopened ticket #" + ticket.getId());
//						break;
//					}
//				}
//			} catch (InvalidInputException ex) {
//				event.reply(stripColor(PREFIX + ex.getMessage()));
//			} catch (Exception ex) {
//				event.reply(PREFIX + "An internal error occurred while attempting to execute this command");
//				ex.printStackTrace();
//			}
//		});
//	}
//
//
//}
