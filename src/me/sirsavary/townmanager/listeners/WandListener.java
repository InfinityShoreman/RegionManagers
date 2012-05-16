package me.sirsavary.townmanager.listeners;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Plot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		Action a = event.getAction();
		Player p = event.getPlayer();
		
		if (a == Action.LEFT_CLICK_BLOCK && p.hasPermission(Main.name + ".Wand") && p.getItemInHand().getType() == Material.CLAY_BRICK) {
			Location l = event.getClickedBlock().getLocation();
			Main.firstPointMap.put(p, l);
			p.sendMessage("First point set to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
		}
		
		else if (a == Action.RIGHT_CLICK_BLOCK) {
			if (p.hasPermission(Main.name + ".Wand") && p.getItemInHand().getType() == Material.CLAY_BRICK) {
				Location l = event.getClickedBlock().getLocation();
				Main.secondPointMap.put(p, l);
				p.sendMessage("Second point set to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
			}
			else if (p.hasPermission(Main.name + ".Inspect") && p.getItemInHand().getType() == Material.BOOK) {
				if (Main.fileManager.isChunkOccupied(event.getClickedBlock().getChunk())) {
					String message = "Regions: ";
					boolean s = false;
					for (Plot r : Main.fileManager.getPlotsAtChunk(event.getClickedBlock().getChunk())) {
						if (Main.regionHandler.isBlockWithinRegion(event.getClickedBlock(), r)) {
							message = message + r.getID() + " belonging to town " + r.getTownID();
							s = true;
						}
					}
					if (s) p.sendMessage(Chatter.Message(message));
					else p.sendMessage(Chatter.Message("There are no regions here!"));
				}
				else p.sendMessage(Chatter.Message("There are no regions here!"));
				
			}
		}
	}

}
