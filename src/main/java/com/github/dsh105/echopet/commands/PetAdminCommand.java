package com.github.dsh105.echopet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.dsh105.echopet.EchoPet;

public class PetAdminCommand implements CommandExecutor {
	
	private EchoPet ec;
	
	public PetAdminCommand(EchoPet ec) {
		this.ec = ec;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//TODO: Admin commands
		return false;
	}
}