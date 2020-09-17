package me.wakka.valeriaonline.framework.exceptions;

public class NoPermissionException extends CustomException{
	public NoPermissionException() {
		super("You don't have permission to do that!");
	}
}
