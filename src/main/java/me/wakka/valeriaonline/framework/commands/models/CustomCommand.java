package me.wakka.valeriaonline.framework.commands.models;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.framework.commands.CommandBlockArgs;
import me.wakka.valeriaonline.framework.commands.models.annotations.ConverterFor;
import me.wakka.valeriaonline.framework.commands.models.annotations.Description;
import me.wakka.valeriaonline.framework.commands.models.annotations.Fallback;
import me.wakka.valeriaonline.framework.commands.models.annotations.HideFromHelp;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleterFor;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.framework.exceptions.MustBeCommandBlockException;
import me.wakka.valeriaonline.framework.exceptions.MustBeConsoleException;
import me.wakka.valeriaonline.framework.exceptions.MustBeIngameException;
import me.wakka.valeriaonline.framework.exceptions.NoPermissionException;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.PlayerNotFoundException;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.PlayerNotOnlineException;
import me.wakka.valeriaonline.utils.JsonBuilder;
import me.wakka.valeriaonline.utils.StringUtils;
import me.wakka.valeriaonline.utils.Tasks;
import me.wakka.valeriaonline.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.wakka.valeriaonline.utils.StringUtils.trimFirst;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "UnusedReturnValue"})
public abstract class CustomCommand extends ICustomCommand{
	@NonNull
	@Getter
	protected CommandEvent event;
	public String PREFIX = StringUtils.getPrefix(StringUtils.listLast(this.getClass().getName(), ".").replaceAll("Command$", ""));

	public String getPrefix() {
		return PREFIX;
	}

	public String getAliasUsed() {
		return event.getAliasUsed();
	}

	public void _shutdown() {}

	protected String camelCase(Enum<?> _enum) {
		return camelCase(_enum.name());
	}

	protected String camelCase(String string) {
		return StringUtils.camelCase(string);
	}

	protected String plural(String string, double number) {
		return StringUtils.plural(string, number);
	}

	protected ItemStack getToolRequired() {
		return Utils.getToolRequired(player());
	}

	protected ItemStack getTool() {
		return Utils.getTool(player());
	}

	protected void send(CommandSender sender, String message) {
		send(sender, json(message));
	}

	protected void send(CommandSender sender, BaseComponent... baseComponents) {
		sender.sendMessage(baseComponents);
	}

	protected void send(String uuid, String message) {
		send(UUID.fromString(uuid), message);
	}

	protected void send(UUID uuid, String message) {
		OfflinePlayer player = Utils.getPlayer(uuid.toString());
		if (player != null && player.isOnline())
			send((CommandSender) Utils.getPlayer(uuid), message);
	}

	protected void send(Object object) {
		if (object instanceof String)
			send(sender(), (String) object);
		else if (object instanceof JsonBuilder)
			send(sender(), (JsonBuilder) object);
		else
			throw new InvalidInputException("Cannot send object: " + object.getClass().getSimpleName());
	}

	protected void send(CommandSender sender, int delay, String message) {
		Tasks.wait(delay, () -> send(sender, message));
	}

	protected void send() {
		send("");
	}

	protected void send(String message) {
		send(json(message));
	}

	protected void send(JsonBuilder builder) {
		send(sender(), builder);
	}

	protected void send(BaseComponent... baseComponents) {
		send(sender(), baseComponents);
	}

	protected void send(CommandSender sender, JsonBuilder builder) {
		builder.send(sender);
	}

	protected void send(int delay, String message) {
		Tasks.wait(delay, () -> event.reply(message));
	}

	protected void line() {
		line(1);
	}

	protected void line(CommandSender player) {
		line(player, 1);
	}

	protected void line(int count) {
		line(sender(), count);
	}

	protected void line(CommandSender player, int count) {
		for (int i = 0; i < count; i++)
			send(player, "");
	}

	protected JsonBuilder json() {
		return json("");
	}

	protected JsonBuilder json(String message) {
		return new JsonBuilder(message);
	}

	public void error(String error) {
		throw new InvalidInputException(error);
	}

	@Deprecated
	public void error(OfflinePlayer player, String error) {
		if (player.getPlayer() != null && player.isOnline())
			error(player.getPlayer(), error);
	}

	@Deprecated
	public void error(Player player, String error) {
		player.sendMessage(StringUtils.colorize("&c" + error));
	}

	public void showUsage() {
		throw new InvalidInputException(event.getUsageMessage());
	}

	protected CommandSender sender() {
		return event.getSender();
	}

	protected Player player() {
		if (!isPlayer())
			throw new MustBeIngameException();

		return (Player) event.getSender();
	}

	protected OfflinePlayer offlinePlayer() {
		if (!isPlayer())
			throw new MustBeIngameException();

		return Bukkit.getOfflinePlayer(player().getUniqueId());
	}

	protected UUID uuid() {
		if (!isPlayer())
			throw new MustBeIngameException();

		return ((Player) event.getSender()).getUniqueId();
	}

	protected ConsoleCommandSender console() {
		if (!isConsole())
			throw new MustBeConsoleException();

		return (ConsoleCommandSender) event.getSender();
	}

	protected BlockCommandSender commandBlock() {
		if (!isCommandBlock())
			throw new MustBeCommandBlockException();

		return (BlockCommandSender) event.getSender();
	}

	protected boolean isPlayer() {
		return isPlayer(sender());
	}

	private boolean isPlayer(Object object) {
		return object instanceof Player;
	}

	protected boolean isOfflinePlayer() {
		return isOfflinePlayer(sender());
	}

	private boolean isOfflinePlayer(Object object) {
		return object instanceof OfflinePlayer;
	}

	protected boolean isConsole() {
		return isConsole(sender());
	}

	private boolean isConsole(Object object) {
		return object instanceof ConsoleCommandSender;
	}

	protected boolean isCommandBlock() {
		return isCommandBlock(sender());
	}

	private boolean isCommandBlock(Object object) {
		return object instanceof BlockCommandSender;
	}

	protected boolean isSelf(OfflinePlayer player) {
		return isPlayer() && player.getUniqueId().equals(player().getUniqueId());
	}

	protected boolean isSelf(Player player) {
		return isPlayer() && player.getUniqueId().equals(player().getUniqueId());
	}

	protected boolean isNullOrEmpty(String string) {
		return Strings.isNullOrEmpty(string);
	}

	protected void runCommand(String commandNoSlash) {
		runCommand(sender(), commandNoSlash);
	}

	protected void runCommand(CommandSender sender, String commandNoSlash) {
		Utils.runCommand(sender, commandNoSlash);
	}

	protected void runCommandAsOp(String commandNoSlash) {
		runCommandAsOp(sender(), commandNoSlash);
	}

	protected void runCommandAsOp(CommandSender sender, String commandNoSlash) {
		Utils.runCommandAsOp(sender, commandNoSlash);
	}

	protected void runCommandAsConsole(String commandNoSlash) {
		Utils.runCommandAsConsole(commandNoSlash);
	}

	protected void checkPermission(String permission) {
		if (!sender().hasPermission(permission))
			throw new NoPermissionException();
	}

	protected String argsString() {
		return argsString(args());
	}

	protected String argsString(List<String> args) {
		if (args() == null || args().size() == 0) return "";
		return String.join(" ", args());
	}

	protected List<String> args() {
		return event.getArgs();
	}

	protected void setArgs(List<String> args) {
		event.setArgs(args);
	}

	protected String arg(int i) {
		return arg(i, false);
	}

	protected String arg(int i, boolean rest) {
		if (event.getArgs().size() < i) return null;
		if (rest)
			return String.join(" ", event.getArgs().subList(i - 1, event.getArgs().size()));

		String result = event.getArgs().get(i - 1);
		if (Strings.isNullOrEmpty(result)) return null;
		return result;
	}

	protected boolean isIntArg(int i) {
		if (event.getArgs().size() < i) return false;
		return isInt(arg(i));
	}

	protected boolean isInt(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	protected Integer intArg(int i) {
		if (event.getArgs().size() < i) return null;
		try {
			return Integer.parseInt(arg(i));
		} catch (NumberFormatException ex) {
			throw new InvalidInputException("Argument #" + i + " is not a valid integer");
		}
	}

	protected boolean isDoubleArg(int i) {
		if (event.getArgs().size() < i) return false;
		return isDouble(arg(i));
	}

	protected boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	protected Double doubleArg(int i) {
		if (event.getArgs().size() < i) return null;
		try {
			return Double.parseDouble(arg(i));
		} catch (NumberFormatException ex) {
			throw new InvalidInputException("Argument #" + i + " is not a valid double");
		}
	}

	protected boolean isFloatArg(int i) {
		if (event.getArgs().size() < i) return false;
		return isFloat(arg(i));
	}

	protected boolean isFloat(String input) {
		try {
			Float.parseFloat(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	protected Float floatArg(int i) {
		if (event.getArgs().size() < i) return null;
		try {
			return Float.parseFloat(arg(i));
		} catch (NumberFormatException ex) {
			throw new InvalidInputException("Argument #" + i + " is not a valid float");
		}
	}

	protected Boolean booleanArg(int i) {
		if (event.getArgs().size() < i) return null;
		String value = arg(i);
		if (Arrays.asList("enable", "on", "yes", "1").contains(value)) value = "true";
		return Boolean.parseBoolean(value);
	}

	protected boolean isOfflinePlayerArg(int i) {
		if (event.getArgs().size() < i) return false;
		try {
			Utils.getPlayer(arg(i));
			return true;
		} catch (PlayerNotFoundException ex) {
			return false;
		}
	}

	protected OfflinePlayer offlinePlayerArg(int i) {
		if (event.getArgs().size() < i) return null;
		return Utils.getPlayer(arg(i));
	}

	protected boolean isPlayerArg(int i) {
		if (event.getArgs().size() < i) return false;
		try {
			return Utils.getPlayer(arg(i)).isOnline();
		} catch (PlayerNotFoundException ex) {
			return false;
		}
	}

	protected Player playerArg(int i) {
		if (event.getArgs().size() < i) return null;
		OfflinePlayer player = Utils.getPlayer(arg(i));
		if (!player.isOnline())
			throw new PlayerNotOnlineException(player);
		return player.getPlayer();
	}

	protected void fallback() {
		Fallback fallback = getClass().getAnnotation(Fallback.class);
		if (fallback != null)
			Utils.runCommand(sender(), fallback.value() + ":" + event.getAliasUsed() + " " + event.getArgsString());
		else
			throw new InvalidInputException("Nothing to fallback to");
	}

	@ConverterFor(OfflinePlayer.class)
	public OfflinePlayer convertToOfflinePlayer(String value) {
		if ("self".equalsIgnoreCase(value)) value = player().getUniqueId().toString();
		return Utils.getPlayer(value);
	}

	@ConverterFor(Player.class)
	public Player convertToPlayer(String value) {
		OfflinePlayer offlinePlayer = convertToOfflinePlayer(value);
		if (!offlinePlayer.isOnline())
			throw new PlayerNotOnlineException(offlinePlayer);
		if (isPlayer() && !Utils.canSee(player(), offlinePlayer.getPlayer()))
			throw new PlayerNotOnlineException(offlinePlayer);

		return offlinePlayer.getPlayer();
	}

	@TabCompleterFor({Player.class, OfflinePlayer.class})
	public List<String> tabCompletePlayer(String filter) {
		return Bukkit.getOnlinePlayers().stream()
				.filter(player -> Utils.canSee(player(), player))
				.filter(player -> player.getName().toLowerCase().startsWith(filter.toLowerCase()))
				.map(Player::getName)
				.collect(Collectors.toList());
	}

	//	@TabCompleterFor(OfflinePlayer.class)
	public List<String> tabCompleteOfflinePlayer(String filter) {
		List<String> online = tabCompletePlayer(filter);
		if (!online.isEmpty() || filter.length() < 3)
			return online;

		return null;
	}

	@ConverterFor(World.class)
	World convertToWorld(String value) {
		if ("current".equalsIgnoreCase(value))
			return player().getWorld();
		World world = Bukkit.getWorld(value);
		if (world == null)
			throw new InvalidInputException("World from " + value + " not found");
		return world;
	}

	@TabCompleterFor(World.class)
	List<String> tabCompleteWorld(String filter) {
		return Bukkit.getWorlds().stream()
				.filter(world -> world.getName().toLowerCase().startsWith(filter.toLowerCase()))
				.map(World::getName)
				.collect(Collectors.toList());
	}

	@ConverterFor(Material.class)
	Material convertToMaterial(String value) {
		if (isInt(value))
			return Material.values()[Integer.parseInt(value)];

		Material material = Material.matchMaterial(value);
		if (material == null)
			throw new InvalidInputException("Material from " + value + " not found");
		return material;
	}

	protected <T> void paginate(List<T> values, Function<T, JsonBuilder> formatter, String command, int page) {
		paginate(values, formatter, command, page, 10);
	}

	protected <T> void paginate(List<T> values, Function<T, JsonBuilder> formatter, String command, int page, int amount) {
		if (page < 1)
			error("Page number must be 1 or more");

		int start = (page - 1) * amount;
		if (values.size() < start)
			error("No results on page " + page);

		int end = Math.min(values.size(), start + amount);

		line();
		values.subList(start, end).forEach(t -> send(formatter.apply(t)));

		boolean first = page == 1;
		boolean last = end == values.size();

		JsonBuilder buttons = json();
		if (first)
			buttons.next("&8 « Previous  &d");
		else
			buttons.next("&7 « Previous  &d").command(command + " " + (page - 1));

		buttons.group().next("&7|&7|").group();

		if (last)
			buttons.next("  &8Next »");
		else
			buttons.next("  &7Next »").command(command + " " + (page + 1));

		send(buttons.group());
	}

	@Path("help")
	void help() {
		List<String> aliases = getAllAliases();
		if (aliases.size() > 1)
			send(PREFIX + "&dAliases: &7" + String.join("&f, &7", aliases));

		List<JsonBuilder> lines = new ArrayList<>();
		getPathMethods(event).forEach(method -> {
			Path path = method.getAnnotation(Path.class);
			Description desc = method.getAnnotation(Description.class);
			HideFromHelp hide = method.getAnnotation(HideFromHelp.class);

			if (hide != null) return;
			if ("help".equals(path.value()) || "?".equals(path.value())) return;

			String usage = "/" + getAliasUsed().toLowerCase() + " " + (isNullOrEmpty(path.value()) ? "" : path.value());
			String description = (desc == null ? "" : " &8- &7" + desc.value());
			StringBuilder suggestion = new StringBuilder();
			for (String word : usage.split(" ")) {
				if (word.startsWith("[") || word.startsWith("<"))
					break;
				if (word.startsWith("("))
					suggestion.append(trimFirst(word.split("\\|")[0]));
				else
					suggestion.append(word).append(" ");
			}

			lines.add(json("&c" + usage + description).suggest(suggestion.toString()));
		});

		if (lines.size() == 0)
			error("No usage available");

		send(PREFIX + "&dUsage: &7");
		lines.forEach(this::send);
	}

	@ConverterFor(CommandBlockArgs.class)
	public CommandBlockArgs convertToCommandBlockArgs(String value) {
		BlockCommandSender commandBlock;
		Entity sender;
		Location senderLoc;
		if (sender() instanceof BlockCommandSender) {
			commandBlock = (BlockCommandSender) sender();
			senderLoc = commandBlock.getBlock().getLocation();
		} else {
			sender = (Entity) sender();
			senderLoc = sender.getLocation();
		}

		CommandBlockArgs commandBlockArgs = new CommandBlockArgs();

		if (!value.startsWith("@"))
			throw new InvalidInputException("Needs to start with a selector! (@<variable>)");

		// Get selector
		CommandBlockArgs.SelectorType selectorType;
		String selector = value.substring(1, 2).toLowerCase();
		try {
			selectorType = CommandBlockArgs.SelectorType.valueOf(selector.toUpperCase());
		} catch (Exception e) {
			throw new InvalidInputException("Unknown/Unsupported selector: " + selector);
		}
		commandBlockArgs.setSelectorType(selectorType);
		//

		int maxDistance = -1;
		int minDistance = 0;
		boolean exact = false;
		Location origin = senderLoc;
		String destinationStr;

		if (value.contains("[") && value.contains("]")) {
			value = value.replaceAll("\\[", ", ");
			String[] argGroups = value.split("]");
			destinationStr = argGroups[1];

			// Get location from & distance
			argGroups[0] = argGroups[0].substring(4);
			String[] selectorLoc = argGroups[0].split(", ");

			if (selectorLoc.length >= 3) {
				double x = Double.parseDouble(selectorLoc[0].replaceAll("x=", ""));
				double y = Double.parseDouble(selectorLoc[1].replaceAll("y=", ""));
				double z = Double.parseDouble(selectorLoc[2].replaceAll("z=", ""));
				origin = new Location(origin.getWorld(), x, y, z);

				if (selectorLoc.length == 4) {
					String distanceStr = selectorLoc[3].replaceAll("distance=", "");
					if (!distanceStr.matches("(([0-9]+)?(\\.\\.[0-9]+)?)"))
						throw new InvalidInputException("Unknown distance format: " + distanceStr);

					if (distanceStr.contains("..")) {
						exact = false;
						String[] distances = distanceStr.split("\\.\\.");
						if (Strings.isNullOrEmpty(distances[0]))
							distances[0] = "null";

						maxDistance = Integer.parseInt(distances[1]);
						if (!distances[0].equalsIgnoreCase("null"))
							minDistance = Integer.parseInt(distances[0]);

					} else {
						maxDistance = Integer.parseInt(distanceStr);
						exact = true;
					}
				}

			}
		} else {
			String[] argGroups = value.split("@" + selector);
			destinationStr = argGroups[1];
		}

		commandBlockArgs.setOrigin(origin);
		try {
			commandBlockArgs.setTargets(CommandBlockArgs.getSelectorTargets(sender(), selectorType, origin, minDistance, maxDistance, exact));
		} catch (Exception e) {
			throw new InvalidInputException("Invalid target selectors, targets = null");
		}

		if (commandBlockArgs.getTargets().size() == 0) {
			throw new InvalidInputException("No targets found");
		}

		// Get to location & facing
		if (Strings.isNullOrEmpty(destinationStr)) {
			throw new InvalidInputException("Invalid destination, destination = null");
		}

		try {
			destinationStr = destinationStr.trim();
			String[] destinationArgs = destinationStr.split(" ");
			double dest_x = Double.parseDouble(destinationArgs[0]);
			double dest_y = Double.parseDouble(destinationArgs[1]);
			double dest_z = Double.parseDouble(destinationArgs[2]);
			Location destination = new Location(origin.getWorld(), dest_x, dest_y, dest_z);

			Float dest_yaw = null;
			Float dest_pitch = null;
			if (destinationArgs.length == 5) {
				dest_yaw = Float.parseFloat(destinationArgs[3]);
				dest_pitch = Float.parseFloat(destinationArgs[4]);
				destination = new Location(origin.getWorld(), dest_x, dest_y, dest_z, dest_yaw, dest_pitch);
			}

			commandBlockArgs.setDestination(destination);
			commandBlockArgs.setDestinationYaw(dest_yaw);
			commandBlockArgs.setDestinationPitch(dest_pitch);
		} catch (Exception e) {
			throw new InvalidInputException("Invalid destination, could not parse destination");
		}
		//

//		Utils.wakka("");
//		Utils.wakka("selector: " + commandBlockArgs.getSelectorType());
//		Utils.wakka("origin: " + StringUtils.getShortLocationString(origin));
//		Utils.wakka("minDistance: " + minDistance);
//		Utils.wakka("maxDistnace: " + maxDistance);
//		Utils.wakka("Exact: " + exact);
//		Utils.wakka("Targets: " + (commandBlockArgs.getTargets() != null ? commandBlockArgs.getTargets().toString() : "null"));
//		Utils.wakka("Destination: " + StringUtils.getShortLocationString(commandBlockArgs.getDestination()));

		return commandBlockArgs;

	}

}
