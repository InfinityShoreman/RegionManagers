package me.sirsavary.townmanager.objects;

import org.bukkit.Location;

public class Region {
	
	private Location minPoint;
	private Location maxPoint;
	private OwnerType ownerType;
	private String ownerName;
	private String regionID;
	
	public Region(String regionID, Location min, Location max){
		this.setMinPoint(min);
		this.setMaxPoint(max);
		this.setRegionID(regionID.toLowerCase());
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setMaxPoint(Location maxPoint) {
		this.maxPoint = maxPoint;
	}

	public Location getMaxPoint() {
		return maxPoint;
	}

	public void setMinPoint(Location minPoint) {
		this.minPoint = minPoint;
	}

	public Location getMinPoint() {
		return minPoint;
	}

	public void setOwnerType(OwnerType ownerType) {
		this.ownerType = ownerType;
	}

	public OwnerType getOwnerType() {
		return ownerType;
	}

	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}

	public String getRegionID() {
		return regionID;
	}
}
