package me.wakka.valeriaonline.framework.exceptions.postconfigured;

import me.wakka.valeriaonline.framework.exceptions.CustomException;
import org.bukkit.ChatColor;

public class PostConfiguredException extends CustomException {

	public PostConfiguredException(String message) {
		super(ChatColor.RED + message);
	}

}
