package me.sirsavary.townmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import lib.spywhere.MFS.DataType;
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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
	private HashMap<String, Town> liveTownList = new HashMap<String, Town>();
	private ArrayList<Country> liveCountryList = new ArrayList<Country>();
	private ArrayList<TownChunk> liveChunkList = new ArrayList<TownChunk>();
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
		if (!(dbType.equalsIgnoreCase("flatfile") || dbType.equalsIgnoreCase("mysql") || dbType.equalsIgnoreCase("yml") || dbType.equalsIgnoreCase("sqlite") || dbType.equalsIgnoreCase("h2") || dbType.equalsIgnoreCase("postgre"))) {
			Log.severe("Invalid database type! Valid types:");
			Log.severe("flatfile, mysql, sqlite, h2, postgre, yml");
			return false;
		}
		else {
			StorageType ST = StorageType.valueOf(dbType.toUpperCase());
			if (ST == StorageType.FLATFILE || ST == StorageType.YML || ST == StorageType.SQLITE) mfs = MFSConnector.getMFS(Main.pm, ST);
			else mfs = MFSConnector.getMFS(Main.pm, Main.yamlConfig.getString("Database.MySQL.Host"), Main.yamlConfig.getString("Database.MySQL.Username"), Main.yamlConfig.getString("Database.MySQL.Password"), ST);
			return true;
		}	
	}
	private void CheckDatabase() {
		DB = mfs.getDB( Main.yamlConfig.getString("Database.MySQL.DBName"));
		if (DB == null) DB = mfs.createNewDB(Main.name);		
		chunkTB = DB.getTable("Chunks");
		countryTB = DB.getTable("Countries");
		townTB = DB.getTable("Towns");
		playerTB = DB.getTable("Players");
		plotTB = DB.getTable("Plots");
		if (chunkTB == null) {
			chunkTB = DB.createNewTable("Chunks", new Field("X", DataType.Integer), new Field("Z", DataType.Integer), new Field("Town", DataType.String), new Field("Country", DataType.String), new Field("Plots", DataType.String));
		}
		if (countryTB == null) {
			countryTB = DB.createNewTable("Countries", new Field("ID", DataType.String), new Field("Leader", DataType.String));
		}
		if (townTB == null) {
			townTB = DB.createNewTable("Towns", new Field("ID", DataType.String), new Field("Country", DataType.String), new Field("Mayor", DataType.String), new Field("Plot", DataType.String),
					new Field("TaxType", DataType.String), new Field("TaxRate", DataType.String), new Field("PVPEnabled", DataType.String),
					new Field("FreeBuildEnabled", DataType.String), new Field("SpawnLocation", DataType.String), new Field("Coffers", DataType.String), new Field("Health", DataType.String),
					new Field("Color", DataType.String), new Field("MOTD", DataType.String));
		}
		if (playerTB == null) {
			playerTB = DB.createNewTable("Players", new Field("Name", DataType.String), new Field("Town", DataType.String), new Field("Map", DataType.Integer));
		}
		if (plotTB == null) {
			plotTB = DB.createNewTable("Plots", new Field("ID", DataType.String), new Field("MaxPoint", DataType.String), new Field("MinPoint", DataType.String), new Field("Type", DataType.String),
					new Field("Town", DataType.String), new Field("Owner", DataType.String), new Field("Members", DataType.String));
		}
		
		playerTB.addField(new Field("Map",DataType.Integer));
	}
	private void PullDataFromDatabase() {
		Result r;
		int i;
		//Pull plots
		r = plotTB.getRecords();
		if (r.totalRecord() > 0) {
			for (i = 0; i < r.totalRecord(); i++) {
			Record rec = r.getRecord(i);
			String ID = rec.getData(new Field("ID"));
			String owner = rec.getData(new Field("Owner"));
			String members = rec.getData(new Field("Members"));
			String[] rawMinPos = rec.getData(new Field("MinPoint")).split(",");
			String[] rawMaxPos = rec.getData(new Field("MaxPoint")).split(",");
			//FIXME Make world that is define for min and max points dynamic, not static
			Location minPos = new Location(Main.server.getWorlds().get(0), Double.parseDouble(rawMinPos[0]), Double.parseDouble(rawMinPos[1]), Double.parseDouble(rawMinPos[2]));
			Location maxPos = new Location(Main.server.getWorlds().get(0), Double.parseDouble(rawMaxPos[0]), Double.parseDouble(rawMaxPos[1]), Double.parseDouble(rawMaxPos[2]));
			String plotTown = rec.getData(new Field("Town"));
			String plotType = rec.getData(new Field("Type")).toUpperCase();
			this.livePlotList.add(new Plot(ID, minPos, maxPos, plotTown, PlotType.valueOf(plotType), owner, members));
		}
		}
		//Pull chunks
		r = chunkTB.getRecords();
		if (r.totalRecord() > 0) {
		for (i = 0; i < r.totalRecord(); i++) {
			Record rec = r.getRecord(i);
			ArrayList<Plot> pList = new ArrayList<Plot>();
			if (rec.getData(new Field("Plots")) != null || !rec.getData(new Field("Plots")).equalsIgnoreCase("")) {
				String[] rawPList = rec.getData(new Field("Plots")).split(",");
				for (String s : rawPList) {
					pList.add(getPlot(s));
				}
			}
			//FIXME Make chunk code allow for world selection (Why didn't you think of this in the first place?)
			this.liveChunkList.add(new TownChunk(Main.server.getWorlds().get(0).getChunkAt(Integer.parseInt(rec.getData(new Field("X"))),Integer.parseInt(rec.getData(new Field("Z")))), pList, rec.getData(new Field("Country")), rec.getData(new Field("Town"))));
		}
		}
		//Pull countries
		r = countryTB.getRecords();
		if (r.totalRecord() > 0) {
		for (i = 0; i < r.totalRecord(); i++) {
			this.liveCountryList.add(new Country(r.getRecord(i).getData(new Field("ID"))));
		}
		}
		//Pull towns
		r = townTB.getRecords();
		if (r.totalRecord() > 0) {
		for (i = 0; i < r.totalRecord(); i++) {
			Record rec = r.getRecord(i);
			String name = rec.getData(new Field("ID")); //The name of the town
			String mayor = rec.getData(new Field("Mayor")); //The name of the town's mayor
			Plot townHallPlot = getPlot(rec.getData(new Field("Plot"))); //The name of the town's Town Hall plot
			String taxType = rec.getData(new Field("TaxType")); //The type of tax the town is using
			String color = rec.getData(new Field("Color")); //The town's color, may be null
			String motd = rec.getData(new Field("MOTD")); //The town's "message of the day" (Greeting message), may be null
			String country = rec.getData(new Field("Country")); //The country the town is a part of, may be null
			
			Integer taxAmount = Integer.parseInt(rec.getData(new Field("TaxRate"))); //The amount of tax the town is charging
			Integer coffers = Integer.parseInt(rec.getData(new Field("Coffers"))); //The amount of money in the town's coffers
			Integer health = Integer.parseInt(rec.getData(new Field("Health"))); //The health of the town
			
			Boolean pvpAllowed = Boolean.parseBoolean(rec.getData(new Field("PVPEnabled")));
			Boolean freeBuildAllowed = Boolean.parseBoolean(rec.getData(new Field("FreeBuildEnabled")));
			
			Integer size = chunkTB.filterRecord("Town", name).totalRecord();
			
			String[] rawSpawnLocation = rec.getData(new Field("SpawnLocation")).split(",");
			Location spawnLocation = new Location(townHallPlot.getMinPoint().getWorld(), Double.parseDouble(rawSpawnLocation[0]), Double.parseDouble(rawSpawnLocation[1]), Double.parseDouble(rawSpawnLocation[2])); //The town's spawn location, may be null
						
			Town newTown = new Town(name, country, mayor, townHallPlot, taxType, taxAmount, pvpAllowed, freeBuildAllowed, spawnLocation, coffers, health, color, motd, size);
			
			List<String> list = new ArrayList<String>();

			Result playerRes = playerTB.filterRecord("Town", name);
			for (int a = 0; a < playerRes.totalRecord(); a++) {
				list.add(playerRes.getRecord(a).getData(new Field("Name")));
			}
			
			newTown.setCitizens(list);
			this.liveTownList.put(newTown.getID(), newTown);
		}
		}
		
		for (Plot p : livePlotList) {
			Log.info(p.getID() + "; " + p.getMinPoint() + "," + p.getMaxPoint());
		}
	}
	public Integer getPlayerMap(String player) {
		Result res = playerTB.filterRecord("Name", player);
		if (res.totalRecord() <= 0) return null;
		try {
			return Integer.parseInt(res.getRecord(0).getData(new Field("Map")));
		} catch (NullPointerException e) {
			return null;
		}
	}
	public void setPlayerMap(String player, Integer mapNumber) {
		Result res = playerTB.filterRecord("Name", player);
		if (res.totalRecord() == 1) {
			playerTB.deleteRecords(res);
			Record rec = res.getRecord(0);
			playerTB.addRecord(player, rec.getData(new Field("Town")), mapNumber.toString());
		}
		else {
			playerTB.addRecord(player, null, mapNumber.toString());
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
		
		plotTB.addRecord(plot.getID(), p1, p2, plot.getPlotType().getName(), town.getID(), town.getMayor(), plot.getMembers());
		this.livePlotList.add(plot);
	}	
	public Town getTown(String townName) {
		return liveTownList.get(townName);
	}
	public void SaveTown(Town town) {
		String name = town.getID(); //The name of the town
		String mayor = town.getMayor(); //The name of the town's mayor
		String townHallPlot = town.getTownHallPlot().getID(); //The name of the town's Town Hall plot
		String taxType = town.getTaxType().getName(); //The type of tax the town is using
		String color = town.getColor().name(); //The town's color, may be null
		String motd = town.getMOTD(); //The town's "message of the day" (Greeting message), may be null
		String country = town.getCountry(); //The country the town is a part of, may be null
		Location rawSpawnLocation = town.getSpawnLocation(); //The town's spawn location, may be null
		Integer taxAmount = town.getTax(); //The amount of tax the town is charging
		Integer health = town.getHealth(); //The health of the town
		Integer coffers = town.getCoffers(); //The amount of money in the town's coffers
		String pvpAllowed = town.isPVPAllowed().toString();
		String freeBuildAllowed = town.isFreeBuildAllowed().toString();
		
		int x1 = (int) Math.floor(rawSpawnLocation.getX());
		int y1 = (int) Math.floor(rawSpawnLocation.getY());
		int z1 = (int) Math.floor(rawSpawnLocation.getZ());
		
		String spawnLocation = x1 + "," + y1 + "," + z1;
		townTB.addRecord(name, country, mayor, townHallPlot, taxType, taxAmount.toString(), pvpAllowed, freeBuildAllowed, spawnLocation, coffers.toString(), health.toString(), color, motd);
		town.setSize(chunkTB.filterRecord("Town", name).totalRecord());
		this.liveTownList.put(town.getID(), town);
	}
	public void UpdateTown(Town town) {
		String name = town.getID(); //The name of the town
		String mayor = town.getMayor(); //The name of the town's mayor
		String townHallPlot = town.getTownHallPlot().getID(); //The name of the town's Town Hall plot
		String taxType = town.getTaxType().getName(); //The type of tax the town is using
		String color = town.getColor().name(); //The town's color, may be null
		String motd = town.getMOTD(); //The town's "message of the day" (Greeting message), may be null
		String country = town.getCountry(); //The country the town is a part of, may be null
		Location rawSpawnLocation = town.getSpawnLocation(); //The town's spawn location, may be null
		Integer taxAmount = town.getTax(); //The amount of tax the town is charging
		Integer health = town.getHealth(); //The health of the town
		Integer coffers = town.getCoffers(); //The amount of money in the town's coffers
		String pvpAllowed = town.isPVPAllowed().toString();
		String freeBuildAllowed = town.isFreeBuildAllowed().toString();
		
		int x1 = (int) Math.floor(rawSpawnLocation.getX());
		int y1 = (int) Math.floor(rawSpawnLocation.getY());
		int z1 = (int) Math.floor(rawSpawnLocation.getZ());
		
		String spawnLocation = x1 + "," + y1 + "," + z1;
		townTB.deleteRecords(townTB.filterRecord("ID", town.getID()));
		townTB.addRecord(name, country, mayor, townHallPlot, taxType, taxAmount.toString(), pvpAllowed, freeBuildAllowed, spawnLocation, coffers.toString(), health.toString(), color, motd);
		town.setSize(chunkTB.filterRecord("Town", name).totalRecord());
		this.liveTownList.put(town.getID(), town);
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
	public TownChunk getTownChunkAtChunk(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				return tc;
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
	/**
	 * Returns whether or not the specified chunk is occupied
	 * 
	 * @param chunk
	 * @return true if chunk is occupied, false if chunk is not
	 */
	public boolean isChunkOccupied(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getCountry() != null || tc.getTown() != null || tc.getPlots().size() > 0) return true;
			}
		}
		return false;
	}
	/**
	 * Returns whether or not the specified chunk is occupied by any country
	 * 
	 * @param chunk
	 * @return true if chunk is occupied by country, false if chunk is not
	 */
	public boolean isChunkOccupiedByCountry(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getCountry() != null) return true;
			}
		}
		return false;
	}
	/**
	 * Returns whether or not the specified chunk is occupied by any town
	 * 
	 * @param chunk
	 * @return true if chunk is occupied by town, false if chunk is not
	 */
	public boolean isChunkOccupiedByTown(Chunk chunk) {
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				if (tc.getTown() != null) return true;
			}
		}
		return false;
	}
	/**
	 * Returns whether or not the specified chunk is occupied by any plots
	 * 
	 * @param chunk
	 * @return true if chunk is occupied by plots, false if chunk is not
	 */
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
	public Collection<Town> getTowns() {
		return liveTownList.values();
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
		String plots = "";
		//For loop to see if an instance of the chunk is already being tracked, will update it if it exists
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				tc.setCountry(chunk.getCountry());
				tc.setTown(chunk.getTown());
				ArrayList<Plot> newPlots = tc.getPlots();
				if (newPlots != null) {
					for (Plot p : newPlots) {
						if (plots.equals("")) plots = plots + p.getID();
						else plots = plots + p.getID() + ",";
					}
				}
				if (chunk.getPlots() != null) {
					for (Plot p : chunk.getPlots()) {
						if (plots.equals("")) plots = plots + p.getID();
						else plots = plots + p.getID() + ",";
						newPlots.add(p);
					}
				}
				tc.setPlots(newPlots);
				chunkTB.addRecord("" + chunk.getX() , "" + chunk.getZ(), chunk.getTown(), chunk.getCountry(), plots);
				this.liveChunkList.add(tc);
				
				if (tc.getTown() != null || !tc.getTown().equalsIgnoreCase("")) {
					Town t = getTown(tc.getTown());
					t.setSize(t.getSize() + 1);
				}
				return;
			}
		}
		//If it did not exist, add a new one to the list for tracking
		if (chunk.getPlots() != null) {
			for (Plot p : chunk.getPlots()) {
				if (plots.equals("")) plots = plots + p.getID();
				else plots = plots + p.getID() + ",";
			}
		}
		else plots = null;
		chunkTB.addRecord("" + chunk.getX() , "" + chunk.getZ(), chunk.getTown(), chunk.getCountry(), plots);
		this.liveChunkList.add(chunk);
		
		if (chunk.getTown() != null || !chunk.getTown().equalsIgnoreCase("")) {
			Town t = getTown(chunk.getTown());
			t.setSize(t.getSize() + 1);
		}
		
	}
	public void TrackPlotChunks(Plot plot) {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		for (Block b : plot.getSurfaceBlocks()) {
			if (!cList.contains(b.getChunk())) cList.add(b.getChunk());
		}
		ArrayList<Plot> pList = new ArrayList<Plot>();
		pList.add(plot);
		for (Chunk c : cList) {
			TrackChunk(new TownChunk(c, pList, getTown(plot.getTownID()).getCountry(), plot.getTownID()));
		}
	}
	public Town getPlayerTown(Player player) {
		Result r = playerTB.filterRecord("Name", player.getName());
		if (r.totalRecord() <= 0) return null;
		else return getTown(r.getRecord(0).getData(new Field("Town")));
	}
	public void RemoveTown(Town t) {
		playerTB.deleteRecords(playerTB.filterRecord("Town", t.getID()));
		chunkTB.deleteRecords(chunkTB.filterRecord("Town", t.getID()));
		townTB.deleteRecords(townTB.filterRecord("ID", t.getID()));
		plotTB.deleteRecords(plotTB.filterRecord("Town", t.getID()));
		
		livePlotList = new ArrayList<Plot>();
		liveTownList = new HashMap<String, Town>();
		liveChunkList = new ArrayList<TownChunk>();
		
		PullDataFromDatabase();
	}
	public void AddCitizen(String player, Town town) {
		int mapNumber;
		try {
			mapNumber = this.getPlayerMap(player);
		} catch (NullPointerException e) {
			mapNumber = (int) Main.server.createMap(town.getWorld()).getId();
			this.setPlayerMap(player, mapNumber);
		}
		
		Result res = playerTB.filterRecord("Name", player);
		if (res.totalRecord() == 1) {
			playerTB.deleteRecords(res);
			playerTB.addRecord(player, town.getID(), "" + mapNumber);
		}
		else {
			playerTB.addRecord(player, town.getID(), "" + mapNumber);
		}

		List<String> list = town.getCitizens();
		list.add(player);
		town.setCitizens(list);
	}
	public void RemoveCitizen(String player, Town town) {
		playerTB.deleteRecords(playerTB.filterRecord("Name", player));
		
		Result res = plotTB.filterRecord("Town", town.getID()); //Fetch all plots belonging to town
		
		for (int i = 0; i < res.totalRecord(); i++) { //For each plot
			Record rec = res.getRecord(i);
			String rawMembers = rec.getData(new Field("Members")); //Get the raw members "member1,member2,member3"
			String[] memberArray = rawMembers.split(","); //Split them down "member1 member2 member3"
			for (String s : memberArray) { //For each member
				if (s.equalsIgnoreCase(player)) { //If the player is a member
					rawMembers.replaceAll(player, ""); //Remove the player from the members
					rawMembers.replaceAll(",,", ","); //Make sure there are no double commas
					break;
				}
			}
			
			plotTB.deleteRecords(plotTB.filterRecord("ID", rec.getData(new Field("ID"))));
			Plot plot = getPlot(rec.getData(new Field("ID")));
			this.livePlotList.remove(plot);
			plot.setMembers(rawMembers);
			int x1 = (int) Math.floor(plot.getMinPoint().getX());
			int y1 = (int) Math.floor(plot.getMinPoint().getY());
			int z1 = (int) Math.floor(plot.getMinPoint().getZ());
			
			int x2 = (int) Math.floor(plot.getMaxPoint().getX());
			int y2 = (int) Math.floor(plot.getMaxPoint().getY());
			int z2 = (int) Math.floor(plot.getMaxPoint().getZ());
			
			String p1 = x1 + "," + y1 + "," + z1;
			String p2 = x2 + "," + y2 + "," + z2;
			
			plotTB.addRecord(plot.getID(), p1, p2, plot.getPlotType().getName(), town.getID(), town.getMayor(), rawMembers);
			this.livePlotList.add(plot);
		}
		
		List<String> list = town.getCitizens();
		list.remove(player);
		town.setCitizens(list);
	}
	public void UnTrackChunk(TownChunk chunk) {
		TownChunk townChunkToRemove = null;
		for (TownChunk tc : liveChunkList) {
			if (tc.getX() == chunk.getX() && tc.getZ() == chunk.getZ()) {
				townChunkToRemove = tc;
			}
		}
		liveChunkList.remove(townChunkToRemove);
		Result res = chunkTB.filterRecord("X", "" + townChunkToRemove.getX());
		res = res.filterBy("Z", "" + townChunkToRemove.getZ());
		chunkTB.deleteRecords(res);
		
		if (chunk.getTown() != null || !chunk.getTown().equalsIgnoreCase("")) {
			Town t = getTown(chunk.getTown());
			t.setSize(t.getSize() - 1);
		}
	}
	public boolean isPlayerMemberOfPlot(Plot plot, Player p) {
		String[] memberArray = plot.getMembers().split(",");
		for (String s : memberArray) {
			if (s.equalsIgnoreCase(p.getName())) return true;
		}
		return false;
	}
}