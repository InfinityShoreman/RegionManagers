package me.sirsavary.townmanager.listeners;

import me.sirsavary.townmanager.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		Action a = event.getAction();
		Player p = event.getPlayer();
		
		if (a == Action.LEFT_CLICK_BLOCK && p.hasPermission(Main.name + ".Wand") && p.getItemInHand().getType() == Material.CLAY_BRICK) {
			Location l = event.getClickedBlock().getLocation();
			Main.firstPointMap.put(p, l);
			p.sendMessage("First point set to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
		}
		
		else if (a == Action.RIGHT_CLICK_BLOCK && p.hasPermission(Main.name + ".Wand") && p.getItemInHand().getType() == Material.CLAY_BRICK) {
			Location l = event.getClickedBlock().getLocation();
			Main.secondPointMap.put(p, l);
			p.sendMessage("Second point set to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
		}
	}

}
