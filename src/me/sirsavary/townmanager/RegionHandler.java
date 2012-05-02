package me.sirsavary.townmanager;

import java.util.ArrayList;
import java.util.Set;

import lib.spywhere.MFS.Result;
import me.sirsavary.townmanager.objects.Plot;
import me.sirsavary.townmanager.objects.PlotType;
import me.sirsavary.townmanager.objects.Selection;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RegionHandler {

	public ArrayList<Plot> getPlotsAtChunk(Chunk chunk) {
		IOManager fm = Main.fileManager;
		String p = chunk.getX() + "_" + chunk.getZ() + ".Regions";
		String townName = fm.getValue(
				chunk.getX() + "_" + chunk.getZ() + ".Town").toString();
		Set<String> nameList = fm.flatfileConfig.getConfigurationSection(p)
				.getKeys(false);
		ArrayList<Plot> regionList = new ArrayList<Plot>();
		for (String s : nameList) {
			String path = p + "." + s;
			Location loc1 = new Location(Main.server.getWorld(fm.getValue(
					path + ".World").toString()), (Double) fm.getValue(path
					+ ".MinPoint.X"), (Double) fm
					.getValue(path + ".MinPoint.Y"), (Double) fm.getValue(path
					+ ".MinPoint.Z"));
			Location loc2 = new Location(Main.server.getWorld(fm.getValue(
					path + ".World").toString()), (Double) fm.getValue(path
					+ ".MaxPoint.X"), (Double) fm
					.getValue(path + ".MaxPoint.Y"), (Double) fm.getValue(path
					+ ".MaxPoint.Z"));
			PlotType pt = PlotType.valueOf(fm.getValue(path + ".Type")
					.toString().toUpperCase());
			regionList.add(new Plot(s, loc1, loc2, townName, pt));
		}
		return regionList;
	}

	/**
	 * Retrieves the current Selection for the provided Player
	 * 
	 * @param player
	 * @return
	 **/
	public Selection getSelection(Player player) {
		Log.info("Getting selection");
		return new Selection(Main.firstPointMap.get(player),
				Main.secondPointMap.get(player));
	}

	public Town getTownFromChunk(Chunk chunk) {
		String p = (chunk.getX() + "_" + chunk.getZ() + ".Town");
		Town t = new Town((String) Main.fileManager.getValue(p));
		return t;
	}

	/**
	 * Checks to see if the specified block is within the specified region
	 * 
	 * @param block
	 * @param region
	 * @return true if block is within region, false if block is not within
	 *         region
	 */
	public boolean isBlockWithinRegion(Block block, Plot region) {
		return isLocationWithinRegion(block.getLocation(), region);
	}

	/**
	 * Returns whether or not the specified chunk is occupied
	 * 
	 * @param chunk
	 * @return true if chunk is occupied, false if chunk is not
	 */
	public boolean isChunkOccupied(Chunk chunk) {
		String p = (chunk.getX() + "_" + chunk.getZ());
		if (Main.fileManager.getValue(p) == null)
			return false;
		return true;
	}

	/**
	 * Returns whether or not the specified chunk is occupied by any country
	 * 
	 * @param chunk
	 * @return true if chunk is occupied by country, false if chunk is not
	 */
	public boolean isChunkOccupiedByCountry(Chunk chunk) {
		String p = (chunk.getX() + "_" + chunk.getZ() + ".Country");
		if (Main.fileManager.getValue(p) == null)
			return false;
		return true;
	}

	/**
	 * Returns whether or not the specified chunk is occupied by any regions.
	 * Faster region check method than check to see if getRegionsAtChunk returns
	 * null
	 * 
	 * @param chunk
	 * @return true if chunk is occupied, false if chunk is not
	 */
	public boolean isChunkOccupiedByRegions(Chunk chunk) {
		String p = (chunk.getX() + "_" + chunk.getZ() + ".Regions");
		if (Main.fileManager.getValue(p) == null)
			return false;
		return true;
	}

	/**
	 * Returns whether or not the specified chunk is occupied by any town
	 * 
	 * @param chunk
	 * @return true if chunk is occupied by town, false if chunk is not
	 */
	public boolean isChunkOccupiedByTown(Chunk chunk) {
		String p = (chunk.getX() + "_" + chunk.getZ() + ".Town");
		if (Main.fileManager.getValue(p) == null)
			return false;
		return true;
	}

	/**
	 * Checks to see if the specified location is within the specified region
	 * 
	 * @param location
	 * @param region
	 * @return true if location is within region, false if location is not
	 *         within region
	 */
	public boolean isLocationWithinRegion(Location location, Plot region) {
		Location l = location;
		Location min = region.getMinPoint();
		Location max = region.getMaxPoint();

		if ((l.getX() >= min.getX()) && (l.getY() >= min.getY())
				&& (l.getZ() >= min.getZ()) && (l.getX() <= max.getX())
				&& (l.getY() <= max.getY()) && (l.getZ() <= max.getZ()))
			return true;
		else if ((l.getX() <= min.getX()) && (l.getY() <= min.getY())
				&& (l.getZ() <= min.getZ()) && (l.getX() >= max.getX())
				&& (l.getY() >= max.getY()) && (l.getZ() >= max.getZ()))
			return true;
		return false;
	}

	/**
	 * Checks to see if the specified player is within the specified region
	 * 
	 * @param player
	 * @param region
	 * @return true if player is within region, false if player is not within
	 *         region
	 */
	public boolean isPlayerWithinRegion(Player player, Plot region) {
		return isLocationWithinRegion(player.getLocation(), region);
	}

	public void NewTown(Plot townHallRegion, String townName, Player mayor) {
		Integer townHealth = townHallRegion.getBlocks().size() / 5;

		if (townHealth > 500) {
			townHealth = 500;
		}

		Town t = new Town(townName, mayor, townHallRegion.getMinPoint()
				.getWorld(), townHealth);
		SaveTownRegion(townHallRegion, t);
		SaveTown(t);
	}

	/**
	 * Method to save regions, will save in appropriate database
	 * 
	 * @param region
	 */
	public void SaveRegion(Plot region) {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
		for (Block b : region.getBlocks()) {
			if (!chunkList.contains(b.getChunk())) {
				chunkList.add(b.getChunk());
			}
		}

		IOManager f = Main.fileManager;

		for (Chunk c : chunkList) {
			String p = (c.getX() + "_" + c.getZ() + ".Regions." + region
					.getRegionID());

			f.setValue(p + ".World", region.getMinPoint().getWorld().getName());

			f.setValue(p + ".MinPoint.X", region.getMinPoint().getX());
			f.setValue(p + ".MinPoint.Y", region.getMinPoint().getY());
			f.setValue(p + ".MinPoint.Z", region.getMinPoint().getZ());

			f.setValue(p + ".MaxPoint.X", region.getMaxPoint().getX());
			f.setValue(p + ".MaxPoint.Y", region.getMaxPoint().getY());
			f.setValue(p + ".MaxPoint.Z", region.getMaxPoint().getZ());
		}
		Main.fileManager.Save();
	}

	private void SaveTown(Town t) {
		IOManager f = Main.fileManager;
		String p = ("Towns." + t.getID());

		f.setValue(p + ".World", t.getWorld().getName());
		f.setValue(p + ".Mayor", t.getMayor().getName());
		f.setValue(p + ".Health", t.getHealth());

		Main.fileManager.Save();
	}

	/**
	 * Method to save regions, will save in appropriate database
	 * 
	 * @param region
	 */
	public void SaveTownRegion(Plot plot, Town town) {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
		for (Block b : plot.getBlocks()) {
			if (!chunkList.contains(b.getChunk())) {
				chunkList.add(b.getChunk());
			}
		}
		for (Chunk c : chunkList) {			
			Main.fileManager.SaveChunk(c, town.getCountry(), town.getID(), plot);
		}
		Main.fileManager.SavePlot(plot);
	}
}
