package me.sirsavary.townmanager.commands;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.town.TownClaim;
import me.sirsavary.townmanager.commands.town.TownCreate;
import me.sirsavary.townmanager.commands.town.TownInvite;
import me.sirsavary.townmanager.commands.town.TownJoin;
import me.sirsavary.townmanager.commands.town.TownLeave;
import me.sirsavary.townmanager.commands.town.TownRemove;
import me.sirsavary.townmanager.commands.town.TownSettings;
import me.sirsavary.townmanager.commands.town.TownTeleport;
import me.sirsavary.townmanager.commands.town.TownUnclaim;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCommand implements CommandExecutor {

	private final Main plugin;

	public TownCommand(Main plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Town t = Main.fileManager.getPlayerTown((Player) sender);
		Player p = (Player) sender;
		if (args.length == 0)
		{
			if (t == null)
			{
				sender.sendMessage(Chatter.Message("Currently, you are not part of a town!"));
				sender.sendMessage(Chatter.Message("To create a town, type /town create"));
				sender.sendMessage(Chatter.Message("To join a town, type /town join [TownName]"));
				sender.sendMessage(Chatter.Message("If you cannot find a town to join, ask around or type /town list"));
			} else if (t.getMayor().equalsIgnoreCase(p.getName())) {
				p.sendMessage(Chatter.Message("You are the mayor of " + t.getColor() + t.getID()));
				p.sendMessage(Chatter.Message(t.getCitizens().size() + " total residents"));
				p.sendMessage(Chatter.Message("Is " + t.getSize() + " chunks in size"));
				p.sendMessage(Chatter.Message(t.getHealth() + " town points"));
				p.sendMessage(Chatter.Message(t.getCoffers() + " dollars in the coffers"));
				p.sendMessage(Chatter.Message("Type /town mayor for more info"));
			}
			else {
				p.sendMessage(Chatter.Message("You are a resident of " + t.getColor() + t.getID()));
				p.sendMessage(Chatter.Message(t.getCitizens().size() + " total residents"));
				p.sendMessage(Chatter.Message("Is " + t.getSize() + " chunks in size"));
				p.sendMessage(Chatter.Message(t.getHealth() + " town points"));
				p.sendMessage(Chatter.Message(t.getCoffers() + " dollars in the coffers"));
				p.sendMessage(Chatter.Message("Type /town help for more info"));
			}
		}
		else if (args.length == 1)
		{
			if (sender instanceof Player) {
				if (args[0].equalsIgnoreCase("mayor"))
				{
					sender.sendMessage(Main.messageColor + "-----" + Main.tagColor + "[TownManager Help]" + Main.messageColor + "-----");
					sender.sendMessage(Chatter.Message("/town mayor invite [Player] - Invite a player"));
					sender.sendMessage(Chatter.Message("/town mayor claim - Claim a chunk"));
					sender.sendMessage(Chatter.Message("/town mayor unclaim - Unclaim a chunk"));
					sender.sendMessage(Chatter.Message("/town mayor settings - Edit town settings"));
					sender.sendMessage(Chatter.Message("/town mayor plot - Top level plot command"));

				}
				else if (args[0].equalsIgnoreCase("join")) sender.sendMessage(Chatter.TagMessage("You must specify a town to join!"));

				else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("home")) new TownTeleport(p);
				else if (args[0].equalsIgnoreCase("list")) ListTowns(p);
				else if (args[0].equalsIgnoreCase("create")) {
					try {
						new TownCreate(sender, true, plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if (args[0].equalsIgnoreCase("leave")) {
					try {
						new TownLeave(sender, true, plugin, p);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else sender.sendMessage(Chatter.TagMessage("Not a valid command!"));
			}
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("join"))
				try {
					new TownJoin(sender, true, plugin, args[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			else if (args[0].equalsIgnoreCase("mayor"))
				if (args[1].equalsIgnoreCase("claim")) new TownClaim(p);
				else if (args[1].equalsIgnoreCase("unclaim")) new TownUnclaim(p);
				else if (args[1].equalsIgnoreCase("invite")) p.sendMessage(Chatter.TagMessage("Not enough args! Must specify player to invite!"));
				else if (args[1].equalsIgnoreCase("kick")) p.sendMessage(Chatter.TagMessage("Not enough args! Must specify player to kick!"));
				else if (args[1].equalsIgnoreCase("plot")) PlotHelp(p);
				else if (args[1].equalsIgnoreCase("settings")) {
					try {
						new TownSettings(sender, true, plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if (args[1].equalsIgnoreCase("remove")) {
					try {
						new TownRemove(sender, true, plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else sender.sendMessage(Chatter.TagMessage("Not a valid command!"));
		}
			else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("mayor")) {
					if (args[1].equalsIgnoreCase("plot")) { //TODO
						if (args[2].equalsIgnoreCase("new")) {

						}
						else if (args[2].equalsIgnoreCase("remove")) {

						}
						else if (args[2].equalsIgnoreCase("addowner")) {

						}
						else if (args[2].equalsIgnoreCase("delowner")) {

						}
						else if (args[2].equalsIgnoreCase("info")) {

						} 
						else PlotHelp(p);
					}
					else if (args[1].equalsIgnoreCase("invite"))
						try {
							new TownInvite(sender, true, plugin, args[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					else sender.sendMessage(Chatter.TagMessage("Not a valid command!"));
				}

			}
		return true;
	}

	private void PlotHelp(Player p) {
		p.sendMessage(Chatter.Message("/town mayor plot new [ID]- Makes a new plot"));
		p.sendMessage(Chatter.Message("/town mayor plot remove [ID]"));
		p.sendMessage(Chatter.Message("/town mayor plot addowner [ID] [OWNER] - Adds an owner to a plot"));
		p.sendMessage(Chatter.Message("/town mayor plot delowner [ID] [OWNER] - Removes an owner from a plot"));
		p.sendMessage(Chatter.Message("/town mayor plot info - Gets the info from the plot you are standing in"));
		p.sendMessage(Chatter.Message("/town mayor plot info [ID] - Gets the info from a plot"));
	}
	private void ListTowns(Player player) {
		String s = "";
		if (Main.fileManager.getTowns() == null)
			player.sendMessage(Chatter.TagMessage("There are no available towns!"));
		else {
			for (Town town : Main.fileManager.getTowns()) {
				if (s.equalsIgnoreCase("")) s = town.getFormattedID();
				else s = s + "," + town.getFormattedID();
				player.sendMessage(Chatter.TagMessage("Available Towns:"));
				player.sendMessage(Chatter.Message(s));
			}
		}
	}
}