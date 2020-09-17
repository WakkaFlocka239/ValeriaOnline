package me.wakka.valeriaonline.framework.exceptions;

public class MustBeConsoleException extends CustomException {

	public MustBeConsoleException() {
		super("You must be console to use this command");
	}

}