//package me.wakka.valeriaonline.features.discord.commands;
//
//import com.jagrosh.jdautilities.command.Command;
//import com.jagrosh.jdautilities.command.CommandEvent;
//import me.wakka.valeriaonline.features.discord.Bot;
//import me.wakka.valeriaonline.features.discord.Bot.HandledBy;
//import me.wakka.valeriaonline.features.discord.Discord;
//import me.wakka.valeriaonline.features.discord.DiscordId.Role;
//import me.wakka.valeriaonline.framework.exceptions.CustomException;
//import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
//import me.wakka.valeriaonline.utils.Tasks;
//
//import static me.wakka.valeriaonline.utils.StringUtils.camelCase;
//import static me.wakka.valeriaonline.utils.StringUtils.stripColor;
//
//@HandledBy(Bot.KODA)
//public class UnsubscribeDiscordCommand extends Command {
//
//	public UnsubscribeDiscordCommand() {
//		this.name = "unsubscribe";
//		this.guildOnly = true;
//	}
//
//	protected void execute(CommandEvent event) {
//		Tasks.async(() -> {
//			try {
//				String[] args = event.getArgs().split(" ");
//				if (args.length == 0)
//					throw new InvalidInputException("Correct usage: `/unsubscribe <role>`");
//
//				Role role = getRole(args[0]);
//				if (role == null)
//					throw new InvalidInputException("Unknown role, available options are `minigames` and `movienight`");
//
//				Discord.removeRole(event.getAuthor().getId(), role);
//				event.reply(event.getAuthor().getAsMention() + " You have unsubscribed from " + camelCase(role.name()));
//
//			} catch (Exception ex) {
//				event.reply(stripColor(ex.getMessage()));
//				if (!(ex instanceof CustomException))
//					ex.printStackTrace();
//			}
//		});
//	}
//
//}
