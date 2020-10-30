package me.wakka.valeriaonline.framework.exceptions.postconfigured;

import me.wakka.valeriaonline.framework.exceptions.CustomException;
import net.md_5.bungee.api.ChatColor;

public class PostConfiguredException extends CustomException {

	public PostConfiguredException(String message) {
		super(ChatColor.RED + message);
	}

}
