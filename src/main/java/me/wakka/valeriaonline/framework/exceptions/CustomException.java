package me.wakka.valeriaonline.framework.exceptions;

import org.bukkit.ChatColor;

public class CustomException extends RuntimeException{
	public CustomException(String message) {
		super(ChatColor.RED + message);
	}
}
