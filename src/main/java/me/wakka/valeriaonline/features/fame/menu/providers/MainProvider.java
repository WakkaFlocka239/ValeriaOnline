package me.wakka.valeriaonline.features.fame.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import me.wakka.valeriaonline.features.fame.menu.FameMenu;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.models.fame.PrefixTag;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.MenuUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainProvider extends MenuUtils implements InventoryProvider {
	private final ItemStack leaderboardMenu = new ItemBuilder(Material.BEACON).name("&f[&bLeaderboards&f]").build();

	@Override
	public void init(Player player, InventoryContents contents) {
		addCloseItem(contents);

		FameService service = new FameService();
		Fame fame = service.get(player);

		int questFame = fame.getPoints_quest();
		int guildFame = fame.getPoints_guild();
		ItemStack questFameItem = new ItemBuilder(Material.PAPER).name("&dQuest Fame").lore("&7You have: &a" + questFame + " &7Quest Fame").build();
		ItemStack guildFameItem = new ItemBuilder(Material.PAPER).name("&dGuild Fame").lore("&7You have: &a" + guildFame + " &7Guild Fame").build();

		PrefixTag prefixTag = PrefixTags.parseTag(fame.getActiveTag());
		String currentTag = "&7Current Tag: &r";
		if (prefixTag == null)
			currentTag = "&cYou don't have an active tag";
		else
			currentTag += prefixTag.getFormat();

		ItemStack tagsMenu = new ItemBuilder(Material.NAME_TAG).name("&f[&bTags&f]").lore(currentTag).build();

		contents.set(new SlotPos(1, 1), ClickableItem.empty(questFameItem));
		contents.set(new SlotPos(1, 2), ClickableItem.empty(guildFameItem));

		contents.set(new SlotPos(1, 4), ClickableItem.from(tagsMenu, e ->
				FameMenu.openTags(player, TagsProvider.TagFilter.ALL).open(player)));

		contents.set(new SlotPos(1, 7), ClickableItem.from(leaderboardMenu, e ->
				FameMenu.openFameLeaderboard(FameService.FameType.QUEST).open(player)));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
	}
}
