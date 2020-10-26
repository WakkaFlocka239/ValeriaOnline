package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.annotations.Disabled;
import me.wakka.valeriaonline.framework.features.Feature;
import org.bukkit.event.Listener;
import org.objenesis.ObjenesisStd;
import org.reflections.Reflections;

public class Listeners extends Feature {

	@Override
	public void startup() {
		new Reflections(getClass().getPackage().getName()).getSubTypesOf(Listener.class).forEach(listener -> {
			try {
				if (listener.getAnnotation(Disabled.class) == null)
					ValeriaOnline.registerListener(new ObjenesisStd().newInstance(listener));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
