package me.sirsavary.townmanager;

import java.util.ArrayList;

import me.sirsavary.townmanager.objects.Region;
import me.sirsavary.townmanager.objects.Selection;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RegionHandler {
	
	/**
	 * Method to save regions, will save in appropriate database
	 * @param region
	 */
	public void SaveRegion(Region region) {
		Selection sel = new Selection(region.getMinPoint(), region.getMaxPoint());
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
		//TODO add getSurfaceBlocks method to speed up chunk processing, consider adding getChunks method
		for (Block b : sel.getBlocks()) {
			if (!chunkList.contains(b.getChunk())) chunkList.add(b.getChunk());
		}
		
		IOManager f = Main.fileManager;
		
		for (Chunk c : chunkList) {
			ArrayList<Region> regionList = getRegionsAtChunk(c);
			if (regionList == null) regionList = new ArrayList<Region>();
			regionList.add(region);
			
			String p = (c.getX() + "_" + c.getZ() + ".Regions." + region.getRegionID());
			
			f.setValue(p + ".World", region.getMinPoint().getWorld().getName());
			
			f.setValue(p + ".MinPoint.X", region.getMinPoint().getX());
			f.setValue(p + ".MinPoint.Y", region.getMinPoint().getY());
			f.setValue(p + ".MinPoint.Z", region.getMinPoint().getZ());
			
			f.setValue(p + ".MaxPoint.X", region.getMaxPoint().getX());
			f.setValue(p + ".MaxPoint.Y", region.getMaxPoint().getY());
		}
		Main.fileManager.Save();
	}
	
	/**
	 * Retrieves the current Selection for the provided Player
	 * @param player
	 * @return
	 */
	public Selection getSelection(Player player) {
		return new Selection(Main.firstPointMap.get(player), Main.secondPointMap.get(player));
	}
	
	/**
	 * Returns whether or not the specified chunk is occupied by any regions. Faster region check method than check to 
	 * see if getRegionsAtChunk returns null
	 * @param chunk
	 * @return
	 */
	public boolean isChunkOccupied(Chunk chunk) {
		if (Main.fileManager.getValue(chunk.getX() + "." + chunk.getZ() + ".Regions") == null) return false;
		return true;
	}
	
	/**
	 * Gets the regions at the specified chunk
	 * @param chunk
	 */
	//TODO finish getRegions logic
	@SuppressWarnings("unchecked")
	public ArrayList<Region> getRegionsAtChunk(Chunk chunk) {
		return (ArrayList<Region>) Main.fileManager.flatfileConfig.getList(chunk.getX() + "." + chunk.getZ() + ".Regions");
	}
	
}
