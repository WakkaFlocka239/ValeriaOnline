package me.wakka.valeriaonline.framework.exceptions;


public class PlayerNotFoundException extends CustomException {

	public PlayerNotFoundException(String input) {
		super("Player &7" + input + " &cnot found");
	}

}