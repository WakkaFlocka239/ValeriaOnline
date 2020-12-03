//package me.wakka.valeriaonline.features.discord;
//
//import lombok.Getter;
//import net.dv8tion.jda.api.entities.Member;
//
//public class DiscordId {
//
//	public enum Channel {
//		GENERAL("739476867215654973"), 			// public_chat
//		ANNOUNCEMENTS("739660748845875270"),		// public-announcements
//		BOT_COMMANDS("223897739082203137"),
//
//		BRIDGE("331277920729432065"),
//		STAFF_BRIDGE("331842528854802443"),
//		STAFF_ADMINS("133950052249894913"),
//
//		STAFF_LOG("256866302176526336"),
//		ADMIN_LOG("390751748261937152");
//
//		@Getter
//		private String id;
//
//		Channel(String id) {
//			this.id = id;
//		}
//	}
//
//	public enum User {
//		WAKKA("115300235680546825"),
//		RELAY("??"), // TODO DISCORD
//		UBER("85614143951892480");
//
//		@Getter
//		private String id;
//
//		User(String id) {
//			this.id = id;
//		}
//
//		public net.dv8tion.jda.api.entities.User get() {
//			Member member = getMember();
//			return member == null ? null : member.getUser();
//		}
//
//		public Member getMember() {
//			return Discord.getGuild().retrieveMemberById(id).complete();
//		}
//	}
//
//	public enum Guild {
//		VALERIA_ONLINE("739476156515876875");
//
//		@Getter
//		private String id;
//
//		Guild(String id) {
//			this.id = id;
//		}
//	}
//
//	public enum Role {
//		STAFF("739669915853651978"),
//		VERIFIED("771857373168926741"),
//		CHATBOT("771857617781260309");
//
//
//		@Getter
//		private String id;
//
//		Role(String id) {
//			this.id = id;
//		}
//
//		public net.dv8tion.jda.api.entities.Role get() {
//			return Discord.getGuild().getRoleById(id);
//		}
//	}
//}
