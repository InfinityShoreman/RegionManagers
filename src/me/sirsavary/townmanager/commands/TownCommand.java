package me.sirsavary.townmanager.commands;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCommand implements CommandExecutor{
	
	private final Main plugin;
	
	public TownCommand(Main plugin) 
    {
        this.plugin = plugin;
    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String playerTown = (String) Main.fileManager.getValue("Players." + sender.getName() + ".Town");
		Player p = (Player) sender;
		if (args.length == 0)
		{
			if (playerTown == null)
			{
				sender.sendMessage("Currently, you are not part of a town!");
				sender.sendMessage("To create a town, type /town create");
				sender.sendMessage("To join a town, type /town join [TownName]");
				sender.sendMessage("If you cannot find a town to join, ask around or type /town list");
			}
			else 
			{
				Town t = new Town(playerTown);
				if (t.getMayor() == p) {
					p.sendMessage("You are the mayor of " + playerTown);
					p.sendMessage(Main.fileManager.getValue(playerTown + ".TotalResidents").toString() + " total residents");
					p.sendMessage("Is " + Main.fileManager.getValue("Towns." + playerTown + ".Size").toString() + " chunks in size");
					p.sendMessage("Has " + Main.fileManager.getValue(playerTown + ".TownPoints").toString() + " town points");
					p.sendMessage("Has " + Main.fileManager.getValue(playerTown + ".Coffers").toString() + " dollars in the coffers");
					p.sendMessage(Chatter.Message("Type /town mayor for more info"));
				}
				else {
					p.sendMessage("You are a resident of " + playerTown);
					p.sendMessage(Main.fileManager.getValue(playerTown + ".TotalResidents").toString() + " total residents");
					p.sendMessage("Is " + Main.fileManager.getValue("Towns." + playerTown + ".Size").toString() + " chunks in size");
					p.sendMessage("Has " + Main.fileManager.getValue(playerTown + ".TownPoints").toString() + " town points");
					p.sendMessage("Has " + Main.fileManager.getValue(playerTown + ".Coffers").toString() + " dollars in the coffers");
					p.sendMessage(Chatter.Message("Type /town help for more info"));
				}
			}
		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("info"))
			{
				if (playerTown == null)
				{
					p.sendMessage("Currently, you are not part of a town!");
					p.sendMessage("To create a town, type /town create");
					p.sendMessage("To join a town, type /town join [TownName]");
					p.sendMessage("If you cannot find a town to join, ask around or type /town list");
				}
				else 
				{
					p.sendMessage("You are a resident of " + playerTown + "which:");
					p.sendMessage(Main.fileManager.getValue(playerTown + ".TotalResidents").toString() + " total residents");
					p.sendMessage("Is " + Main.fileManager.getValue(playerTown + ".Size").toString() + " chunks in size");
					p.sendMessage("Has " + Main.fileManager.getValue(playerTown + ".TownPoints").toString() + " town points");
					p.sendMessage("Has " + Main.fileManager.getValue(playerTown + ".Coffers").toString() + "dollars in the coffers");
				}
				return true;
				
			}
			else if (args[0].equalsIgnoreCase("create"))
			{			
				if (sender instanceof Player) {
					try {
						new TownCreate(sender, true, plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				}
				else {
					sender.sendMessage("This command cannot be run from the console");
				}	
				return true;
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
		else if (args.length == 2)
		{
			
		}
		else if (args.length == 3)
		{
			
		}
		return false;
	}
}
