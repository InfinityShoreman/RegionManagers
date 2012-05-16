package me.sirsavary.townmanager.commands.town;

import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Town;
import me.sirsavary.townmanager.objects.TownChunk;

public class TownUnclaim {

	public TownUnclaim(Player player) {
		Player p = player;
		Town t = Main.fileManager.getPlayerTown(p);
		if (t == null) {
			p.sendMessage(Chatter.TagMessage("You are not part of a town!"));
		}
		else if (!t.getMayor().equalsIgnoreCase(p.getName())) {
			p.sendMessage(Chatter.TagMessage("You are not the mayor of " + t.getFormattedID() + "!"));
		}
		else {
			TownChunk tc = Main.fileManager.getTownChunkAtChunk(p.getLocation().getChunk()); //TownChunk(p.getLocation().getChunk(), null, t.getCountry(), t.getID());
			
			if (tc == null) {
				p.sendMessage(Chatter.Message("This chunk has not been claimed!"));
			}
			else {
				if (Main.fileManager.getTownAtChunk(p.getLocation().getChunk()).getID().equalsIgnoreCase(t.getID())) {
					if (tc.getPlots() != null) {
						p.sendMessage(Chatter.Message("This chunk has plots and cannot be unclaimed!"));
					}
					else {
						Main.fileManager.UnTrackChunk(tc);
						p.sendMessage("Chunk unclaimed!");
					}
				}
				else {
					p.sendMessage(Main.fileManager.getTownAtChunk(p.getLocation().getChunk()).getFormattedID() + " owns this chunk!");
				}
			}
		}
	}
}
