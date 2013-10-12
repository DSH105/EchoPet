package io.github.dsh105.echopet.commands;

import io.github.dsh105.echopet.EchoPet;
import org.apache.commons.lang.Validate;
import org.bukkit.command.*;

import java.util.List;

public class CustomCommand extends Command {
	
	private CommandExecutor executor;
	private TabCompleter completer;
	
	public CustomCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		if (executor != null) {
			executor.onCommand(arg0, this, arg1, arg2);
		}
		return false;
	}
	
	public void setExecutor(CommandExecutor exe) {
		this.executor = exe;
	}

	public void setTabCompleter(TabCompleter completer) {
		this.completer = completer;
	}

	public TabCompleter getTabCompleter() {
		return completer;
	}

	@Override
	public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args) throws CommandException, IllegalArgumentException {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "Arguments cannot be null");
		Validate.notNull(alias, "Alias cannot be null");

		List<String> completions = null;
		try {
			if (completer != null) {
				completions = completer.onTabComplete(sender, this, alias, args);
			}
			if (completions == null && executor instanceof TabCompleter) {
				completions = ((TabCompleter) executor).onTabComplete(sender, this, alias, args);
			}
		} catch (Throwable ex) {
			StringBuilder message = new StringBuilder();
			message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
			for (String arg : args) {
				message.append(arg).append(' ');
			}
			message.deleteCharAt(message.length() - 1).append("' in plugin ").append(EchoPet.getPluginInstance().getDescription().getFullName());
			throw new CommandException(message.toString(), ex);
		}

		if (completions == null) {
			return super.tabComplete(sender, alias, args);
		}
		return completions;
	}
}