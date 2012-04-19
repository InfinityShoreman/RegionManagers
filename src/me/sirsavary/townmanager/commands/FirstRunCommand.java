package me.sirsavary.townmanager.commands;

import me.sirsavary.townmanager.Chatter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FirstRunCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(Chatter.TagMessage("First run detected! Plugin disabled until next restart!"));
		return true;
	}
}
