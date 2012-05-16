package me.sirsavary.townmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MayorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Player used /town mayor
		if (args.length == 1) {
			
		}
		else if (args.length == 2) {
			
		}
		return false;
	}
}
