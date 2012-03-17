package me.sirsavary.townmanager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.regions.Region;

public class Town {
	
	Player owner;
	Region townHallRegion;
	Location spawnLocation;
	ChatColor townColor;
	String townName;
	int townHealth;
	
	public Town(String TownName) {
		this.townName = TownName;
		this.owner = Main.server.getPlayerExact(Main.fileManager.getValue(townName + ".Owner").toString());
	}	
}
