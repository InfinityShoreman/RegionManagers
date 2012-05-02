package me.sirsavary.townmanager.objects;

import org.bukkit.entity.Player;


public class Country extends Settlement{

	private Player leader;
	
	public Country(String Name) {
		super(Name);
	}

	public void setLeader(Player leader) {
		this.leader = leader;
	}

	public Player getLeader() {
		return leader;
	}
}
