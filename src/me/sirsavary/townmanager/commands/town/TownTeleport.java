package me.sirsavary.townmanager.commands.town;

import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Town;

public class TownTeleport {
	
	public TownTeleport(Player player) {
		Player p = player;
		Town t = Main.fileManager.getPlayerTown(p);
		if (t == null) {
			p.sendMessage(Chatter.TagMessage("You are not part of a town!"));
		}
		else {
			p.teleport(t.getSpawnLocation());
			p.sendMessage(Chatter.TagMessage("Teleporting to town spawn!"));
		}
	}
}