package me.wakka.valeriaonline.framework.exceptions;


public class PlayerNotFoundException extends CustomException {

	public PlayerNotFoundException(String input) {
		super("Player &e" + input + " &cnot found");
	}

}