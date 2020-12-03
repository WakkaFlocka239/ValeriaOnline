//package me.wakka.valeriaonline.features.chat.bridge;
//
//import net.dv8tion.jda.api.entities.Role;
//import org.bukkit.OfflinePlayer;
//
//import java.awt.*;
//import java.util.Arrays;
//import java.util.List;
//
//public class RoleManager {
//	public static final List<String> ignore = Arrays.asList(
//			"WakkaFlocka",
//			"Filid",
//			"Blast",
//			"KodaBear");
//
//	public static void update(DiscordUser user) {
//		if (Discord.getGuild() == null)
//			return;
//
//		DiscordService service = new DiscordService();
//		OfflinePlayer player = Utils.getPlayer(user.getUuid());
//
//		if (ignore.contains(player.getName()))
//			return;
//
//		Color roleColor = new Nerd(player).getRank().getDiscordColor();
//
//		if (roleColor == null) {
//			user.setRoleId(null);
//			service.save(user);
//			return;
//		}
//
//		Role role = null;
//		if (user.getRoleId() != null)
//			role = Discord.getGuild().getRoleById(user.getRoleId());
//
//		if (user.getRoleId() == null || role == null) {
//			List<Role> rolesByName = Discord.getGuild().getRolesByName(player.getName(), true);
//			if (rolesByName.size() > 0)
//				user.setRoleId(rolesByName.get(0).getId());
//			else
//				Discord.getGuild().createRole()
//						.setName(player.getName())
//						.setColor(new Nerd(player).getRank().getDiscordColor())
//						.setMentionable(true)
//						.queue();
//		} else {
//			if (role.getColor() != roleColor)
//				role.getManager().setColor(roleColor).queue();
//			if (!role.getName().equals(player.getName()))
//				role.getManager().setName(player.getName()).queue();
//		}
//	}
//
//}
