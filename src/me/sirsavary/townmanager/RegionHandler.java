package me.sirsavary.townmanager;

import me.sirsavary.townmanager.objects.Plot;
import me.sirsavary.townmanager.objects.Selection;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RegionHandler {
	/**
	 * Retrieves the current Selection for the provided Player
	 * 
	 * @param player
	 * @return
	 **/
	public Selection getSelection(Player player) {
		Log.info("Getting selection");
		if (Main.firstPointMap.get(player) == null || Main.secondPointMap.get(player) == null) return null;
		return new Selection(Main.firstPointMap.get(player),
				Main.secondPointMap.get(player));
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
}
