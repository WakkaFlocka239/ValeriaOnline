//package me.wakka.valeriaonline.features.discord;
//
//import com.google.gson.Gson;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import me.pugabyte.bncore.BNCore;
//import me.pugabyte.bncore.features.discord.DiscordId.Channel;
//import me.pugabyte.bncore.features.discord.DiscordId.User;
//import me.pugabyte.bncore.framework.exceptions.BNException;
//import me.pugabyte.bncore.utils.Tasks;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//import javax.annotation.Nonnull;
//import java.util.Arrays;
//
//import static me.pugabyte.bncore.utils.StringUtils.stripColor;
//
//@NoArgsConstructor
//public class DiscordListener extends ListenerAdapter {
//
//	@Override
//	public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
//		Tasks.async(() -> {
//			String name = Discord.getName(event.getMember());
//			String channel = event.getChannelJoined().getName();
//
//			BNCore.fileLog("discord", name + " joined " + channel);
//		});
//	}
//
//	@Data
//	private class RandomPugClubResponse {
//		private String image;
//		private String link;
//	}
//
//	@Override
//	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
//		Tasks.async(() -> {
//			String name = Discord.getName(event.getMember());
//			String channel = event.getChannel().getName();
//			String message = event.getMessage().getContentRaw();
//
//			if (Arrays.asList(Channel.STAFF_BRIDGE.getId(), Channel.BRIDGE.getId()).contains(event.getChannel().getId()))
//				if (event.getMember().getUser().getId().equals(User.RELAY.getId()))
//					return;
//
//			for (Message.Attachment attachment : event.getMessage().getAttachments())
//				message += " " + attachment.getUrl();
//
//			BNCore.fileLog("discord", "[#" + channel + "] " + name + ": " + message.trim());
//
//			if (Channel.BOT_COMMANDS.getId().equals(event.getChannel().getId())) {
//				if (message.toLowerCase().startsWith(".pug")) {
//					try {
//						try (Response response = new OkHttpClient().newCall(new Request.Builder().url("http://randompug.club/loaf").build()).execute()) {
//							RandomPugClubResponse result = new Gson().fromJson(response.body().string(), RandomPugClubResponse.class);
//							Discord.koda(result.getImage(), Channel.BOT_COMMANDS);
//						}
//					} catch (Exception ex) {
//						event.getChannel().sendMessage(stripColor(ex.getMessage())).queue();
//						if (!(ex instanceof BNException))
//							ex.printStackTrace();
//					}
//				}
//			}
//		});
//	}
//
//}
