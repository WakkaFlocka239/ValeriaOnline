//package me.wakka.valeriaonline.features.discord.commands;
//
//import com.jagrosh.jdautilities.command.Command;
//import com.jagrosh.jdautilities.command.CommandEvent;
//import me.wakka.valeriaonline.features.discord.Bot;
//import me.wakka.valeriaonline.features.discord.Bot.HandledBy;
//import me.wakka.valeriaonline.features.discord.DiscordId.Channel;
//import me.wakka.valeriaonline.features.discord.DiscordId.Role;
//
//import static me.wakka.valeriaonline.utils.Utils.runCommandAsConsole;
//
//@HandledBy(Bot.RELAY)
//public class ClearChatDiscordCommand extends Command {
//
//	public ClearChatDiscordCommand() {
//		this.name = "clearchat";
//		this.aliases = new String[]{"cc"};
//		this.requiredRole = Role.STAFF.name();
//		this.guildOnly = true;
//	}
//
//	protected void execute(CommandEvent event) {
//		if (!event.getChannel().getId().equals(Channel.STAFF_BRIDGE.getId()))
//			return;
//
//		runCommandAsConsole("clearchat");
//	}
//
//
//}
