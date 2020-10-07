package me.wakka.valeriaonline.framework.commands;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wakka.valeriaonline.utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CommandBlockArgs {
	SelectorType selectorType;
	Collection<?> targets;
	Location origin;
	double maxDistance;
	double minDistance;
	boolean exact;
	Location destination;
	Float destinationYaw;
	Float destinationPitch;

	public enum SelectorType {
		P, // Nearest Player
		R, // Random Player
		A, // All Players
		E, // All Entities
		S // Executing entity
	}

	public static Collection<? extends Object> getSelectorTargets(CommandSender sender, SelectorType selectorType, Location origin, double minRadius, double maxRadius, boolean exact) {
		boolean useRadius = true;

		if (maxRadius == -1) {
			useRadius = false;
		} else {
			if (exact)
				minRadius = maxRadius;
		}

		return getSelectorTargetsFinal(sender, selectorType, origin, minRadius, maxRadius, useRadius);

	}

	private static Collection<? extends Object> getSelectorTargetsFinal(CommandSender sender, SelectorType selectorType, Location origin, double minRadius, double maxRadius, boolean useRadius) {
		// Nearest Player
		if (selectorType.equals(SelectorType.P)) {
			List<Player> nearbyPlayers;

			if (useRadius) {
				nearbyPlayers = origin.getNearbyPlayers(maxRadius, maxRadius, maxRadius)
						.stream()
						.filter(player -> player.getLocation().distance(origin) >= minRadius)
						.collect(Collectors.toList());
			} else {
				nearbyPlayers = Bukkit.getOnlinePlayers()
						.stream()
						.filter(player -> player.getWorld().equals(origin.getWorld()))
						.collect(Collectors.toList());
			}

			double nearestDistance = Double.MAX_VALUE;
			Player nearestPlayer = null;
			for (Player nearbyPlayer : nearbyPlayers) {
				double distance = nearbyPlayer.getLocation().distance(origin);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					nearestPlayer = nearbyPlayer;
				}
			}

			return Collections.singleton(nearestPlayer);
		}

		// Random Player
		if (selectorType.equals(SelectorType.R)) {
			if (useRadius)
				return Collections.singletonList(
						RandomUtils.randomElement(
								Bukkit.getOnlinePlayers()
										.stream()
										.filter(player ->
												player.getWorld().equals(origin.getWorld())
														&& player.getLocation().distance(origin) >= minRadius
														&& player.getLocation().distance(origin) <= maxRadius)
										.collect(Collectors.toList())
						)
				);
			else
				return Collections.singletonList(
						RandomUtils.randomElement(
								Bukkit.getOnlinePlayers()
										.stream()
										.filter(player -> player.getWorld().equals(origin.getWorld()))
										.collect(Collectors.toList())
						)
				);
		}

		// All Players
		if (selectorType.equals(SelectorType.A)) {
			List<Player> nearbyPlayers;
			if (useRadius) {
				nearbyPlayers = origin.getNearbyPlayers(maxRadius, maxRadius, maxRadius)
						.stream()
						.filter(player -> player.getLocation().distance(origin) >= minRadius)
						.collect(Collectors.toList());
			} else {
				nearbyPlayers = Bukkit.getOnlinePlayers()
						.stream()
						.filter(player -> player.getWorld().equals(origin.getWorld()))
						.collect(Collectors.toList());
			}

			return nearbyPlayers;
		}

		// All Entities
		if (selectorType.equals(SelectorType.E)) {
			List<Entity> nearbyEntities;
			if (useRadius) {
				nearbyEntities = origin.getNearbyEntities(maxRadius, maxRadius, maxRadius)
						.stream()
						.filter(entity -> entity.getLocation().distance(origin) >= minRadius)
						.collect(Collectors.toList());
			} else {
				nearbyEntities = origin.getWorld().getEntities()
						.stream()
						.filter(entity -> entity.getWorld().equals(origin.getWorld()))
						.collect(Collectors.toList());
			}

			return nearbyEntities;
		}

		if (selectorType.equals(SelectorType.S)) {
			if (sender instanceof Entity) {
				Entity entitySender = (Entity) sender;
				return Collections.singleton(entitySender);
			}
		}

		return null;
	}
}
