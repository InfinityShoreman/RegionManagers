package me.sirsavary.townmanager.objects;

import java.util.ArrayList;

import org.bukkit.Chunk;

public class TownChunk{
	
	private int X;
	private int Z;
	private ArrayList<Plot> plots = new ArrayList<Plot>();
	private String country;
	private String town;
	
	public TownChunk(Chunk c, ArrayList<Plot> p, String countryID, String townID) {
		this.X = c.getX();
		this.Z = c.getZ();
		setPlots(p);
		setCountry(countryID);
		setTown(townID);
	}
	public int getX() {
		return X;
	}
	public int getZ() {
		return Z;
	}
	public void setPlots(ArrayList<Plot> plots) {
		this.plots = plots;
	}
	public ArrayList<Plot> getPlots() {
		if (plots == null || plots.size() <= 0) return null;
		return plots;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getTown() {
		return town;
	}
}
