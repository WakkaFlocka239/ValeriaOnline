package me.wakka.valeriaonline.framework.exceptions;

import net.md_5.bungee.api.ChatColor;

public class CustomException extends RuntimeException{
	public CustomException(String message) {
		super(ChatColor.RED + message);
	}
}
