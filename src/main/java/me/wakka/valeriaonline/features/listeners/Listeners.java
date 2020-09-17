package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.ValeriaOnline;
import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.objenesis.ObjenesisStd;

public class Listeners {

	public Listeners() {
		new Reflections(getClass().getPackage().getName()).getSubTypesOf(Listener.class).forEach(listener -> {
			try {
				ValeriaOnline.registerListener(new ObjenesisStd().newInstance(listener));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
