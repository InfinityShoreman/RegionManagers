package me.sirsavary.townmanager.listeners;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PVPListener implements Listener {
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		
		if (event.getEntity() instanceof Player || event.getEntity() instanceof Wolf)
		
		 if (event instanceof EntityDamageByEntityEvent) {
		       EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;  
		       Entity damager = damageEvent.getDamager();
		       Entity defender = event.getEntity();
		       
		       Town defTown = Main.fileManager.getTownAtChunk(defender.getLocation().getChunk()); //Get the defenders town
		       Town attTown = Main.fileManager.getTownAtChunk(damager.getLocation().getChunk()); //Get the attackers town
		       
		       if (defTown != null) { //If there is a town here
		    	   if (!defTown.isPVPAllowed()) { //PVP is not allowed
		    		   event.setCancelled(true); //Cancel the damage
				       if (damager instanceof Player) { //If a player caused it
				        	Player attacker = (Player) damager;
				        	attacker.sendMessage(Chatter.Message("PVP is dissallowed in " + defTown.getFormattedID() + "!")); //Tell them that they're bad
				       }
		    	   }
		       }
		       else if (attTown != null) {
		    	   if (!attTown.isPVPAllowed()) { //PVP is not allowed
		    		   event.setCancelled(true); //Cancel the damage
				       if (damageEvent.getDamager() instanceof Player) { //If a player caused it
				        	Player attacker = (Player) damager;
				        	attacker.sendMessage(Chatter.Message("Camping is disallowed!")); //Tell them that they're bad
				       }
		    	   }
		       }
		 }
		 else {		       
			 Entity defender = event.getEntity();
		     Town t = Main.fileManager.getTownAtChunk(defender.getLocation().getChunk()); //Get the town

		     if (t != null) { //If there is a town here
		    	   if (!t.isPVPAllowed()) { //PVP is not allowed
		    		   if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) event.setCancelled(true); //Block fire damage
		    	   }
		     }
		 }
	}
}
