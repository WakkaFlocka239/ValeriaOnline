package me.wakka.valeriaonline.framework.exceptions;

public class MustBeCommandBlockException extends CustomException {

	public MustBeCommandBlockException() {
		super("You must be a command block to use this command");
	}

}
