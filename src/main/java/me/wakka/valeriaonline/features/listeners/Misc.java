package me.wakka.valeriaonline.features.listeners;

import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Time;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Misc implements Listener {
	private static final Map<UUID, List<String>> tempPermMap = new HashMap<>();

	@EventHandler
	public void onClickNPC(NPCRightClickEvent event) {
		NPC npc = event.getNPC();
		String npcName = npc.getName();
		Player player = event.getClicker();

		if (npcName.equals("AuctionMaster"))
			handle(player, "auctionhouse.sell");
		else if (npcName.equals("Banker"))
			handle(player, "mobhunting.money.sell");
	}

	private void handle(Player player, String perm) {
		UUID uuid = player.getUniqueId();

		if (tempPermMap.containsKey(uuid) && tempPermMap.get(uuid).contains(perm))
			return;

		List<String> perms = tempPermMap.getOrDefault(uuid, new ArrayList<>());
		perms.add(perm);
		tempPermMap.put(uuid, perms);

		ValeriaOnline.getPerms().playerAdd(player, perm);

		Tasks.wait(Time.SECOND.x(30), () -> {
			List<String> _perms = tempPermMap.get(uuid);
			_perms.remove(perm);
			tempPermMap.put(uuid, _perms);

			ValeriaOnline.getPerms().playerRemove(player, perm);
		});
	}
}
