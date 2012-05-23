package me.sirsavary.townmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import me.sirsavary.questioner.Questioner;
import me.sirsavary.townmanager.commands.FirstRunCommand;
import me.sirsavary.townmanager.commands.TownCommand;
import me.sirsavary.townmanager.listeners.BlockBreakListener;
import me.sirsavary.townmanager.listeners.PVPListener;
import me.sirsavary.townmanager.listeners.WandListener;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	//Name of the plugin, used for tagging (messages and logging) and permissions
	public static String name = "TownManager";
	// Various file variables
	public static File pluginFolder = new File("plugins" + File.separator
			+ name);
	static File configFile = new File(pluginFolder, "Config.yml");
	static YamlConfiguration yamlConfig = new YamlConfiguration();
	// The IOManager, used to interface with the Database that TownManager uses
	public static IOManager fileManager;
	//Static variable so that the server is always within reach
	public static Server server;
	//Static variable so that the pluginmanager is always within reach
	public static PluginManager pm;
	//Hashmap storing the last town the player was in
	public static HashMap<Player, Town> lastTownMap = new HashMap<Player, Town>();
	// The RegionHandler, used to interface with Plots, Towns and Countries
	public static RegionHandler regionHandler;
	//The first and second point maps, used to store the locations of the blocks the player selected with his/her wand
	public static HashMap<Player, Location> firstPointMap = new HashMap<Player, Location>();
	public static HashMap<Player, Location> secondPointMap = new HashMap<Player, Location>();
	//tagColor and messageColor are both used with Chatter.java to make messaging the player easier
	public static ChatColor tagColor = ChatColor.RED;
	public static ChatColor messageColor = ChatColor.GREEN;
	public static Boolean debugEnabled = true;;
	// The DBType variable, used later on when passed off to the IOManager
	String dbType;
	//firstRun is used to determine whether or not the plugin should be disabled
	private Boolean firstRun = false;

	public static Questioner questioner;
	
	/**
	 * Method to be used on plugin's first run, generates fresh config file
	 */
	private void FirstRun() throws IOException {
		// Config settings
		
		// Database settings
		yamlConfig.set("Database.DBType", "yml");
		yamlConfig.set("Database.MySQL.Host", "localhost");
		yamlConfig.set("Database.MySQL.Port", "3306");
		yamlConfig.set("Database.MySQL.Username", "User");
		yamlConfig.set("Database.MySQL.Password", "Pass");
		yamlConfig.set("Database.MySQL.DBName", "Database");
		//Plot settings
		yamlConfig.set("Settlements.Plots.MaxPlotSize", "-1");
		yamlConfig.set("Settlements.Plots.MaxPlotsPerPlayer", "-1");
		//Town settings
		yamlConfig.set("Settlements.Towns.MaxTownSize", "-1");
		yamlConfig.set("Settlements.Towns.MaxTownSize", "-1");
		//Country settings
		yamlConfig.set("Settlements.Countries.MinTowns", "3");
		//Make firstRun true, disabling the plugin
		firstRun = true;
		
		//Save all config values to file
		yamlConfig.save(configFile);
	}

	/**
	 * Method to load configuration files (Skills file and config file)
	 */
	private void LoadConfigurations() throws FileNotFoundException,
			IOException, InvalidConfigurationException {
		yamlConfig.load(configFile);
	}

	@Override
	public void onDisable() {
		Log.info("Plugin stopped, shutting down!");
		try {
			yamlConfig.save(configFile);
		} catch (IOException e) {
			Log
					.severe("Failed to save configuration file! Printing stack trace!");
			e.printStackTrace();
		}

		Log.info("Plugin disabled!!");
	}

	@Override
	public void onEnable() {
		Log.info("Plugin started, initializing!");
		// Code block to load configurations and handle IO errors, also handles
		// the first plugin run
		try {
			LoadConfigurations();
		} catch (FileNotFoundException e) {
			Log.info("First run detected! Configuration files generated!");
			Log.info("Plugin disabled until configuration is completed!");
			try {
				FirstRun();
			} catch (IOException e1) {
				Log.severe("Failed to create configuration files! Printing stack trace!");
				e1.printStackTrace();
			}
		} catch (IOException e) {
			Log.severe("Failed to load configuration files! Printing stack trace!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			Log.severe("Failed to load configuration files! Printing stack trace!");
			e.printStackTrace();
		}

		RegisterCommands(firstRun);
		if (!firstRun) {
			server = getServer();
			pm = server.getPluginManager();
			// Now that config is loaded set DBType and make a new IOManager
			dbType = yamlConfig.getString("Database.DBType", "flatfile");
			fileManager = new IOManager(this, dbType);
			regionHandler = new RegionHandler();
			questioner = (Questioner)pm.getPlugin("Questioner");
			questioner.setDefaultMessageColor(messageColor);

			// Parse skills and register events
			ParseConfigs();
			RegisterEvents();
			//TownMapRenderer.drawToMap(server.getMap((short) 0));
		}
		Log.info("Plugin enabled!");
	}

	/**
	 * Method to parse the skills inside the Skills file
	 */
	public void ParseConfigs() {

	}

	/**
	 * Registers commands, if parameter first run is true all commands will
	 * return a firstrun message rather than returning a command not found
	 * message
	 * 
	 * @param FirstRun
	 * 
	 */
	private void RegisterCommands(Boolean FirstRun) {
		if (!firstRun) {
			getCommand("town").setExecutor(new TownCommand(this));
		} else {
			FirstRunCommand frc = new FirstRunCommand();
			getCommand("town").setExecutor(frc);
		}
	}

	/**
	 * Registers commands
	 */
	private void RegisterEvents() {
		pm = server.getPluginManager();
		pm.registerEvents(new WandListener(), this);
		pm.registerEvents(new BlockBreakListener(), this);
	    pm.registerEvents(new PVPListener(), this); 	  
	}

	/**
	 * Reloads the plugin by re-parsing the config file,
	 */
	public void ReloadPlugin() {
		ParseConfigs();
	}

}
