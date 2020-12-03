//package me.wakka.valeriaonline.features.discord.commands;
//
//import com.jagrosh.jdautilities.command.Command;
//import com.jagrosh.jdautilities.command.CommandEvent;
//import me.wakka.valeriaonline.ValeriaOnline;
//import me.wakka.valeriaonline.features.discord.Bot;
//import me.wakka.valeriaonline.features.discord.Bot.HandledBy;
//import me.wakka.valeriaonline.framework.exceptions.CustomException;
//import me.wakka.valeriaonline.models.discord.DiscordService;
//import me.wakka.valeriaonline.models.discord.DiscordUser;
//import me.wakka.valeriaonline.utils.Tasks;
//import me.wakka.valeriaonline.utils.Utils;
//import org.bukkit.OfflinePlayer;
//
//import java.text.NumberFormat;
//
//import static com.google.common.base.Strings.isNullOrEmpty;
//import static me.wakka.valeriaonline.utils.StringUtils.stripColor;
//
//@HandledBy(Bot.KODA)
//public class BalanceDiscordCommand extends Command {
//
//	public BalanceDiscordCommand() {
//		this.name = "balance";
//		this.aliases = new String[]{"bal", "money"};
//	}
//
//	protected void execute(CommandEvent event) {
//		Tasks.async(() -> {
//			try {
//				DiscordUser user = new DiscordService().checkVerified(event.getAuthor().getId());
//				OfflinePlayer player = Utils.getPlayer(user.getUuid());
//
//				String[] args = event.getArgs().split(" ");
//				if (args.length > 0 && !isNullOrEmpty(args[0]))
//					player = Utils.getPlayer(args[0]);
//
//				String formatted = NumberFormat.getCurrencyInstance().format(ValeriaOnline.getEcon().getBalance(player));
//				boolean isSelf = user.getUuid().equals(player.getUniqueId().toString());
//				event.reply("Balance" + (isSelf ? "" : " of " + player.getName()) + ": " + formatted);
//			} catch (Exception ex) {
//				event.reply(stripColor(ex.getMessage()));
//				if (!(ex instanceof CustomException))
//					ex.printStackTrace();
//			}
//		});
//	}
//
//}
