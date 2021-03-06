package me.wakka.valeriaonline.framework.commands.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.wakka.valeriaonline.framework.commands.Commands;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.annotations.TabCompleteIgnore;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;
import me.wakka.valeriaonline.framework.commands.models.events.TabEvent;
import me.wakka.valeriaonline.framework.exceptions.CustomException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;
import static me.wakka.valeriaonline.utils.StringUtils.left;

@Data
class PathParser {
	@NonNull
	CommandEvent event;
	CustomCommand command;
	List<Method> methods;

	public PathParser(@NonNull CommandEvent event) {
		this.event = event;
		this.command = event.getCommand();
		this.methods = command.getPathMethods(event);
		Collections.reverse(methods);
	}

	@Data
	class TabCompleteHelper {
		private Method method;
		private List<String> pathArgs;
		private List<String> realArgs;
		private List<TabCompleteArg> args = new ArrayList<>();
		private Class<?> finalType;
		private Method finalTabCompleter;
		private Object finalContextArg;

		public TabCompleteHelper(Method method, List<String> realArgs) {
			this.method = method;
			this.realArgs = realArgs;
			this.pathArgs = Arrays.asList(method.getAnnotation(Path.class).value().split(" "));

			createArgs();
		}

		private void createArgs() {
			int index = 0;
			int paramIndex = 0;
			for (String realArg : realArgs) {
				TabCompleteArg arg = new TabCompleteArg(index, realArg);
				if (pathArgs.size() > index)
					arg.setPathArg(pathArgs.get(index));
				if (realArgs.size() == index + 1)
					arg.isCompletionIndex(true);

				if (arg.isVariable()) {
					arg.setParamIndex(paramIndex++);
					Parameter parameter = method.getParameters()[arg.getParamIndex()];
					Arg annotation = parameter.getAnnotation(Arg.class);
					if (annotation != null && !isNullOrEmpty(annotation.permission()))
						if (!event.getSender().hasPermission(annotation.permission()))
							break;

					arg.setTabCompleter(parameter.getType());
					arg.setList(Collection.class.isAssignableFrom(parameter.getType()));
					if (annotation != null) {
						if (List.class.isAssignableFrom(parameter.getType()) && annotation.type() != void.class)
							arg.setTabCompleter(annotation.type());
						if (annotation.tabCompleter() != void.class)
							arg.setTabCompleter(annotation.tabCompleter());
						if (annotation.context() > 0)
							arg.setContextArg(command.getMethodParameters(method, event, false)[annotation.context() - 1]);
					}

					if (finalTabCompleter == null) {
						if (arg.getPathArg() != null && arg.getPathArg().contains("...")) {
							finalType = arg.getType();
							finalTabCompleter = arg.getTabCompleter();
							finalContextArg = arg.getContextArg();
						}
					}
				}

				if (finalType != null)
					arg.setType(finalType);
				if (finalTabCompleter != null)
					arg.setTabCompleter(finalTabCompleter);
				if (finalContextArg != null)
					arg.setContextArg(finalContextArg);

				args.add(arg);
				++index;
			}
		}

		@ToString.Include
		boolean pathMatches() {
			for (TabCompleteArg arg : args)
				if (!arg.matches())
					return false;

			return true;
		}

		@ToString.Include
		List<String> tabComplete() {
			for (TabCompleteArg arg : args)
				if (arg.isCompletionIndex())
					return arg.tabComplete();

			return new ArrayList<>();
		}
	}

	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	class TabCompleteArg {
		@NonNull
		private int index;
		@NonNull
		private String realArg;
		private String pathArg;
		@Accessors(fluent = true)
		private boolean isCompletionIndex = false;
		private Integer paramIndex;

		private Class<?> type;
		private boolean isList;
		private Method tabCompleter;
		private Object contextArg;

		@ToString.Include
		boolean isLiteral() {
			if (pathArg == null) return false;
			return !pathArg.startsWith("[") && !pathArg.startsWith("<");
		}

		@ToString.Include
		boolean isVariable() {
			if (pathArg == null) return false;
			return !isLiteral();
		}

		@ToString.Include
		boolean matches() {
			if (isNullOrEmpty(pathArg) || isVariable())
				return true;

			if (isCompletionIndex)
				if (isNullOrEmpty(realArg))
					return true;
				else if (isLiteral())
					return getSplitPathArg(realArg).size() > 0;

			for (String option : tabComplete())
				if (option.equalsIgnoreCase(realArg))
					return true;

			return false;
		}

		private List<String> getSplitPathArg(String filter) {
			List<String> options = Arrays.asList(pathArg.replaceAll("\\(", "").replaceAll("\\)", "").split("\\|"));
			if (filter != null)
				options = options.stream().filter(option -> option.toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
			return options;
		}

		@ToString.Include
		@SneakyThrows
		List<String> tabComplete() {
			if (isLiteral())
				return getSplitPathArg(realArg);

			String realArg = this.realArg;
			List<String> results = new ArrayList<>();

			String splitter = (realArg.contains(",") ? "," : " ");
			if (isList) {
				if (realArg.lastIndexOf(splitter) == realArg.length() - 1)
					realArg = "";
				else {
					String[] split = realArg.split(splitter);
					realArg = split[split.length - 1];
				}
			}

			if (tabCompleter != null) {
				CustomCommand tabCompleteCommand = command;
				if (!(tabCompleter.getDeclaringClass().equals(command.getClass()) || Modifier.isAbstract(tabCompleter.getDeclaringClass().getModifiers())))
					tabCompleteCommand = command.getNewCommand(command.getEvent(), tabCompleter.getDeclaringClass());

				if (tabCompleter.getParameterCount() == 1) {
					results.addAll((List<String>) tabCompleter.invoke(tabCompleteCommand, realArg.toLowerCase()));
				} else if (tabCompleter.getParameterCount() == 2)
					results.addAll((List<String>) tabCompleter.invoke(tabCompleteCommand, realArg.toLowerCase(), contextArg));
				else
					throw new CustomException("Unknown converter parameters in " + tabCompleter.getName());
			} else if (type != null && type.isEnum())
				results.addAll(command.tabCompleteEnum((Class<? extends Enum<?>>) type, realArg.toLowerCase()));

			if (isList) {
				List<String> realArgs = new ArrayList<>(Arrays.asList(this.realArg.split(splitter)));
				if (!this.realArg.endsWith(splitter))
					realArgs.remove(realArgs.size() - 1);
				String realArgBeginning = String.join(splitter, realArgs);
				if (realArgs.size() > 0)
					realArgBeginning += splitter;

				ArrayList<String> strings = new ArrayList<>(results);
				results.clear();
				for (String result : strings)
					results.add(realArgBeginning + result);
			}

			return results;
		}

		void setTabCompleter(Method tabCompleter) {
			this.tabCompleter = tabCompleter;
		}

		void setTabCompleter(Class<?> clazz) {
			this.type = clazz;
			if (Commands.getTabCompleters().containsKey(clazz))
				this.tabCompleter = Commands.getTabCompleters().get(clazz);
		}

	}

	List<String> tabComplete(TabEvent event) {
		List<String> completions = new ArrayList<>();

		for (Method method : methods) {
			if (!event.getCommand().hasPermission(event.getSender(), method))
				continue;
			TabCompleteIgnore tabCompleteIgnore = method.getAnnotation(TabCompleteIgnore.class);
			if (tabCompleteIgnore != null)
				if (isNullOrEmpty(tabCompleteIgnore.permission()) || !event.getSender().hasPermission(tabCompleteIgnore.permission()))
					continue;

			TabCompleteHelper helper = new TabCompleteHelper(method, event.getArgs());
//			ValeriaOnline.log(helper.toString());
			if (!helper.pathMatches())
				continue;

			completions.addAll(helper.tabComplete());
		}

		return completions.stream().distinct().collect(Collectors.toList());
	}

	Method match(List<String> args) {
		String argsString = String.join(" ", args).toLowerCase();

		// Look for exact match
		for (Method method : methods)
			if (method.getAnnotation(Path.class).value().equalsIgnoreCase(argsString))
				return method;

		Method fallback = null;

		for (Method method : methods) {
			String path = getPathString(method);
			String literalWords = getLiteralWords(path);

			if (literalWords.length() == 0) {
				if (args.size() > 0 && path.length() > 0)
					if (path.split(" ").length <= args.size())
						return method;

				if (args.size() == 0 && path.length() == 0)
					return method;

				if (fallback == null) {
					if (args.size() >= getRequiredArgs(path))
						fallback = method;
					else if (args.size() == 0 && getLiteralWords(path).length() == 0)
						fallback = method;
				} else if (args.size() == 0 && getPathString(method).length() < getPathString(fallback).length())
					fallback = method;
				continue;
			}

			// Has arguments, has literal worlds
			Matcher matcher = Pattern.compile("^" + literalWords + " .*").matcher(argsString + " ");
			if (matcher.matches())
				return method;
		}

		return fallback;
	}

	protected static String getPathString(Method method) {
		return method.getAnnotation(Path.class).value().toLowerCase();
	}

	protected static String getLiteralWords(String path) {
		String[] pathArgs = path.split(" ");

		String literalWords = "";
		if (path.length() > 0)
			for (String pathArg : pathArgs)
				switch (left(pathArg, 1)) {
					case "[":
					case "<":
						break;
					default:
						literalWords += pathArg + " ";
				}

		literalWords = literalWords.trim().toLowerCase();
		return literalWords;
	}

	protected static int getRequiredArgs(String path) {
		String[] pathArgs = path.split(" ");

		int requiredArgs = 0;
		if (path.length() > 0)
			for (String pathArg : pathArgs)
				if (!pathArg.startsWith("["))
					++requiredArgs;

		return requiredArgs;
	}
}

