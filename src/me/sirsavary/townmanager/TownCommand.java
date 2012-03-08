package me.sirsavary.townmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TownCommand implements CommandExecutor{
	
	Main plugin;
	
    public TownCommand(Main plugin) 
    {
        this.plugin = plugin;
    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0)
		{
			
			String playerTown = (String) Main.fileManager.getValue("Players." + sender.getName() + ".Town");
			sender.sendMessage("Welcome to the TownManager interface!");
			if (playerTown == null)
			{
				sender.sendMessage("Currently, you are not part of a town!");
				sender.sendMessage("To create a town, type /town create");
				sender.sendMessage("To join a town, type /town join [TownName]");
				sender.sendMessage("If you cannot find a town to join, ask around or type /town list");
			}
			else
			{
				sender.sendMessage("Currently, you are a citizen of " + playerTown + "!");
				sender.sendMessage("For information on " + playerTown + ", type /town info");
				sender.sendMessage("To teleport to " + playerTown + "'s town square, type /town tp");
				sender.sendMessage("To leave " + playerTown + ", type /town leave");
			}

		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("info"))
			{
				
			}
			else if (args[0].equalsIgnoreCase("create"))
			{
				
			}
			else if (args[0].equalsIgnoreCase("delete"))
			{
				
			}
			else if (args[0].equalsIgnoreCase("join"))
			{
				
			}
			else if (args[0].equalsIgnoreCase("leave"))
			{
				
			}
			else if (args[0].equalsIgnoreCase("tp"))
			{
				
			}
			else if (args[0].equalsIgnoreCase("list"))
			{
				
			}
		}
		else if (args.length == 2)
		{
			
		}
		else if (args.length == 3)
		{
			
		}
		return false;
	}

}
