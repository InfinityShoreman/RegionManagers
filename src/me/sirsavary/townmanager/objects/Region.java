package me.sirsavary.townmanager.objects;

import org.bukkit.Location;

public class Region extends Selection {
	private String ID;
	
	public Region(Location min, Location max, String regionID) {
		super(min, max);
		setID(regionID);
	}

	public void setID(String regionID) {
		this.ID = regionID;
	}

	public String getID() {
		return ID;
	}
}
