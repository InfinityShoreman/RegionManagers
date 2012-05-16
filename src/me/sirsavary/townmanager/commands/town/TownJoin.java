package me.sirsavary.townmanager.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Town;

public class TownJoin extends AbstractCommand {

	private final Town townToJoin;
	Town t = Main.fileManager.getPlayerTown((Player) sender);
	
	public TownJoin(CommandSender sender, boolean async, Main plugin, String TownToJoin)
			throws Exception {
		super(sender, async, plugin);
		townToJoin = Main.fileManager.getTown(TownToJoin);
	}

	@Override
	public void run() {
		if (t == null) {
			if (townToJoin != null) {
				Main.fileManager.AddCitizen(sender.getName(), townToJoin);
				Main.server.getPlayer(townToJoin.getMayor()).sendMessage(Chatter.Message(sender.getName() + " has joined " + townToJoin.getColor() + townToJoin.getID() + Main.messageColor + "!"));
			}
			else {
				sender.sendMessage(Chatter.TagMessage("That town does not exist!"));
			}
		}
		else {
			sender.sendMessage(Chatter.TagMessage("You are already part of a town!"));
		}
	}

}
