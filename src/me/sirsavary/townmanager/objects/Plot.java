package me.sirsavary.townmanager.objects;

import org.bukkit.Location;

public class Plot extends Selection {
	private String townID;
	private String owner;
	private String ID;
	private PlotType plotType;
	private String members;
	
	/*public Plot(String ID, Location min, Location max, String town, PlotType PT){
		super(min, max);
		this.setID(ID.toLowerCase());
		setTownID(town);
		setPlotType(PT);
		setOwner(null);
	}*/
	
	public Plot(String ID, Location min, Location max, String town, PlotType PT, String Owner){
		super(min, max);
		this.setID(ID.toLowerCase());
		setTownID(town);
		setPlotType(PT);
		setOwner(Owner);
	}
	
	public Plot(String ID, Location min, Location max, String town, PlotType PT, String Owner, String Members){
		super(min, max);
		this.setID(ID.toLowerCase());
		setTownID(town);
		setPlotType(PT);
		setOwner(Owner);
		setMembers(Members);
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getID() {
		return ID;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}
}
