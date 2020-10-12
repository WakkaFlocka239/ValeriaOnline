package me.wakka.valeriaonline.features.fame.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.features.fame.menu.FameMenu;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.MenuUtils;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@AllArgsConstructor
public class FameLeaderboardProvider extends MenuUtils implements InventoryProvider {
	FameService.FameType filter;

	@Override
	public void init(Player player, InventoryContents contents) {
		addBackItem(contents, e -> FameMenu.openMain(player));

		Pagination page = contents.pagination();

		//Filter Button
		contents.set(0, 8, ClickableItem.from(
				new ItemBuilder(Material.HOPPER).name("&dType: &7" + StringUtils.camelCase(filter.name())).build(),
				e -> FameMenu.openFameLeaderboard(Utils.EnumUtils.nextWithLoop(FameService.FameType.class, filter.ordinal())).open(player, page.getPage())
		));

		List<ClickableItem> menuItems = new ArrayList<>();

		FameService service = new FameService();
		List<Fame> results = service.getAll();
		if (results.size() == 0)
			return;

		List<Fame> sortedFame = results.stream()
				.sorted(comparing((Fame fame) -> fame.getPoints(filter)).reversed())
				.collect(Collectors.toList());

		int ndx = ((page.getPage() + 1) * 36) - 35;
		for (Fame fame : sortedFame) {
			int points = fame.getPoints(filter);
			if (points <= 0)
				continue;

			ItemBuilder skull = new ItemBuilder(Material.PLAYER_HEAD)
					.skullOwner(fame.getPlayer())
					.name("&d#" + ndx + " &b" + fame.getPlayer().getName())
					.lore("&7" + StringUtils.camelCase(filter.name()) + " Fame: &b" + fame.getPoints(filter));

			ClickableItem clickableItem = ClickableItem.empty(skull.build());

			menuItems.add(clickableItem);
			++ndx;
		}

		page.setItemsPerPage(36);
		page.setItems(menuItems.toArray(new ClickableItem[0]));
		page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

		if (!page.isFirst())
			contents.set(5, 0, ClickableItem.from(new ItemBuilder(Material.ARROW).name("<-- Back").build(),
					e -> FameMenu.openFameLeaderboard(filter).open(player, page.previous().getPage())));
		if (!page.isLast())
			contents.set(5, 8, ClickableItem.from(new ItemBuilder(Material.ARROW).name("Next -->").build(),
					e -> FameMenu.openFameLeaderboard(filter).open(player, page.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
	}
}
