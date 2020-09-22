package me.wakka.valeriaonline.features.itemtags;

import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemTagUtils {

	public static ItemStack updateItem(ItemStack itemStack) {
		Condition condition = Condition.of(itemStack);
		Rarity rarity = Rarity.of(itemStack);

		// Clear Tags
		itemStack = clearTags(itemStack);

		// Add Tag: Condition
		itemStack = addCondition(itemStack, condition);

		// Add Tag: Rarity
		itemStack = addRarity(itemStack, rarity);

		return finalizeItem(itemStack);
	}

	// Grabs all tags and reorders them in the correct order
	public static ItemStack finalizeItem(ItemStack itemStack){
		String conditionTag = null;
		String rarityTag = null;

		List<String> lore = itemStack.getLore();


		if(lore != null && lore.size() > 0) {
			// Find condition tag
			int ndx = 0;
			for (String line : new ArrayList<>(lore)) {
				if (conditionTag == null) {
					for (Condition condition : Condition.values()) {
						String tag = StringUtils.stripColor(condition.getTag());
						String _line = StringUtils.stripColor(line);

						if (tag.equalsIgnoreCase(_line)) {
							conditionTag = line;
							lore.remove(ndx);
							break;
						}
					}
					++ndx;
				} else
					break;
			}

			// Find rarity tag
			ndx = 0;
			for (String line : new ArrayList<>(lore)) {
				if (rarityTag == null) {
					for (Rarity rarity : Rarity.values()) {
						String tag = StringUtils.stripColor(rarity.getTag());
						String _line = StringUtils.stripColor(line);

						if (tag.equalsIgnoreCase(_line)) {
							rarityTag = line;
							lore.remove(ndx);
							break;
						}
					}
					++ndx;
				} else
					break;
			}

			// re-add tags in correct order
			if (conditionTag != null)
				lore.add(conditionTag);

			if (rarityTag != null)
				lore.add(rarityTag);

			itemStack.setLore(lore);
		}
		return itemStack;
	}

	public static ItemStack clearTags(ItemStack itemStack){

		clearCondition(itemStack);
		clearRarity(itemStack);

		return itemStack;
	}

	public static ItemStack clearRarity(ItemStack itemStack){
		List<String> lore = itemStack.getLore();
		if(lore != null && lore.size() > 0) {
			for (Rarity _rarity : Rarity.values()) {
				int ndx = 0;
				for (String line : new ArrayList<>(lore)) {
					String tag = StringUtils.stripColor(_rarity.getTag());
					String _line = StringUtils.stripColor(line);

					if (tag.equalsIgnoreCase(_line))
						lore.remove(ndx);
					++ndx;
				}
			}

			itemStack.setLore(lore);
		}

		return itemStack;
	}

	public static ItemStack clearCondition(ItemStack itemStack){
		List<String> lore = itemStack.getLore();
		if(lore != null && lore.size() > 0) {
			for (Condition _condition : Condition.values()) {
				int ndx = 0;
				for (String line : new ArrayList<>(lore)) {
					String tag = StringUtils.stripColor(_condition.getTag());
					String _line = StringUtils.stripColor(line);

					if (tag.equalsIgnoreCase(_line))
						lore.remove(ndx);
					++ndx;
				}
			}

			itemStack.setLore(lore);
		}

		return itemStack;
	}

	public static ItemStack addRarity(ItemStack itemStack, Rarity rarity){
		return addRarity(itemStack, rarity, false);
	}

	public static ItemStack addRarity(ItemStack itemStack, Rarity rarity, boolean clear){
		// Clear Rarity Tag
		if(clear)
			clearRarity(itemStack);

		if(rarity != null) {
			List<String> lore = itemStack.getLore();
			String rarityTag = rarity.getTag();

			if(lore == null)
				lore = new ArrayList<>();

			lore.add(rarityTag);
			itemStack.setLore(lore);
		}

		return itemStack;
	}

	public static ItemStack addCondition(ItemStack itemStack, Condition condition){
		return addCondition(itemStack, condition, false);
	}

	public static ItemStack addCondition(ItemStack itemStack, Condition condition, boolean clear){
		if(clear)
			clearCondition(itemStack);

		if(condition != null){
			List<String> lore = itemStack.getLore();
			String conditionTag = condition.getTag();

			if(lore == null)
				lore = new ArrayList<>();

			lore.add(conditionTag);
			itemStack.setLore(lore);
		}

		return itemStack;
	}

}
