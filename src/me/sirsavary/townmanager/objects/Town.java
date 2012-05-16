package me.sirsavary.townmanager.objects;

import java.util.ArrayList;
import java.util.List;

import me.sirsavary.townmanager.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class Town extends Settlement {
	
	private Integer health = 0;
	
	//Basic town variables
	private World world;
	private String mayor;
	private ChatColor color = ChatColor.AQUA;
	private Location spawnLocation = Main.server.getWorlds().get(0).getSpawnLocation();
	private String MOTD = "New town is new";
	private Integer coffers = 0;
	private String country = "";
	private Integer size;
	
	//Basic town flags
	private Boolean pvpAllowed = Boolean.parseBoolean(SettingsManager.townSettings.get("PVP"));
	private Boolean freeBuildAllowed = Boolean.parseBoolean(SettingsManager.townSettings.get("FREEBUILD"));
	
	//The town's Town Hall Plot
	private Plot townHallPlot;
	
	//Citizens
	private List<String> citizens;
	
	//Tax settings
	private TaxType taxType = TaxType.NONE;
	private int tax = 0;
	
	//Special
	private String formattedID;
	
	public Town(String ID, String Country, String Mayor, Plot TownHallPlot, String TT, Integer TaxRate, Boolean PVP, Boolean FreeBuild, Location SpawnLocation, Integer Coffers, Integer Health, String Color, String MOTD, Integer Size) {
		super(ID);
		setCountry(Country);
		setMayor(Mayor);
		setTownHallPlot(TownHallPlot);
		setTaxType(TaxType.valueOf(TT.toUpperCase()));
		setTax(TaxRate);
		setPVPAllowed(PVP);
		setFreeBuildAllowed(FreeBuild);
		setSpawnLocation(SpawnLocation);
		setCoffers(Coffers);
		setHealth(Health);
		setColor(ChatColor.valueOf(Color));
		setMOTD(MOTD);
		setSize(Size);
		setFormattedID(this.getColor() + this.getID() + Main.messageColor);
	}
	
	public Town(String ID, Plot TownHallPlot, String Mayor) {
		super(ID);
		setTownHallPlot(TownHallPlot);
		setMayor(Mayor);
		this.world = TownHallPlot.getMaxPoint().getWorld();
		setFormattedID(this.getColor() + this.getID() + Main.messageColor);
		//Main.fileManager.SaveTown(this);
	}

	public void setMayor(String mayor) {
		this.mayor = mayor;
	}

	public String getMayor() {
		return mayor;
	}

	public void setTownHallPlot(Plot townHallPlot) {
		this.townHallPlot = townHallPlot;
	}

	public Plot getTownHallPlot() {
		return townHallPlot;
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
		setFormattedID(this.getColor() + this.getID() + Main.messageColor);
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

	public void setPVPAllowed(Boolean pvpAllowed) {
		this.pvpAllowed = pvpAllowed;
	}

	public Boolean isPVPAllowed() {
		return pvpAllowed;
	}

	public void setFreeBuildAllowed(Boolean freeBuildAllowed) {
		this.freeBuildAllowed = freeBuildAllowed;
	}

	public Boolean isFreeBuildAllowed() {
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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getFormattedID() {
		return formattedID;
	}

	public void setFormattedID(String formattedID) {
		this.formattedID = formattedID;
	}
}
