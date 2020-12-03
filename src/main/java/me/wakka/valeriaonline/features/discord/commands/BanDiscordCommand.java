//package me.wakka.valeriaonline.features.discord.commands;
//
//import com.google.common.base.Strings;
//import com.jagrosh.jdautilities.command.Command;
//import com.jagrosh.jdautilities.command.CommandEvent;
//import me.wakka.valeriaonline.features.discord.Bot;
//import me.wakka.valeriaonline.features.discord.Bot.HandledBy;
//import me.wakka.valeriaonline.features.discord.DiscordId.Channel;
//import me.wakka.valeriaonline.features.discord.DiscordId.Role;
//import me.wakka.valeriaonline.models.discord.DiscordService;
//import me.wakka.valeriaonline.models.discord.DiscordUser;
//import me.wakka.valeriaonline.utils.Tasks;
//import me.wakka.valeriaonline.utils.Utils;
//
//import static me.wakka.valeriaonline.utils.StringUtils.trimFirst;
//import static me.wakka.valeriaonline.utils.Utils.runCommandAsConsole;
//
//@HandledBy(Bot.RELAY)
//public class BanDiscordCommand extends Command {
//
//	public BanDiscordCommand() {
//		this.name = "ban";
//		this.aliases = new String[]{"tempban", "unban", "banip", "ipban", "kick", "warn", "unwarn", "mute", "unmute"};
//		this.requiredRole = Role.STAFF.name();
//		this.guildOnly = true;
//	}
//
//	protected void execute(CommandEvent event) {
//		if (!event.getChannel().getId().equals(Channel.STAFF_BRIDGE.getId()))
//			return;
//
//		Tasks.async(() -> {
//			DiscordUser user = new DiscordService().getFromUserId(event.getAuthor().getId());
//			if (!Strings.isNullOrEmpty(user.getUserId()))
//				Tasks.sync(() ->
//						runCommandAsConsole(trimFirst(event.getMessage().getContentRaw() + " --sender=" + Utils.getPlayer(user.getUuid()).getName())));
//		});
//	}
//
//
//}
