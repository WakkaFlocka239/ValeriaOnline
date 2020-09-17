package me.wakka.valeriaonline.framework.exceptions;

public class MustBeIngameException extends CustomException{
	public MustBeIngameException() {
		super("You must be in-game to use this command");
	}
}
