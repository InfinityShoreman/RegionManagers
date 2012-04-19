package me.sirsavary.townmanager.objects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Selection {
	
	private Location minPoint;
	private Location maxPoint;
	
	public Selection(Location Loc1, Location Loc2) {
		this.setMinPoint(Loc1);
		this.setMaxPoint(Loc2);
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
	
	public ArrayList<Block> getBlocks() {
		ArrayList<Block> blockList = new ArrayList<Block>();
		World w = this.minPoint.getWorld();
		for (int x = (int) this.minPoint.getX(); x < this.maxPoint.getX() + 1; x++) {
			for (int z = (int) this.minPoint.getZ(); z < this.maxPoint.getZ() + 1; z++) {
				for (int y = (int) this.minPoint.getY(); y < this.maxPoint.getY() + 1; y++) {
					Block b = w.getBlockAt(x, y, z);
					blockList.add(b);
				}	
			}
		}
		return blockList;
	}

}
