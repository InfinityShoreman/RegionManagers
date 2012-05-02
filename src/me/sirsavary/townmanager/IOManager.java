package me.sirsavary.townmanager;

import java.util.ArrayList;

import lib.spywhere.MFS.Database;
import lib.spywhere.MFS.Field;
import lib.spywhere.MFS.MFS;
import lib.spywhere.MFS.Record;
import lib.spywhere.MFS.Result;
import lib.spywhere.MFS.StorageType;
import lib.spywhere.MFS.Table;
import me.sirsavary.townmanager.objects.Country;
import me.sirsavary.townmanager.objects.Plot;
import me.sirsavary.townmanager.objects.PlotType;
import me.sirsavary.townmanager.objects.Town;
import me.sirsavary.townmanager.objects.TownChunk;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class IOManager {
	Main plugin;
	String dbType;
	//Used for universal and standardized I/O
	public static MFS mfs = null;
	//The database, used with MFS
	public static Database DB;
	//The tables, used with MFS
	public static Table playerTB;
	public static Table plotTB;
	public static Table townTB;
	public static Table chunkTB;
	public static Table countryTB;
	
	private ArrayList<Plot> livePlotList = new ArrayList<Plot>();
	private ArrayList<Town> liveTownList = new ArrayList<Town>();
	private ArrayList<Country> liveCountryList = new ArrayList<Country>();
	private ArrayList<TownChunk> liveChunkList = new ArrayList<TownChunk>();
	//TODO Pull pre-existing Plot/Town/Country/Chunk data from Database on startup! (What's the point in saving it if you aren't going to retrieve it later?)
	public IOManager(Main Instance, String DBType) {
		plugin = Instance;
		dbType = DBType.toLowerCase();	
		Initialize();
	}
	private void Initialize(){
		//Prepare MFS backend
		if (PrepareMFS()) {
			//Initialize and verify database connection
			if (VerifyDatabase()) {
				//Fetch database and tables, create them if necessary
				CheckDatabase();
				//Pull data from database
				PullDataFromDatabase();
			}
		}
		
	}
	private boolean PrepareMFS() {
		if (MFSConnector.prepareMFS(Main.pm)) {
			//MFS prepared and can be connected now
			return true;
		} else {
			//MFS failed to download/install/run
			Log.severe("Error intializing MFS I/O backend! Check log for MFS stack trace!");
			return false;
		}
	}
	private boolean VerifyDatabase() {
		if (!(dbType.equalsIgnoreCase("flatfile") || dbType.equalsIgnoreCase("mysql") || dbType.equalsIgnoreCase("sqlite") || dbType.equalsIgnoreCase("h2") || dbType.equalsIgnoreCase("postgre"))) {
			Log.severe("Invalid database type! Valid types:");
			Log.severe("flatfile, mysql, sqlite, h2, postgre");
			return false;
		}
		else {
			StorageType ST = StorageType.valueOf(dbType.toUpperCase());
			if (ST == StorageType.FLATFILE) mfs = MFSConnector.getMFS(Main.pm, ST);
			else mfs = MFSConnector.getMFS(Main.pm, "127.0.0.1", "root", "twilight", ST);
			return true;
		}	
	}
	private void CheckDatabase() {
		DB = mfs.getDB(Main.name);
		if (DB == null) DB = mfs.createNewDB(Main.name);		
		chunkTB = DB.getTable("Chunks");
		countryTB = DB.getTable("Countries");
		townTB = DB.getTable("Towns");
		playerTB = DB.getTable("Players");
		plotTB = DB.getTable("Plots");
		if (chunkTB == null) {
			chunkTB = DB.createNewTable("Chunks", "X", "Z", "Town", "Country", "Plots");
		}
		if (countryTB == null) {
			countryTB = DB.createNewTable("Countries", "ID", "Leader");
		}
		if (townTB == null) {
			townTB = DB.createNewTable("Towns", "ID", "Country", "Mayor", "TownHallPlot",
					"TaxType", "TaxRate", "PVPEnabled",
					"FreeBuildEnabled", "SpawnLocation", "Coffers", "Health",
					"Color", "MOTD");
		}
		if (playerTB == null) {
			playerTB = DB.createNewTable("Players", "Town");
		}
		if (plotTB == null) {
			plotTB = DB.createNewTable("Plots", "MinPoint", "MaxPoint", "Type",
					"Town", "Owner");
		}
	}
	private void PullDataFromDatabase() {
		Result r;
		int i;
		//Pull countries
		r = countryTB.getRecords();
		for (i = 0; i < r.totalRecord(); i++) {
			this.liveCountryList.add(new Country(r.getRecord(i).getData(new Field("ID"))));
		}
		//Pull towns
		r = townTB.getRecords();
		for (i = 0; i < r.totalRecord(); i++) {
			this.liveTownList.add(new Town(r.getRecord(i).getData(new Field("ID"))));
		}
		//Pull plots
		r = plotTB.getRecords();
		for (i = 0; i < r.totalRecord(); i++) {
			Record rec = r.getRecord(i);
			String ID = rec.getData(new Field("ID"));
			String[] rawMinPos = rec.getData(new Field("MinPoint")).split(",");
			String[] rawMaxPos = rec.getData(new Field("MaxPoint")).split(",");
			//FIXME Make world that is define for min and max points dynamic, not static
			Location minPos = new Location(Main.server.getWorlds().get(0), Double.parseDouble(rawMinPos[0]), Double.parseDouble(rawMinPos[1]), Double.parseDouble(rawMinPos[2]));
			Location maxPos = new Location(Main.server.getWorlds().get(0), Double.parseDouble(rawMaxPos[0]), Double.parseDouble(rawMaxPos[1]), Double.parseDouble(rawMaxPos[2]));
			String plotTown = rec.getData(new Field("Town"));
			String plotType = rec.getData(new Field("Type"));
			this.livePlotList.add(new Plot(ID, minPos, maxPos, plotTown, PlotType.valueOf(plotType)));
		}
		//Pull chunks
		r = chunkTB.getRecords();
		for (i = 0; i < r.totalRecord(); i++) {
			Record rec = r.getRecord(i);
			//FIXME Make chunk code allow for world selection (Why didn't you think of this in the first place?)
			this.liveChunkList.add(new TownChunk(Main.server.getWorlds().get(0).getChunkAt(arg0, arg1), livePlotList, rec.getData(new Field("X")), dbType));
		}
	}
	public Plot getPlot(String plotName) {
		for (Plot p : livePlotList) {
			if (p.getID().equalsIgnoreCase(plotName)) {
				return p;
			}
		}
		return null;
	}
	public void SavePlot(Plot plot) {
		int x1 = (int) Math.floor(plot.getMinPoint().getX());
		int y1 = (int) Math.floor(plot.getMinPoint().getY());
		int z1 = (int) Math.floor(plot.getMinPoint().getZ());
		
		int x2 = (int) Math.floor(plot.getMaxPoint().getX());
		int y2 = (int) Math.floor(plot.getMaxPoint().getY());
		int z2 = (int) Math.floor(plot.getMaxPoint().getZ());
		
		String p1 = x1 + "," + y1 + "," + z1;
		String p2 = x2 + "," + y2 + "," + z2;
		
		Town town = getTown(plot.getTownID());
		
		plotTB.addRecord(p1, p2, plot.getPlotType().getName(), town.getID(), town.getMayor().getName());
		this.livePlotList.add(plot);
	}	
	public Town getTown(String townName) {
		for (Town t : liveTownList) {
			if (t.getID().equalsIgnoreCase(townName)) {
				return t;
			}
		}
		return null;
	}
	public void SaveTown(Town town) {
		String name = town.getID(); //The name of the town
		String mayor = town.getMayor().getName(); //The name of the town's mayor
		String townHallPlot = town.getTownHallRegion().getID(); //The name of the town's Town Hall plot
		String taxType = town.getTaxType().getName(); //The type of tax the town is using
		String color = town.getColor().name(); //The town's color, may be null
		String motd = town.getMOTD(); //The town's "message of the day" (Greeting message), may be null
		String country = town.getCountry(); //The country the town is a part of, may be null
		Location spawnLocation = town.getSpawnLocation(); //The town's spawn location, may be null
		Integer taxAmount = town.getTax(); //The amount of tax the town is charging
		Integer health = town.getHealth(); //The health of the town
		Integer coffers = town.getCoffers(); //The amount of money in the town's coffers
		
		townTB.addRecord(name, country, mayor, townHallPlot, taxType, taxAmount.toString(), null, null, spawnLocation.toString(), coffers.toString(), health.toString(), color, motd);
		this.liveTownList.add(town);
	}
	public Country getCountry(String  countryName) {
		for (Country t : liveCountryList) {
			if (t.getID().equalsIgnoreCase(countryName)) {
				return t;
			}
		}
		return null;
	}
	public void SaveCountry(Country country) {
		String ID = country.getID();
		String leader = country.getLeader().getName();
		
		countryTB.addRecord(ID, leader);
		this.liveCountryList.add(country);
	}
	public Country getCountryAtChunk(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				return getCountry(tc.getCountry());
			}
		}
		return null;
	}
	public Town getTownAtChunk(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				return getTown(tc.getTown());
			}
		}
		return null;
	}
	public ArrayList<Plot> getPlotsAtChunk(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				return tc.getPlots();
			}
		}
		return null;
	}
	public boolean isChunkOccupied(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getCountry() != null || tc.getTown() != null || tc.getPlots().size() > 0) return true;
			}
		}
		return false;
	}
	public boolean isChunkOccupiedByCountry(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getCountry() != null) return true;
			}
		}
		return false;
	}
	public boolean isChunkOccupiedByTown(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getTown() != null) return true;
			}
		}
		return false;
	}
	public boolean isChunkOccupiedByPlots(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getPlots().size() > 0) return true;
			}
		}
		return false;
	}
	public ArrayList<Plot> getPlots() {
		if (livePlotList.size() <= 0) return null;
		return livePlotList;
	}
	public ArrayList<Town> getTowns() {
		if (liveTownList.size() <= 0) return null;
		return liveTownList;
	}
	public ArrayList<Country> getCountries() {
		if (liveCountryList.size() <= 0) return null;
		return liveCountryList;
	}
	public ArrayList<TownChunk> getChunks() {
		if (liveChunkList.size() <= 0) return null;
		return liveChunkList;
	}
	public void TrackChunk(TownChunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				tc.setCountry(chunk.getCountry());
				tc.setTown(chunk.getTown());
				tc.setPlots(chunk.getPlots());
				return;
			}
		}
		
	}

	//XXX SHIT'S ON FIRE YO'
	/*
	private void SaveChunk(Chunk chunk, String country, String town, Plot plot) {
		String chunkX = "" + chunk.getX();
		String chunkZ = "" + chunk.getZ();
		String plotID = plot.getID();
		String plots = "";
		
		//Attempt to fetch record
		Result chunkRecord = chunkTB.filterRecord("X", chunkX);
		//If there was no record found (Town does not exist), return null
		if (chunkRecord != null && chunkRecord.totalRecord() > 1) {
			//If only one record was found create a Town and return it
			if (chunkRecord.totalRecord() == 1) {
				plots = chunkRecord.getRecord(0)[4];
			}
			//If more then one record was found find the one that matches EXACTLY, create a town from it and return it
			else {
				for (int i = 0; i < chunkRecord.totalRecord(); i++) {
					if (chunkRecord.getRecord(i).getData(new Field("X")).equalsIgnoreCase(chunkZ)) {
						plots = chunkRecord.getRecord(i).getData(new Field("Plots"));
					}
				}
			}
		}
		if (plots == "") plots = plotID;
		else plots = plots + "," + plotID;
		
		chunkTB.addRecord(chunkX, chunkZ, country, town, plots);
	}
	*/
}