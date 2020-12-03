//package me.wakka.valeriaonline.features.discord;
//
//import com.google.common.base.Strings;
//import lombok.NoArgsConstructor;
//import me.pugabyte.bncore.BNCore;
//import me.pugabyte.bncore.features.discord.DiscordId.Role;
//import me.pugabyte.bncore.models.discord.DiscordCaptcha;
//import me.pugabyte.bncore.models.discord.DiscordCaptcha.CaptchaResult;
//import me.pugabyte.bncore.models.discord.DiscordCaptchaService;
//import me.pugabyte.bncore.models.discord.DiscordService;
//import me.pugabyte.bncore.models.discord.DiscordUser;
//import me.pugabyte.bncore.utils.Tasks;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.PrivateChannel;
//import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
//import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//
//import javax.annotation.Nonnull;
//
//@NoArgsConstructor
//public class DiscordCaptchaListener extends ListenerAdapter {
//
//	@Override
//	public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
//		Tasks.async(() -> {
//			final String id = event.getUser().getId();
//
//			DiscordCaptchaService captchaService = new DiscordCaptchaService();
//			DiscordCaptcha captcha = captchaService.get();
//			CaptchaResult result = captcha.check(id);
//
//			if (result == CaptchaResult.CONFIRMED) {
//				Tasks.waitAsync(1, () -> {
//					Discord.addRole(id, Role.NERD);
//					DiscordUser user = new DiscordService().getFromUserId(id);
//					if (user != null && !Strings.isNullOrEmpty(user.getUuid()))
//						Discord.addRole(id, Role.VERIFIED);
//				});
//
//				return;
//			}
//
//			Discord.staffLog("**[Captcha]** " + Discord.getName(event.getMember()) + " - Requiring verification");
//			captcha.require(id);
//			captchaService.save(captcha);
//		});
//	}
//
//	@Override
//	public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
//		Tasks.async(() -> {
//			if (event.getUser() == null) {
//				BNCore.log("[Captcha] Received reaction from null user");
//				return;
//			}
//
//			final String id = event.getUser().getId();
//			DiscordCaptchaService captchaService = new DiscordCaptchaService();
//			DiscordCaptcha captcha = captchaService.get();
//
//			CaptchaResult result = captcha.check(id);
//			if (result == CaptchaResult.UNCONFIRMED) {
//				captcha.confirm(id);
//
//				Member member = Discord.getGuild().retrieveMemberById(id).complete();
//				PrivateChannel complete = event.getUser().openPrivateChannel().complete();
//
//				if (member == null) {
//					Message message = complete.getHistory().getMessageById(event.getMessageId());
//					if (message == null)
//						BNCore.warn("[Captcha] Could not find original message");
//					else
//						message.editMessage("Account confirmed. You may now join the server again: discord.gg/bearnation").queue();
//				} else
//					complete.sendMessage("Account confirmed, thank you!").queue();
//
//				captchaService.save(captcha);
//			}
//		});
//	}
//}
