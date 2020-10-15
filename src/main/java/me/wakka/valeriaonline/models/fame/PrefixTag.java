package me.wakka.valeriaonline.models.fame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.features.prefixtags.PrefixTags;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PrefixTag {
	@NonNull String name;
	String format;
	int cost;
	@NonNull PrefixTags.PrefixTagType type;
	String description;
	@NonNull String permission;

	public PrefixTag(String name, String format, int cost, PrefixTags.PrefixTagType type, String description) {
		this.name = name;
		this.format = format;
		this.cost = cost;
		this.type = type;
		this.description = description;
		this.permission = "prefixtags." + type.name().toLowerCase() + "." + name.toLowerCase();
	}
}
