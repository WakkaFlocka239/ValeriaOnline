package me.wakka.valeriaonline.features.fame.menu.providers;

import com.google.common.base.Strings;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lombok.AllArgsConstructor;
import me.wakka.valeriaonline.features.fame.menu.FameMenu;
import me.wakka.valeriaonline.features.menus.MenuUtils;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.models.fame.PrefixTag;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TagsProvider extends MenuUtils implements InventoryProvider {
	TagFilter filter;

	@Override
	public void init(Player player, InventoryContents contents) {
		FameService service = new FameService();
		Fame fame = service.get(player);
		PrefixTag activeTag = PrefixTags.parseTag(fame.getActiveTag());

		addBackItem(contents, e -> FameMenu.openMain(player));

		contents.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.BOOK).name("&dInfo")
				.lore("&7Click a tag")
				.lore("&7to select it")
				.build()));

		contents.set(0, 4, ClickableItem.from(new ItemBuilder(Material.RED_CONCRETE).name("&cRemove Tag").build(), e -> {
			PrefixTags.setActiveTag(player, null);
			Tasks.wait(2, () -> FameMenu.openMain(player));
		}));

		Pagination page = contents.pagination();

		//Filter Button
		contents.set(0, 6, ClickableItem.from(
				new ItemBuilder(Material.HOPPER).name("&dFilter:").lore("&7" + StringUtils.camelCase(filter.name())).build(),
				e -> FameMenu.openTags(player, Utils.EnumUtils.nextWithLoop(TagFilter.class, filter.ordinal())).open(player, page.getPage())
		));

		List<ClickableItem> menuItems = new ArrayList<>();
		List<PrefixTag> prefixTags = PrefixTags.getTags(player);

		for (PrefixTag prefixTag : prefixTags) {
			String filterTypeStr = filter.name().toUpperCase();
			String tagTypeStr = prefixTag.getType().name().toUpperCase();

			if (!filter.equals(TagFilter.ALL)) {
				if (!filterTypeStr.equalsIgnoreCase(tagTypeStr))
					continue;
			}

			String name = prefixTag.getName();
			String format = prefixTag.getFormat();
			String description = prefixTag.getDescription();
			if (Strings.isNullOrEmpty(description))
				description = "";

			ItemBuilder nameTag = new ItemBuilder(Material.NAME_TAG).name("&f" + name).lore(format, "", description).loreize(true);
			if (prefixTag.equals(activeTag))
				nameTag.name("&f&n" + name).glow();

			ClickableItem clickableTag = ClickableItem.from(nameTag.build(), e -> {
				PrefixTags.setActiveTag(player, prefixTag);
				Tasks.wait(2, () -> FameMenu.openMain(player));
			});

			menuItems.add(clickableTag);
		}

		page.setItemsPerPage(36);
		page.setItems(menuItems.toArray(new ClickableItem[0]));
		page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

		if (!page.isFirst())
			contents.set(5, 0, ClickableItem.from(new ItemBuilder(Material.ARROW).name("<-- Back").build(),
					e -> FameMenu.openTags(player, filter).open(player, page.previous().getPage())));
		if (!page.isLast())
			contents.set(5, 8, ClickableItem.from(new ItemBuilder(Material.ARROW).name("Next -->").build(),
					e -> FameMenu.openTags(player, filter).open(player, page.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
	}

	public enum TagFilter {
		ALL,
		QUEST,
		GUILD,
		OTHER
	}
}
