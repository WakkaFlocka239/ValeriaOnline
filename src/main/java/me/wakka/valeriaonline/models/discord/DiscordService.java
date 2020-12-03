//package me.wakka.valeriaonline.models.discord;
//
//import me.wakka.valeriaonline.features.discord.DiscordId.Role;
//import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
//import me.wakka.valeriaonline.models.MySQLService;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DiscordService extends MySQLService {
//	private static final Map<String, DiscordUser> cache = new HashMap<>();
//
//	public static void clearCache() {
//		cache.clear();
//	}
//
//	@Override
//	public DiscordUser get(String uuid) {
//		cache.computeIfAbsent(uuid, $ -> {
//			DiscordUser user = database.where("uuid = ?", uuid).first(DiscordUser.class);
//			if (user.getUuid() == null)
//				return new DiscordUser(uuid);
//			return user;
//		});
//
//		return cache.get(uuid);
//	}
//
//	public DiscordUser checkVerified(String userId) {
//		DiscordUser user = getFromUserId(userId);
//
//		if (user == null || user.getUserId() == null || user.getUuid() == null || !user.getMember().getRoles().contains(Role.VERIFIED.get()))
//			throw new InvalidInputException("You must link your Discord and Minecraft accounts before using this command");
//
//		return user;
//	}
//
//	public DiscordUser getFromUserId(String userId) {
//		DiscordUser user = database.where("userId = ?", userId).first(DiscordUser.class);
//		if (user.getUuid() == null)
//			return null;
//		return user;
//	}
//
//	public List<DiscordUser> getAll() {
//		return database.results(DiscordUser.class);
//	}
//
//}
