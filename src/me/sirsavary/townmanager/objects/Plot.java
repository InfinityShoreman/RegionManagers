package me.sirsavary.townmanager.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Plot extends Selection {
	private String townID;
	private Player playerOwner;
	private String ownerName;
	private String ID;
	private PlotType plotType;
	
	public Plot(String ID, Location min, Location max, String town, PlotType PT){
		super(min, max);
		this.setID(ID.toLowerCase());
		setTownID(town);
		setPlotType(PT);
		setPlayerOwner(null);
	}
	
	public Plot(String ID, Location min, Location max, String town, PlotType PT, Player PO){
		super(min, max);
		this.setID(ID.toLowerCase());
		setTownID(town);
		setPlotType(PT);
		setPlayerOwner(PO);
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getID() {
		return ID;
	}

	public void setPlayerOwner(Player playerOwner) {
		this.playerOwner = playerOwner;
	}

	public Player getPlayerOwner() {
		return playerOwner;
	}

	public void setTownID(String townID) {
		this.townID = townID;
	}

	public String getTownID() {
		return townID;
	}

	public void setPlotType(PlotType plotType) {
		this.plotType = plotType;
	}

	public PlotType getPlotType() {
		return plotType;
	}
}
