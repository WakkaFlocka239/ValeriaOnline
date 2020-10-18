package me.wakka.valeriaonline.features.fame.menu;

import fr.minuskube.inv.SmartInventory;
import me.wakka.valeriaonline.features.fame.menu.providers.FameLeaderboardProvider;
import me.wakka.valeriaonline.features.fame.menu.providers.MainProvider;
import me.wakka.valeriaonline.features.fame.menu.providers.TagsProvider;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.entity.Player;

public class FameMenu {

	public static void openMain(Player player) {
		SmartInventory.builder()
				.title("&fFame Menu")
				.size(3, 9)
				.provider(new MainProvider())
				.build()
				.open(player);
	}

	public static SmartInventory openTags(Player player, TagsProvider.TagFilter filter) {
		return SmartInventory.builder()
				.title("&fSelect A Tag:")
				.size(getTagsSize(player), 9)
				.provider(new TagsProvider(filter))
				.build();
	}

	private static int getTagsSize(Player player) {
		int tags = PrefixTags.getTags(player).size();
		int rows = (int) Math.ceil((tags / 9.0)) + 2;
		return Math.min(Math.max(rows, 3), 6);
	}

	public static SmartInventory openFameLeaderboard(FameService.FameType filter) {
		return SmartInventory.builder()
				.title("&f" + StringUtils.camelCase(filter.name()) + " Fame Leaderboard")
				.size(4, 9)
				.provider(new FameLeaderboardProvider(filter))
				.build();
	}
}
