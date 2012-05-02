package me.sirsavary.townmanager.objects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Selection {
	
	private Location minPoint;
	private Location maxPoint;
	
	public Selection(Location Loc1, Location Loc2) {

		Location loc1 = new Location(Loc1.getWorld(), 0, 0, 0);
		Location loc2 = new Location(Loc1.getWorld(), 0, 0, 0);
		
		//Compare X values and determine minimum and maximum X coords
		if (Loc1.getX() > Loc2.getX()) {
			loc1.setX(Loc2.getX());
			loc2.setX(Loc1.getX());
		}
		else if (Loc1.getX() < Loc2.getX()) {
			loc1.setX(Loc1.getX());
			loc2.setX(Loc2.getX());
		}
		else {
			loc1.setX(Loc1.getX());
			loc2.setX(Loc1.getX());
		}
		
		//Compare Y values and determine minimum and maximum Y coords
		if (Loc1.getY() > Loc2.getY()) {
			loc1.setY(Loc2.getY());
			loc2.setY(Loc1.getY());
		}
		else if (Loc1.getY() < Loc2.getY()) {
			loc1.setY(Loc1.getY());
			loc2.setY(Loc2.getY());
		}
		else {
			loc1.setY(Loc1.getY());
			loc2.setY(Loc1.getY());
		}
		
		//Compare Z values and determine minimum and maximum Z coords
		if (Loc1.getZ() > Loc2.getZ()) {
			loc1.setZ(Loc2.getZ());
			loc2.setZ(Loc1.getZ());
		}
		else if (Loc1.getZ() < Loc2.getZ()) {
			loc1.setZ(Loc1.getZ());
			loc2.setZ(Loc2.getZ());
		}
		else {
			loc1.setZ(Loc1.getZ());
			loc2.setZ(Loc1.getZ());
		}
		
		setMinPoint(loc1);
		setMaxPoint(loc2);
	}

	public void setMinPoint(Location minPoint) {
		this.minPoint = minPoint;
	}

	public Location getMinPoint() {
		return minPoint;
	}

	public void setMaxPoint(Location maxPoint) {
		this.maxPoint = maxPoint;
	}

	public Location getMaxPoint() {
		return maxPoint;
	}
	
	/**
	 * Method to gather all the blocks within a region
	 * @return
	 * an ArrayList of type Block containing the selection's blocks
	 */
	public ArrayList<Block> getBlocks() {
		ArrayList<Block> blockList = new ArrayList<Block>();
		World w = this.minPoint.getWorld();
		for (int x = (int) this.minPoint.getX(); x < (int) this.maxPoint.getX() + 1; x++) {
			for (int z = (int) this.minPoint.getZ(); z < (int) this.maxPoint.getZ() + 1; z++) {
				for (int y = (int) this.minPoint.getY(); y < (int) this.maxPoint.getY() + 1; y++) {
					Block b = w.getBlockAt(x, y , z);
					blockList.add(b);
				}	
			}
		}
		return blockList;
	}

	/**
	 * Method to gather horizontal blocks from a region (Y = 0)
	 * @return
	 * an ArrayList of type Block containing the selection's horizontal blocks
	 */
	public ArrayList<Block> getHorizontalBlocks() {
		ArrayList<Block> blockList = new ArrayList<Block>();
		World w = this.minPoint.getWorld();
		for (int x = (int) this.minPoint.getX(); x < this.maxPoint.getX() + 1; x++) {
			for (int z = (int) this.minPoint.getZ(); z < this.maxPoint.getZ() + 1; z++) {
				Block b = w.getBlockAt(x, 0, z);
				blockList.add(b);
			}
		}
		return blockList;
	}
	
	/**
	 * Method to gather the highest blocks across a region
	 * @return
	 * an ArrayList of type Block containing the selection's surface blocks
	 */
	public ArrayList<Block> getSurfaceBlocks() {
		ArrayList<Block> blockList = new ArrayList<Block>();
		World w = this.minPoint.getWorld();
		for (int x = (int) this.minPoint.getX(); x < this.maxPoint.getX() + 1; x++) {
			for (int z = (int) this.minPoint.getZ(); z < this.maxPoint.getZ() + 1; z++) {
				Block b = w.getHighestBlockAt(x, z);
				blockList.add(b);
			}
		}
		return blockList;
	}
}