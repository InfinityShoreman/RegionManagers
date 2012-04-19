package me.sirsavary.townmanager;

import me.sirsavary.townmanager.objects.Region;
import me.sirsavary.townmanager.objects.Selection;

import org.bukkit.entity.Player;

public class RegionHandler {
	
	/**
	 * Method to save regions, will save in appropriate database
	 * @param region
	 */
	public void SaveRegion(Region region) {
		
	}
	
	/**
	 * Retrieves the current Selection for the provided Player
	 * @param player
	 * @return
	 */
	public Selection getSelection(Player player) {
		return new Selection(Main.firstPointMap.get(player), Main.secondPointMap.get(player));
	}

}
