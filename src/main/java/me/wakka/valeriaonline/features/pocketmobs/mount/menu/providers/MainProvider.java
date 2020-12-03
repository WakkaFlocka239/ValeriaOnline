package me.wakka.valeriaonline.features.pocketmobs.mount.menu.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.wakka.valeriaonline.features.menus.MenuUtils;
import me.wakka.valeriaonline.features.pocketmobs.mount.MountType;
import me.wakka.valeriaonline.features.pocketmobs.mount.menu.MountsMenu;
import me.wakka.valeriaonline.utils.ItemBuilder;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainProvider extends MenuUtils implements InventoryProvider {
	@Override
	public void init(Player player, InventoryContents contents) {
		addCloseItem(contents);

		Pagination page = contents.pagination();
		List<ClickableItem> menuItems = new ArrayList<>();
		List<MountType> mountTypes = MountType.getMountTypes(player);
		for (MountType mountType : mountTypes) {
			ItemBuilder mountItem = new ItemBuilder(mountType.getDisplayMaterial())
					.name("&f" + StringUtils.camelCase(mountType));

			ClickableItem clickableItem = ClickableItem.empty(mountItem.build());

			menuItems.add(clickableItem);
		}

		page.setItemsPerPage(36);
		page.setItems(menuItems.toArray(new ClickableItem[0]));
		page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

		if (!page.isFirst())
			contents.set(5, 0, ClickableItem.from(new ItemBuilder(Material.ARROW).name("<-- Back").build(),
					e -> MountsMenu.openMain(player).open(player, page.previous().getPage())));
		if (!page.isLast())
			contents.set(5, 8, ClickableItem.from(new ItemBuilder(Material.ARROW).name("Next -->").build(),
					e -> MountsMenu.openMain(player).open(player, page.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
	}
}
