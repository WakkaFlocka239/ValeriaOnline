package me.wakka.valeriaonline.framework.exceptions.postconfigured;


public class PlayerNotFoundException extends PostConfiguredException {

	public PlayerNotFoundException(String input) {
		super("Player &7" + input + " &cnot found");
	}

}