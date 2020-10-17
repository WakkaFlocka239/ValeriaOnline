package me.wakka.valeriaonline.features.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;
import me.wakka.valeriaonline.models.fame.Fame;
import me.wakka.valeriaonline.models.fame.FameService;
import me.wakka.valeriaonline.models.fame.PrefixTag;
import me.wakka.valeriaonline.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

	private final ValeriaOnline plugin = ValeriaOnline.getInstance();
	FameService service = new FameService();

	/**
	 * Because this is an internal class,
	 * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
	 * PlaceholderAPI is reloaded
	 *
	 * @return true to persist through reloads
	 */
	@Override
	public boolean persist() {
		return true;
	}

	/**
	 * Because this is a internal class, this check is not needed
	 * and we can simply return {@code true}
	 *
	 * @return Always true since it's an internal class.
	 */
	@Override
	public boolean canRegister() {
		return true;
	}

	/**
	 * The name of the person who created this expansion should go here.
	 * For convienience do we return the author from the plugin.yml
	 *
	 * @return The name of the author as a String.
	 */
	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	/**
	 * The placeholder identifier should go here.
	 * This is what tells PlaceholderAPI to call our onRequest
	 * method to obtain a value if a placeholder starts with our
	 * identifier.
	 * The identifier has to be lowercase and can't contain _ or %
	 *
	 * @return The identifier in {@code %<identifier>_<value>%} as String.
	 */
	@Override
	public @NotNull String getIdentifier() {
		return "valeriaonline";
	}

	/**
	 * This is the version of the expansion.
	 * You don't have to use numbers, since it is set as a String.
	 * <p>
	 * For convienience do we return the version from the plugin.yml
	 *
	 * @return The version as a String.
	 */
	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	/**
	 * This is the method called when a placeholder with our identifier
	 * is found and needs a value.
	 * We specify the value identifier in this method.
	 * Since version 2.9.1 can you use OfflinePlayers in your requests.
	 *
	 * @param player     A {@link Player Player}.
	 * @param identifier A String containing the identifier/value.
	 * @return possibly-null String of the requested identifier.
	 */

	// %valeriaonline_<identifier>%
	@Override
	public String onPlaceholderRequest(Player player, @NotNull String identifier) {

		if (player == null) return "";

		if (identifier.equals("chatradius")) {
			String chatManagerRadius = StringUtils.stripColor(PlaceholderAPI.setPlaceholders(player, "%chatmanager_radius%")).toLowerCase();

			switch (chatManagerRadius) {
				case "l":
					return "&eLocal";
				case "g":
					return "&2Global";
				case "world":
					return "&cWorld";
			}

			return "null";
		} else if (identifier.equals("tag")) {
			Fame fame = service.get(player);
			PrefixTag tag = PrefixTags.parseTag(fame.getActiveTag());
			String format = "";

			if (player.hasPermission("group.staff")) {
				if (tag != null)
					format = StringUtils.colorize(tag.getFormat());

				return PrefixTags.getGroupFormat(player) + format;
			} else {
				if (tag != null)
					format = StringUtils.colorize(tag.getFormat());
				else
					format = PrefixTags.getGroupFormat(player);

				return format;
			}

		} else if (identifier.equals("group")) {
			return PrefixTags.getGroupFormat(player);
		}

		return null;
	}
}
