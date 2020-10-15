package me.wakka.valeriaonline.features.minigames.models.mechanics;

import me.wakka.valeriaonline.features.minigames.mechanics.JDungeon;

public enum MechanicType {
	CRYPT(new JDungeon());

	private Mechanic mechanic;

	MechanicType(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	public Mechanic get() {
		return mechanic;
	}
}
