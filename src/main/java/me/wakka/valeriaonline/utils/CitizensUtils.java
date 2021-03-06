package me.wakka.valeriaonline.utils;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.entity.Entity;

public class CitizensUtils {

	public static NPC getNPC(int id) {
		return CitizensAPI.getNPCRegistry().getById(id);
	}

	public static void updateNameAndSkin(int id, String name) {
		updateNameAndSkin(getNPC(id), name);
	}

	public static void updateNameAndSkin(NPC npc, String name) {
		updateName(npc, name);
		updateSkin(npc, name);
	}

	public static void updateName(int id, String name) {
		updateName(getNPC(id), name);
	}

	public static void updateName(NPC npc, String name) {
		Tasks.sync(() -> npc.setName(name));
	}

	public static void updateSkin(int id, String name) {
		updateSkin(getNPC(id), name);
	}

	public static void updateSkin(NPC npc, String name) {
		updateSkin(npc, name, false);
	}

	public static void updateSkin(NPC npc, String name, boolean useLatest) {
		Tasks.sync(() -> {
			npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, StringUtils.stripColor(name));
			npc.data().setPersistent(NPC.PLAYER_SKIN_USE_LATEST, useLatest);

			Entity npcEntity = npc.getEntity();
			if (npcEntity instanceof SkinnableEntity) {
				((SkinnableEntity) npcEntity).getSkinTracker().notifySkinChange(npc.data().get(NPC.PLAYER_SKIN_USE_LATEST));
			}
		});
	}

	public static boolean isNPC(Entity entity) {
		return entity.hasMetadata("NPC");
	}
}
