package me.wakka.valeriaonline.features.commands;

import lombok.NonNull;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Permission("group.member")
public class TransferClaimBlocks extends CustomCommand {
	private static final String PREFIX = StringUtils.getPrefix("ClaimBlocks");

	public TransferClaimBlocks(@NonNull CommandEvent event) {
		super(event);
	}

	@Path("<player> [amount]")
	void transfer(OfflinePlayer toPlayer, int amount) {
		if (!toPlayer.isOnline())
			error("Player must be online");

		Player fromPlayer = player();

		if (toPlayer.equals(fromPlayer))
			error("Cannot transfer claim blocks to yourself");

		GriefPrevention griefPrevention = GriefPrevention.instance;
		DataStore dataStore = griefPrevention.dataStore;

		PlayerData fromPlayerData = dataStore.getPlayerData(fromPlayer.getUniqueId());
		int from_Remaining = fromPlayerData.getRemainingClaimBlocks();
		int minimum = griefPrevention.config_claims_initialBlocks;
		if (from_Remaining <= minimum || (from_Remaining - amount) <= minimum)
			error("You don't have enough claim blocks to transfer, minimum: " + griefPrevention.config_claims_initialBlocks);

		PlayerData toPlayerData = dataStore.getPlayerData(toPlayer.getUniqueId());
		int toTotal = toPlayerData.getAccruedClaimBlocks()
				+ toPlayerData.getBonusClaimBlocks()
				+ GriefPrevention.instance.dataStore.getGroupBonusBlocks(toPlayer.getUniqueId());

		int maximum = griefPrevention.config_claims_maxAccruedBlocks_default;

		if (toTotal + amount > maximum)
			error("Cannot transfer, " + toPlayer.getName() + " will hit the limit of " + maximum);

		fromPlayerData.setAccruedClaimBlocks(fromPlayerData.getAccruedClaimBlocks() - amount);
		send(fromPlayer, PREFIX + "You transferred " + amount + " claim blocks to " + toPlayer.getName());
		dataStore.savePlayerData(fromPlayer.getUniqueId(), fromPlayerData);

		toPlayerData.accrueBlocks(amount);
		send(toPlayer.getPlayer(), PREFIX + "You received " + amount + " claim blocks from " + fromPlayer.getName());
		dataStore.savePlayerData(toPlayer.getUniqueId(), toPlayerData);
	}
}
