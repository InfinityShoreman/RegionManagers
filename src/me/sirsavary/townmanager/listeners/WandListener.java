package me.sirsavary.townmanager.listeners;

import java.util.ArrayList;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Plot;

import org.bukkit.Chunk;
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

		if ((a == Action.LEFT_CLICK_BLOCK) && p.hasPermission(Main.name + ".Wand") && (p.getItemInHand().getType() == Material.CLAY_BRICK)) {
			Location l = event.getClickedBlock().getLocation();
			Main.firstPointMap.put(p, l);
			p.sendMessage("First point set to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
		}

		else if (a == Action.RIGHT_CLICK_BLOCK) {
			Location l = event.getClickedBlock().getLocation();
			if (p.hasPermission(Main.name + ".Wand") && (p.getItemInHand().getType() == Material.CLAY_BRICK)) {

				Main.secondPointMap.put(p, l);
				p.sendMessage("Second point set to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
			}
			else if (p.hasPermission(Main.name + ".Inspect") && (p.getItemInHand().getType() == Material.BOOK)) { //If player is using inspection tool and has permission to use it
				Chunk c = l.getChunk(); //Get the current chunk
				if (Main.fileManager.isChunkOccupied(c)) { //If the chunk is occupied
					String ID;

					if (Main.fileManager.isChunkOccupiedByTown(c)) {
						ID = Main.fileManager.getTownAtChunk(c).getFormattedID(); //Get the town ID
						p.sendMessage("Chunk belongs to town " + ID); //Send it to the player
						if (Main.fileManager.isChunkOccupiedByPlots(c)) { //If the chunk is occupied by plots
							ArrayList<String> messageList = new ArrayList<String>(); //Make a list for storing the plots
							for (Plot plot : Main.fileManager.getPlotsAtChunk(c))
								if (Main.regionHandler.isLocationWithinRegion(l, plot)) { //If the inspection tool was clicked within a plot
									Location min = plot.getMinPoint();
									Location max = plot.getMaxPoint();
									String minString = min.getX() + "," + min.getY() + "," + min.getZ();
									String maxString = max.getX() + "," + max.getY() + "," + max.getZ();
									messageList.add(Chatter.Message("Plot: " + plot.getID() + " Owner: " + plot.getOwner() + " Members: " + plot.getMembers() + "MinPoint: " + minString + " MaxPoint: " + maxString));
								}
							p.sendMessage("Found " + messageList.size() + " plots");
							for (String s : messageList) p.sendMessage(Chatter.Message(s));
						}
					}

					else if (Main.fileManager.isChunkOccupiedByCountry(c)) { //If the chunk is not occupied by a town, but it is by a country
						ID = Main.fileManager.getCountryAtChunk(c).getID(); //Get the name of the country
						p.sendMessage(Chatter.Message("Chunk belongs to country " + ID )); //Send it to the player
					}
				}
				else p.sendMessage(Chatter.Message("This chunk is unoccupied!")); //No plots, town or country was found
			}
		}
	}
}
