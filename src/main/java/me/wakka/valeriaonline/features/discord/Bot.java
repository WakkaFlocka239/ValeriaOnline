//package me.wakka.valeriaonline.features.discord;
//
//import com.jagrosh.jdautilities.command.CommandClientBuilder;
//import lombok.Getter;
//import lombok.SneakyThrows;
//import lombok.experimental.Accessors;
//import me.wakka.valeriaonline.ValeriaOnline;
//import me.wakka.valeriaonline.features.chat.bridge.DiscordBridgeListener;
//import me.wakka.valeriaonline.features.discord.DiscordId.User;
//import me.wakka.valeriaonline.utils.Tasks;
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.JDABuilder;
//import net.dv8tion.jda.api.OnlineStatus;
//import net.dv8tion.jda.api.entities.Activity;
//import net.dv8tion.jda.api.requests.GatewayIntent;
//import net.dv8tion.jda.api.utils.MemberCachePolicy;
//import org.bukkit.command.Command;
//import org.reflections.Reflections;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.EnumSet;
//
//import static com.google.common.base.Strings.isNullOrEmpty;
//
//public enum Bot {
//
//	RELAY {
//		@Override
//		JDABuilder build() {
//			return JDABuilder.createDefault(getToken())
//					.addEventListeners(new DiscordBridgeListener())
////					.addEventListeners(getCommands().setStatus(OnlineStatus.INVISIBLE).build())
//					;
//		}
//	};
//
//	@Getter
//	@Accessors(fluent = true)
//	private JDA jda;
//
//	abstract JDABuilder build();
//
//	@SneakyThrows
//	void connect() {
//		if (this.jda == null && !isNullOrEmpty(getToken())) {
//			final JDA jda = build()
//					.enableIntents(EnumSet.allOf(GatewayIntent.class))
//					.setMemberCachePolicy(MemberCachePolicy.ALL)
//					.build()
//					.awaitReady();
//
//			if (jda == null) {
//				ValeriaOnline.log("Could not connect " + name() + " to Discord");
//				return;
//			}
//
//			Tasks.sync(() -> {
//				if (this.jda == null)
//					this.jda = jda;
//				else
//					jda.shutdown();
//			});
//		}
//	}
//
//	void shutdown() {
//		if (jda != null) {
//			jda.shutdown();
//			jda = null;
//		}
//	}
//
//	protected String getToken() {
//		return ValeriaOnline.getInstance().getConfig().getString("tokens.discord." + name().toLowerCase(), "");
//	}
//
////	@SneakyThrows
////	protected CommandClientBuilder getCommands() {
////		CommandClientBuilder commands = new CommandClientBuilder()
////				.setPrefix("/")
////				.setOwnerId(User.WAKKA.getId())
////				.setActivity(Activity.playing("Minecraft"));
////
////		Reflections reflections = new Reflections(getClass().getPackage().getName());
////		for (Class<? extends Command> command : reflections.getSubTypesOf(Command.class)) {
////			HandledBy handledBy = command.getAnnotation(HandledBy.class);
////			if (handledBy != null && handledBy.value() == this)
////				commands.addCommand(command.newInstance());
////		}
////		return commands;
////	}
//
//	@Target({ElementType.TYPE})
//	@Retention(RetentionPolicy.RUNTIME)
//	public @interface HandledBy {
//		Bot value();
//	}
//}
