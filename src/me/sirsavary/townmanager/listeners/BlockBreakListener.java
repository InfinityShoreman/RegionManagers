package me.sirsavary.townmanager.listeners;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Plot;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockBreakListener implements Listener {
	
	@EventHandler
	public void onPlayerBlockBreak(BlockBreakEvent event) {
		if (Main.regionHandler.isChunkOccupied(event.getBlock().getChunk())) {
			for (Plot r : Main.regionHandler.getPlotsAtChunk(event.getBlock().getChunk())) {
				if (Main.regionHandler.isBlockWithinRegion(event.getBlock(), r)) {
					event.setCancelled(true);
				}
			}
		}
		

	}
	
	@EventHandler
	public void onPlayerTownChange(PlayerMoveEvent event) {
		Chunk c = event.getPlayer().getLocation().getChunk();
		Player p = event.getPlayer();
		
		Town newTown;
		Town oldTown;
		
		if (Main.regionHandler.isChunkOccupiedByTown(c)) newTown = Main.regionHandler.getTownFromChunk(c);
		else newTown = null;
		
		if (!Main.lastTownMap.containsKey(p)) oldTown = null;
		else oldTown = Main.lastTownMap.get(p);
		
		//Wilderness --> Wilderness
		if (oldTown == null && newTown == null) {
			//Don't do anything!
		}
		//Town --> Wilderness and Wilderness --> Town
		else if ((oldTown != null && newTown == null) || (oldTown == null && newTown != null)) {
			//If player is heading into wilderness
			if (newTown == null) {
				//Send the player a message and update lastTownMap
				p.sendMessage(Chatter.Message("~Wilderness~"));
				Main.lastTownMap.put(p, newTown);
			}
			//If player is heading into town
			else if (newTown != null) {
				//Send the player a message using the new town's color and name and update lastTownMap
				p.sendMessage(newTown.getColor() + newTown.getID());
				Main.lastTownMap.put(p, newTown);
			}
		}
		//Town --> Town
		else if (oldTown != null && newTown != null) {
			//If the towns are different
			if (!oldTown.getID().equalsIgnoreCase(newTown.getID())) {
				//Send the player a message using the new town's color and name and update lastTownMap
				p.sendMessage(newTown.getColor() + newTown.getID());
				Main.lastTownMap.put(p, newTown);
			}
		}
	}
}