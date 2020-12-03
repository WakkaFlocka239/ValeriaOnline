//package me.wakka.valeriaonline.features.chat.bridge;
//
//import com.vdurmont.emoji.EmojiParser;
//import lombok.NoArgsConstructor;
//import me.wakka.valeriaonline.features.chat.ChatManager;
//import me.wakka.valeriaonline.features.chat.events.DiscordChatEvent;
//import me.wakka.valeriaonline.features.discord.Discord;
//import me.wakka.valeriaonline.models.chat.PublicChannel;
//import me.wakka.valeriaonline.models.discord.DiscordService;
//import me.wakka.valeriaonline.models.discord.DiscordUser;
//import me.wakka.valeriaonline.utils.JsonBuilder;
//import me.wakka.valeriaonline.utils.Tasks;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Optional;
//
//import static com.google.common.base.Strings.isNullOrEmpty;
//import static me.wakka.valeriaonline.utils.StringUtils.colorize;
//
//@NoArgsConstructor
//public class DiscordBridgeListener extends ListenerAdapter {
//
//	@Override
//	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
//		Tasks.async(() -> {
//			Optional<PublicChannel> channel = ChatManager.getChannelByDiscordId(event.getChannel().getId());
//			if (!channel.isPresent()) return;
//
//			if (event.getAuthor().isBot())
//				return;
//
//			JsonBuilder builder = new JsonBuilder(channel.get().getDiscordColor() + "[D] ");
//
//			DiscordUser user = new DiscordService().getFromUserId(event.getAuthor().getId());
//
//			if (user != null && !isNullOrEmpty(user.getUuid()))
//				builder.next(new Nerd(user.getUuid()).getChatFormat());
//			else
//				builder.next("&f" + Discord.getName(event.getMember(), event.getAuthor()));
//
//			builder.next(" " + channel.get().getDiscordColor() + "&l>&f");
//
//			String content = event.getMessage().getContentDisplay().trim();
//
//			try {
//				content = EmojiParser.parseToAliases(content);
//			} catch (Throwable ignore) {
//			} finally {
//				if (content.length() > 0)
//					builder.next(" " + colorize(content.replaceAll("&", "&&f")));
//
//				for (Message.Attachment attachment : event.getMessage().getAttachments())
//					builder.group()
//							.next(" &f&l[View Attachment]")
//							.url(attachment.getUrl());
//
//				DiscordChatEvent discordChatEvent = new DiscordChatEvent(event.getMember(), channel.get(), content, channel.get().getPermission());
//
//				Tasks.sync(() -> {
//					discordChatEvent.callEvent();
//					if (discordChatEvent.isCancelled()) {
//						Tasks.async(() -> event.getMessage().delete().queue());
//						return;
//					}
//
//					channel.get().broadcastIngame(builder);
//				});
//			}
//
//		});
//	}
//
//}