package me.sirsavary.townmanager.objects;

import me.sirsavary.townmanager.IOManager;
import me.sirsavary.townmanager.Main;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Town {
	
	String townName;
	Player mayor;
	Region townHallRegion;
	Location spawnLocation;
	String townColor;
	Integer townHealth;
	World townWorld;
	
	public Town(String TownName) {
		IOManager fm = Main.fileManager;
		this.townName = TownName;
		this.mayor = Main.server.getPlayerExact(fm.getValue(townName + ".Owner").toString());
		this.townWorld = Main.server.getWorld(fm.getValue(townName + ".World").toString());
		this.spawnLocation = new Location(townWorld, (Integer)fm.getValue(townName + ".SpawnLocation.X"), (Integer)fm.getValue(townName + ".SpawnLocation.Y"), (Integer)fm.getValue(townName + ".SpawnLocation.Z"));
		//TODO fancy string parsing
		//this.townColor;
		//TODO figure out town health
		//this.townHealth;
		
	}	
}
