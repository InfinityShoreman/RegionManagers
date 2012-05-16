package me.sirsavary.townmanager.commands.town;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.entity.Player;

public class TownKick {

	
	public TownKick(Player player, String playerToKick) {
		Player p = player;
		
		Town t = Main.fileManager.getPlayerTown(p);
		if (t == null) {
			p.sendMessage(Chatter.TagMessage("You are not part of a town!"));
		}
		else if (!t.getMayor().equalsIgnoreCase(p.getName())) {
			p.sendMessage(Chatter.TagMessage("You are not the mayor of " + t.getFormattedID() + "!"));
		}
		else {
			if (!t.getCitizens().contains(playerToKick)) {
				p.sendMessage(Chatter.TagMessage(playerToKick + " is not a part of " + t.getFormattedID() + "!"));
			}
			else {
				Main.fileManager.RemoveCitizen(playerToKick, t);
				p.sendMessage(Chatter.TagMessage(playerToKick + " has been kicked from " + t.getFormattedID() + "!"));
				if (Main.server.getPlayerExact(playerToKick) != null) Main.server.getPlayerExact(playerToKick).sendMessage("You have been kicked from" + t.getFormattedID() + "!");
			}
		}
	}
}