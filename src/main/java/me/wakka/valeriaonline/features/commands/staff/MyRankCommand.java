package me.wakka.valeriaonline.features.commands.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.Permission;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Permission("set.my.rank")
public class MyRankCommand extends CustomCommand {
	public static List<Rank> ranks = new ArrayList<>();

	public MyRankCommand(@NonNull CommandEvent event) {
		super(event);
	}

	static {
		for (String group : ValeriaOnline.getPerms().getGroups()) {
			Rank rank = new Rank(group);
			ranks.add(rank);
		}
	}

	@Path("<rank>")
	void set(Rank rank) {
		runCommandAsConsole("upc setgroups " + player().getName() + " " + rank.getName().toLowerCase());
		send(PREFIX + "Set your rank to " + StringUtils.camelCase(rank.getName()));
	}

	@Data
	@AllArgsConstructor
	public static class Rank {
		String name;

		public static Rank getRank(String rank) {
			for (Rank _rank : ranks) {
				if (_rank.getName().toLowerCase().equalsIgnoreCase(rank.toLowerCase()))
					return _rank;
			}
			return null;
		}
	}

	@ConverterFor(Rank.class)
	Rank convertToRank(String value) {
		return Rank.getRank(value);
	}

	@TabCompleterFor(Rank.class)
	List<String> tabCompleteRank(String filter) {
		return ranks.stream()
				.filter(rank -> rank.getName().toLowerCase().startsWith(filter.toLowerCase()))
				.map(Rank::getName)
				.collect(Collectors.toList());
	}
}
