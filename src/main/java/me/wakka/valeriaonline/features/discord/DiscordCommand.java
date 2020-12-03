//package me.wakka.valeriaonline.features.discord;
//
//import lombok.NonNull;
//import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
//import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
//import me.wakka.valeriaonline.framework.commands.models.annotations.Async;
//import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
//import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
//import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
//import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
//import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
//import me.wakka.valeriaonline.framework.features.Features;
//import me.wakka.valeriaonline.models.discord.DiscordService;
//import me.wakka.valeriaonline.models.discord.DiscordUser;
//import me.wakka.valeriaonline.models.setting.Setting;
//import me.wakka.valeriaonline.models.setting.SettingService;
//import me.wakka.valeriaonline.utils.Tasks;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.Role;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.exceptions.ErrorResponseException;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class DiscordCommand extends CustomCommand {
//	DiscordService service = new DiscordService();
//	DiscordUser user;
//
//	public DiscordCommand(@NonNull CommandEvent event) {
//		super(event);
//		if (isPlayer())
//			user = service.get(player());
//	}
//
//	@Path
//	void run() {
//		send(json().next("&e" + BNSocialMediaSite.DISCORD.getUrl()));
//	}
//
//	@Path("link update roles")
//	@Permission("group.seniorstaff")
//	void updateRoles() {
//		Tasks.async(() -> {
//			Role verified = Discord.getGuild().getRoleById(DiscordId.Role.VERIFIED.getId());
//			new DiscordService().getAll().stream().filter(discordUser -> !isNullOrEmpty(discordUser.getUserId())).forEach(discordUser -> {
//				Member member = Discord.getGuild().retrieveMemberById(discordUser.getUserId()).complete();
//				if (member == null) return;
//				if (!member.getRoles().contains(verified))
//					Discord.addRole(discordUser.getUserId(), DiscordId.Role.VERIFIED);
//			});
//		});
//	}
//
//	@Path("link [code]")
//	void link(String code) {
//		if (isNullOrEmpty(code)) {
//			if (!isNullOrEmpty(user.getUserId())) {
//				User userById = Bot.KODA.jda().retrieveUserById(user.getUserId()).complete();
//				if (userById == null)
//					send(PREFIX + "Your minecraft account is linked to a Discord account, but I could not find that account. " +
//							"Are you in our discord server? &e" + BNSocialMediaSite.DISCORD.getUrl());
//				else
//					send(PREFIX + "Your minecraft account is linked to " + user.getName());
//				send(PREFIX + "You can unlink your account with &c/discord unlink");
//				return;
//			} else {
//				send(PREFIX + "Hello! Looking to &elink &3your &eDiscord &3and &eMinecraft &3accounts? Here's how:");
//				line();
//				send("&3Step 1: &eOpen our Discord server");
//				send("&3Step 2: Type &c/discord link " + player().getName() + " &3in any channel");
//				send("&3Step 3: &eCopy the command &3that appears in your &eDMs &3and &epaste it &3into Minecraft");
//			}
//		} else {
//			if (Discord.getCodes().containsKey(code)) {
//				DiscordUser newUser = Discord.getCodes().get(code);
//				if (!player().getUniqueId().toString().equals(newUser.getUuid()))
//					error("There is no pending confirmation with this account");
//
//				String name = newUser.getName();
//				String discrim = newUser.getDiscrim();
//				Bot.KODA.jda().retrieveUserById(newUser.getUserId()).complete().openPrivateChannel().complete().sendMessage("You have successfully linked your Discord account with the Minecraft account **" + player().getName() + "**").queue();
//				send(PREFIX + "You have successfully linked your Minecraft account with the Discord account &e" + name + "#" + discrim);
//				Discord.addRole(newUser.getUserId(), DiscordId.Role.VERIFIED);
//				user.setUserId(newUser.getUserId());
//				service.save(user);
//				Discord.staffLog("**" + player().getName() + "** has linked their discord account to **" + name + "#" + discrim + "**");
//				Discord.getCodes().remove(code);
//			} else
//				error("Invalid confirmation code");
//		}
//	}
//
//	@Path("unlink")
//	void unlink() {
//		if (isNullOrEmpty(user.getUserId()))
//			error("This account is not linked to any Discord account");
//
//		try {
//			User userById = Bot.KODA.jda().retrieveUserById(user.getUserId()).complete();
//			String name = user.getName();
//			String discrim = user.getDiscrim();
//
//			userById.openPrivateChannel().complete().sendMessage("This Discord account has been unlinked from the Minecraft account **" + player().getName() + "**").queue();
//			send(PREFIX + "Successfully unlinked this Minecraft account from Discord account " + name);
//			Discord.staffLog("**" + player().getName() + "** has unlinked their account from **" + name + "#" + discrim + "**");
//		} catch (ErrorResponseException ex) {
//			if (ex.getErrorCode() == 10007) {
//				send(PREFIX + "Successfully unlinked this Minecraft account from an unknown Discord account");
//				Discord.staffLog("**" + player().getName() + "** has unlinked their account from an unknown Discord account");
//			}
//		}
//
//		user.setUserId(null);
//		service.save(user);
//	}
//
//	@Path("linkStatus [player]")
//	@Permission("group.staff")
//	void linkStatus(@Arg("self") DiscordUser discordUser) {
//		send(PREFIX + "Link status of &e" + discordUser.getIngameName());
//
//		if (isNullOrEmpty(discordUser.getUserId()))
//			send(" &7- &cNot linked to any member");
//		else {
//			Member member = discordUser.getMember();
//			if (member == null)
//				send(json(" &7- &cLinked to unknown member with ID &e").next(discordUser.getUserId()).insert(discordUser.getUserId()));
//			else
//				send(json(" &7- &3Linked to member &e")
//					.next(discordUser.getNameAndDiscrim()).insert(discordUser.getNameAndDiscrim())
//					.next(" &3/ &e")
//					.next(discordUser.getUserId()).insert(discordUser.getUserId()));
//		}
//
//		if (isNullOrEmpty(discordUser.getRoleId()))
//			send(" &7- &cNot linked to any bridge role");
//		else {
//			Role role = Discord.getGuild().getRoleById(discordUser.getRoleId());
//			if (role == null)
//				send(json(" &7- &cLinked to unknown bridge role with ID &e").next(discordUser.getRoleId()).insert(discordUser.getRoleId()));
//			else
//				send(json(" &7- &3Linked to bridge role &e")
//					.next(role.getName()).insert(role.getName())
//					.next(" &3/ &e")
//					.next(role.getId()).insert(role.getId()));
//		}
//	}
//
//	@Async
//	@Path("connect")
//	@Permission("group.staff")
//	void connect() {
//		((Discord) Features.get(Discord.class)).connect();
//	}
//
//	@Path("lockdown")
//	@Permission("group.staff")
//	void lockdown() {
//		SettingService service = new SettingService();
//		Setting setting = service.get("discord", "lockdown");
//		setting.setBoolean(!setting.getBoolean());
//		service.save(setting);
//
//		send(PREFIX + "Lockdown " + (setting.getBoolean() ? "enabled, new members will be automatically kicked" : "disabled"));
//	}
//
//	@Async
//	@Path("jda dms send <id> <message...>")
//	@Permission("group.admin")
//	void jda_dms_send(String id, String message) {
//		Bot.KODA.jda().retrieveUserById(id).complete().openPrivateChannel().complete().sendMessage(message).queue();
//	}
//
//	@Async
//	@Path("jda dms view <id>")
//	@Permission("group.admin")
//	void jda_dms_view(String id) {
//		Bot.KODA.jda().retrieveUserById(id).complete().openPrivateChannel().complete().getHistory().retrievePast(50).complete().forEach(message ->
//				send(message.getContentRaw()));
//	}
//
//	@Async
//	@Path("jda dms delete <id>")
//	@Permission("group.admin")
//	void jda_dms_delete(String id) {
//		Bot.KODA.jda().retrieveUserById(id).complete().openPrivateChannel().complete().getHistory().retrievePast(50).complete().forEach(message ->
//				message.delete().queue());
//	}
//
//	@Async
//	@Path("jda getUser <id>")
//	@Permission("group.admin")
//	void jda_getUser(String id) {
//		User user = Bot.KODA.jda().retrieveUserById(id).complete();
//		if (user == null)
//			error("User is null");
//		send(user.getName() + "#" + user.getDiscriminator());
//		send("Mutual guilds: " + user.getMutualGuilds().stream().map(Guild::getName).collect(Collectors.joining(", ")));
//		send("Has Private Channel: " + user.hasPrivateChannel());
//	}
//
////	static {
////		new DiscordCaptchaService().get();
////	}
////
////	@Path("captcha debug")
////	@Permission("group.staff")
////	void debug() {
////		send(new DiscordCaptchaService().get().toString());
////	}
////
////	@Path("captcha unconfirm <id>")
////	@Permission("group.staff")
////	void unconfirm(String id) {
////		DiscordCaptchaService captchaService = new DiscordCaptchaService();
////		DiscordCaptcha captcha = captchaService.get();
////
////		User user = Bot.KODA.jda().retrieveUserById(id).complete();
////		Member member = Discord.getGuild().retrieveMemberById(id).complete();
////
////		if (user == null)
////			send(PREFIX + "&cWarning: &3User is null");
////		if (member == null)
////			send(PREFIX + "&cWarning: &3Member is null");
////
////		String name = Discord.getName(id);
////
////		if (!captcha.getConfirmed().containsKey(id))
////			error(name + " is not confirmed");
////
////		captcha.getConfirmed().remove(id);
////		captchaService.save(captcha);
////		send(PREFIX + "Unconfirmed " + name);
////	}
////
////	// TODO Restrospective confirmation checks
////	@Path("captcha info")
////	@Permission("group.staff")
////	void info() {
////		DiscordCaptcha captcha = new DiscordCaptchaService().get();
////
////		captcha.getUnconfirmed().forEach((id, date) -> {
////			String name = Discord.getName(id);
////			send("ID: " + name + " / Date: " + StringUtils.shortDateTimeFormat(date));
////		});
////	}
//
//	@ConverterFor(DiscordUser.class)
//	DiscordUser convertToDiscordUser(String value) {
//		return new DiscordService().get(convertToOfflinePlayer(value));
//	}
//
//	@TabCompleterFor(DiscordUser.class)
//	List<String> tabCompleteDiscordUser(String value) {
//		return tabCompletePlayer(value);
//	}
//}
