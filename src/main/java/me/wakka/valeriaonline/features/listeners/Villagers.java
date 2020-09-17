package me.wakka.valeriaonline.features.listeners;

import de.tr7zw.nbtapi.NBTEntity;
import me.wakka.valeriaonline.Utils.Utils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Villagers implements Listener {

	@EventHandler
	public void onClickVillager(PlayerInteractEntityEvent event) {
		if (!event.getRightClicked().getType().equals(EntityType.VILLAGER))
			return;

		Villager villager = (Villager) event.getRightClicked();
		Villager.Profession profession = villager.getProfession();
		int level = villager.getVillagerLevel();
		int exp = villager.getVillagerExperience();
		Villager.Type type = villager.getVillagerType();

		Utils.send(event.getPlayer(), "");
		Utils.send(event.getPlayer(), "Profession: " + profession);
		Utils.send(event.getPlayer(), "Level: " + level);
		Utils.send(event.getPlayer(), "Experience: " + exp);
		Utils.send(event.getPlayer(), "Type: " + type);

		NBTEntity nbtE = new NBTEntity(villager);
		String rep = "none";
		try {
			rep = nbtE.getCompoundList("Gossips").get(0).getString("Type");
		} catch (Exception ignored) {
		}

		Utils.send(event.getPlayer(), "Reputation: " + rep);
	}
}
