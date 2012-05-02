package me.sirsavary.townmanager.objects;

import java.util.ArrayList;
import java.util.List;

import me.sirsavary.townmanager.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Town extends Settlement {
	
	private Integer health;
	
	//Basic town variables
	private World world;
	private Player mayor;
	private ChatColor color = ChatColor.WHITE;
	private Location spawnLocation = Main.server.getWorlds().get(0).getSpawnLocation();
	private String MOTD = "";
	private Integer coffers = 0;
	private String country = "";
	
	//Basic town flags
	private boolean pvpAllowed = Boolean.parseBoolean(SettingsManager.townSettings.get("PVP"));
	private boolean freeBuildAllowed = Boolean.parseBoolean(SettingsManager.townSettings.get("FREEBUILD"));
	
	//The town's Town Hall region
	private Plot townHallRegion;
	
	//Citizens
	private List<String> citizens;
	
	//Tax settings
	private TaxType taxType = TaxType.NONE;
	private int tax = 0;
	
	public Town(String ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	
	public void setMayor(Player mayor) {
		this.mayor = mayor;
	}

	public Player getMayor() {
		return mayor;
	}

	public void setTownHallRegion(Plot townHallRegion) {
		this.townHallRegion = townHallRegion;
	}

	public Plot getTownHallRegion() {
		return townHallRegion;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setHealth(Integer townHealth) {
		this.health = townHealth;
	}

	public Integer getHealth() {
		return health;
	}

	public void setColor(ChatColor townColor) {
		this.color = townColor;
	}

	public ChatColor getColor() {
		return color;
	}

	public World getWorld() {
		return world;
	}

	public void setCitizens(List<String> list) {
		this.citizens = list;
	}

	public List<String> getCitizens() {
		if (citizens == null) return new ArrayList<String>();
		return citizens;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	public TaxType getTaxType() {
		return taxType;
	}

	public void setPvpAllowed(boolean pvpAllowed) {
		this.pvpAllowed = pvpAllowed;
	}

	public boolean isPvpAllowed() {
		return pvpAllowed;
	}

	public void setFreeBuildAllowed(boolean freeBuildAllowed) {
		this.freeBuildAllowed = freeBuildAllowed;
	}

	public boolean isFreeBuildAllowed() {
		return freeBuildAllowed;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public int getTax() {
		return tax;
	}

	public void setMOTD(String mOTD) {
		MOTD = mOTD;
	}

	public String getMOTD() {
		return MOTD;
	}

	public void setCoffers(Integer coffers) {
		this.coffers = coffers;
	}

	public Integer getCoffers() {
		return coffers;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}
}
