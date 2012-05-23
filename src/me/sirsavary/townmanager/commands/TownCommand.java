package me.sirsavary.townmanager.commands;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.TownMapRenderer;
import me.sirsavary.townmanager.commands.town.TownClaim;
import me.sirsavary.townmanager.commands.town.TownCreate;
import me.sirsavary.townmanager.commands.town.TownInvite;
import me.sirsavary.townmanager.commands.town.TownJoin;
import me.sirsavary.townmanager.commands.town.TownLeave;
import me.sirsavary.townmanager.commands.town.TownRemove;
import me.sirsavary.townmanager.commands.town.TownSettings;
import me.sirsavary.townmanager.commands.town.TownTeleport;
import me.sirsavary.townmanager.commands.town.TownUnclaim;
import me.sirsavary.townmanager.commands.town.admin.TownAdminRemove;
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
			if (t == null) {
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
		else if (args.length > 0) {
			if (sender instanceof Player) {
				if (args[0].equalsIgnoreCase("mayor")) {
					MayorCommand(p, args);
				} else if (args[0].equalsIgnoreCase("admin")) {
					AdminCommand(p, args);
				} else if (args[0].equalsIgnoreCase("plot")) {
					PlotCommand(p, args);
				} else {
					CitizenCommand(p, args);
				}
			} else {
				if (args[0].equalsIgnoreCase("admin")) {
					AdminCommand(p, args);
				} else {
					sender.sendMessage("You cannot use that command from console!");
				}
			}
		}
		return true;
	}
	private void CitizenCommand(Player player, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("join")) {
				player.sendMessage(Chatter.TagMessage("You must specify a town to join!"));
			} else if (args[0].equalsIgnoreCase("chunk")) {
				player.sendMessage("Chunk is at cords (" + player.getLocation().getChunk().getX() + ","  + player.getLocation().getChunk().getZ() + ")");
			} else if (args[0].equalsIgnoreCase("map")) {
				TownMapRenderer.CreateMap(player);
			} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("home")) {
				new TownTeleport(player);
			} else if (args[0].equalsIgnoreCase("list")) {
				ListTowns(player);
			} else if (args[0].equalsIgnoreCase("create")) {
				try {
					new TownCreate(player, true, plugin);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (args[0].equalsIgnoreCase("leave")) {
				try {
					new TownLeave(player, true, plugin);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				player.sendMessage(Chatter.TagMessage("Not a valid command!"));
			}
		}
		else if (args.length == 2)
			if (args[0].equalsIgnoreCase("join")) {
				String townName = null;
				for (int x = 1; x < args.length; x++) {
					townName = townName + args[x] + " ";
				}
				townName.trim();
				try {
					new TownJoin(player, true, plugin, townName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	private void PlotCommand(Player player, String[] args) {
		if (args.length <= 1) {
			PlotHelp(player);
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("new")) {
				;
			} else if (args[1].equalsIgnoreCase("select")) {
				player.sendMessage(Chatter.TagMessage("Please choose a plot to select!"));
			} else {
				player.sendMessage(Chatter.TagMessage("Not a valid command!"));
			}
		}
		else if (args.length == 3) {
			if (args[1].equalsIgnoreCase("remove")) {
				;
			} else if (args[1].equalsIgnoreCase("addmember")) {
				;
			} else if (args[1].equalsIgnoreCase("delmember")) {
				;
			} else if (args[1].equalsIgnoreCase("info")) {
				;
			} else {
				player.sendMessage(Chatter.TagMessage("Not a valid command!"));
			}
		}
	}
	private void MayorCommand(Player player, String[] args) {
		if (args.length == 1) {
			MayorHelp(player);
		}
		else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("mayor"))
				if (args[1].equalsIgnoreCase("claim")) {
					new TownClaim(player);
				} else if (args[1].equalsIgnoreCase("unclaim")) {
					new TownUnclaim(player);
				} else if (args[1].equalsIgnoreCase("invite")) {
					player.sendMessage(Chatter.TagMessage("Not enough args! Must specify player to invite!"));
				} else if (args[1].equalsIgnoreCase("kick")) {
					player.sendMessage(Chatter.TagMessage("Not enough args! Must specify player to kick!"));
				} else if (args[1].equalsIgnoreCase("plot")) {
					PlotHelp(player);
				} else if (args[1].equalsIgnoreCase("settings")) {
					try {
						new TownSettings(player, true, plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (args[1].equalsIgnoreCase("remove")) {
					try {
						new TownRemove(player, true, plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					player.sendMessage(Chatter.TagMessage("Not a valid command!"));
				}
		}
		else if (args.length == 3)
			if (args[1].equalsIgnoreCase("invite")) {
				try {
					new TownInvite(player, true, plugin, args[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	private void AdminCommand(Player player, String[] args) {
		if (args.length == 1) {
			AdminHelp(player);
		}
		else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("remove")) {
				player.sendMessage(Chatter.TagMessage("Not enough args! Must specify town to remove!"));
			} else {
				player.sendMessage(Chatter.TagMessage("Not a valid command!"));
			}
		}
		else if (args.length >= 3)
			if (args[1].equalsIgnoreCase("remove")) {
				String townName = null;
				for (int x = 2; x < args.length; x++) {
					townName = townName + args[x] + " ";
				}
				townName.trim();
				try {
					new TownAdminRemove(player, true, plugin, args[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	private void PlotHelp(Player p) {
		p.sendMessage(Chatter.Message("/town plot new [ID]- Makes a new plot"));
		p.sendMessage(Chatter.Message("/town plot remove [ID]"));
		p.sendMessage(Chatter.Message("/town plot addowner [ID] [OWNER] - Adds an owner to a plot"));
		p.sendMessage(Chatter.Message("/town plot delowner [ID] [OWNER] - Removes an owner from a plot"));
		p.sendMessage(Chatter.Message("/town plot info - Gets the info from the plot you are standing in"));
		p.sendMessage(Chatter.Message("/town plot info [ID] - Gets the info from a plot"));
	}
	private void AdminHelp(Player p) {
		p.sendMessage(Chatter.Message("/town plot new [ID]- Makes a new plot"));
		p.sendMessage(Chatter.Message("/town plot remove [ID]"));
		p.sendMessage(Chatter.Message("/town plot addowner [ID] [OWNER] - Adds an owner to a plot"));
		p.sendMessage(Chatter.Message("/town plot delowner [ID] [OWNER] - Removes an owner from a plot"));
		p.sendMessage(Chatter.Message("/town plot info - Gets the info from the plot you are standing in"));
		p.sendMessage(Chatter.Message("/town plot info [ID] - Gets the info from a plot"));
	}
	private void MayorHelp(Player p) {
		p.sendMessage(Main.messageColor + "-----" + Main.tagColor + "[TownManager Help]" + Main.messageColor + "-----");
		p.sendMessage(Chatter.Message("/town mayor invite [Player] - Invite a player"));
		p.sendMessage(Chatter.Message("/town mayor claim - Claim a chunk"));
		p.sendMessage(Chatter.Message("/town mayor unclaim - Unclaim a chunk"));
		p.sendMessage(Chatter.Message("/town mayor settings - Edit town settings"));
	}
	private void ListTowns(Player player) {
		String s = "";
		if (Main.fileManager.getTowns() == null) {
			player.sendMessage(Chatter.TagMessage("There are no available towns!"));
		} else {
			for (Town town : Main.fileManager.getTowns())
				if (s.equalsIgnoreCase("")) {
					s = town.getFormattedID();
				} else {
					s = s + ", " + town.getFormattedID();
				}
			player.sendMessage(Chatter.TagMessage("Available Towns:"));
			player.sendMessage(Chatter.Message(s));
		}
	}
}