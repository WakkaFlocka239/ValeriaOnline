//package me.wakka.valeriaonline.models.discord;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import me.wakka.valeriaonline.features.discord.Bot;
//import me.wakka.valeriaonline.features.discord.Discord;
//import me.wakka.valeriaonline.utils.Utils;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.User;
//import org.bukkit.OfflinePlayer;
//
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import static me.wakka.valeriaonline.features.discord.Discord.discordize;
//
//@Data
//@NoArgsConstructor
//@RequiredArgsConstructor
//@Table(name = "discord_user")
//public class DiscordUser {
//	@Id
//	@NonNull
//	private String uuid;
//	private String userId;
//	private String roleId;
//
//	public DiscordUser(@NonNull String uuid, String userId) {
//		this.uuid = uuid;
//		this.userId = userId;
//	}
//
//	public String getBridgeName() {
//		OfflinePlayer player = Utils.getPlayer(uuid);
//		String name = "**" + discordize(player.getName()) + "**";
//		if (roleId != null)
//			name = "<@&&f" + roleId + ">";
//		return name;
//	}
//
//	public OfflinePlayer getOfflinePlayer() {
//		return Utils.getPlayer(uuid);
//	}
//
//	public String getIngameName() {
//		OfflinePlayer player = getOfflinePlayer();
//		if (player == null)
//			return null;
//		return player.getName();
//	}
//
//	public String getName() {
//		return Discord.getName(userId);
//	}
//
//	public String getDiscrim() {
//		return getUser().getDiscriminator();
//	}
//
//	public String getNameAndDiscrim() {
//		return getName() + "#" + getDiscrim();
//	}
//
//	private User getUser() {
//		return Bot.RELAY.jda().retrieveUserById(userId).complete();
//	}
//
//	public Member getMember() {
//		return Discord.getGuild().retrieveMemberById(userId).complete();
//	}
//
//}
