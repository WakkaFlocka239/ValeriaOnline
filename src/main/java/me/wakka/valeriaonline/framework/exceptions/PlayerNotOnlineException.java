package me.wakka.valeriaonline.framework.exceptions;

import org.bukkit.OfflinePlayer;

public class PlayerNotOnlineException extends CustomException {

	public PlayerNotOnlineException(OfflinePlayer offlinePlayer) {
		super(offlinePlayer.getName() + " is not online");
	}

}
