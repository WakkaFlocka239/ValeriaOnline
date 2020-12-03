package me.wakka.valeriaonline.features.pocketmobs;

import me.wakka.valeriaonline.features.pocketmobs.mount.MountType;
import me.wakka.valeriaonline.features.pocketmobs.mount.menu.MountsMenu;
import me.wakka.valeriaonline.features.pocketmobs.mount.mounts.HorseMount;
import me.wakka.valeriaonline.features.pocketmobs.mount.mounts.Mount;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import org.bukkit.entity.Horse;

@Permission("group.dev")
public class PocketMobsCommand extends CustomCommand {
	public static Mount mount = null;

	public PocketMobsCommand(CommandEvent event) {
		super(event);
	}

	@Path("menu")
	void openMenu() {
		MountsMenu.openMain(player()).open(player());
	}

	@Path("spawn")
	void entitySpawn() {
		if (mount == null) {
			HorseMount horseMount = new HorseMount(player(),
					"Horse Mount",
					null,
					MountType.HORSE,
					false,
					Horse.Color.BLACK,
					Horse.Style.WHITE_DOTS,
					HorseMount.iron_armor);

			if (!horseMount.spawn())
				error("something went wrong");

			mount = horseMount;
		} else {
			mount.spawn();
		}
	}

	@Path("revive")
	void entityRevive() {
		mount.setDead(false);
		if (!mount.spawn())
			error("something went wrong");

		send("Entity revived.");
	}

	@Path("remove")
	void entityRemove() {
		mount.despawn();
	}

	@Path("info")
	void entityStats() {
		send("- - -");
		send("Owner: " + mount.getOwningPlayer().getName());
		send("Name: " + mount.getName());
		send("Type: " + mount.getType());
		send("Spawned: " + mount.isSpawned());
		send("Dead: " + mount.isDead());
		send("Health: " + mount.getHealth());
		send("Has Storage: " + mount.isStorage());
		send("Storage Rows: " + mount.getStorageRows());
		send("- - -");
	}
}
