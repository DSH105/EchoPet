package com.github.dsh105.echopet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CustomCommand extends Command {
	
	private CommandExecutor exe;
	
	public CustomCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		if (exe != null) {
			exe.onCommand(arg0, this, arg1, arg2);
		}
		return false;
	}
	
	public void setExecutor(CommandExecutor exe) {
		this.exe = exe;
	}
	
}
