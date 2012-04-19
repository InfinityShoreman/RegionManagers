package me.sirsavary.townmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import me.sirsavary.townmanager.commands.FirstRunCommand;
import me.sirsavary.townmanager.commands.TownCommand;
import me.sirsavary.townmanager.listeners.WandListener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static String name = "TownManager";
	static ChatColor tagColor = ChatColor.RED;
	static ChatColor messageColor = ChatColor.GREEN;
	
	//Various file variables
	public static File pluginFolder = new File("plugins" + File.separator + name);
	static File townFolder = new File(pluginFolder + File.separator + "TownData");
	static File configFile = new File(pluginFolder, "Config.yml");
	static YamlConfiguration yamlConfig = new YamlConfiguration();
	
	//The IOManager, used to interface with the Database that SkillSystem uses
	public static IOManager fileManager;
	
	//The RegionHandler, used to interface with Regions
	public static RegionHandler regionHandler;
	
	public static Server server;
	
	public static HashMap<Player, Location> firstPointMap = new HashMap<Player, Location>();
	public static HashMap<Player, Location> secondPointMap = new HashMap<Player, Location>();
	
	public static PluginManager pm;
	
	private Boolean firstRun = false;
	
	//The DBType variable, used later on when passed off to the IOManager
	String dbType;
	
	public void onDisable()
	{
		Log.info("Plugin stopped, shutting down!");
		try {
			yamlConfig.save(configFile);
		} catch (IOException e) {
			Log.severe("Failed to save configuration file! Printing stack trace!");
			e.printStackTrace();
		}

		Log.info("Plugin disabled!!");
	}
	
	public void onEnable()
	{
		Log.info("Plugin started, initializing!");
		
		//Code block to load configurations and handle IO errors, also handles the first plugin run
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
			//Now that config is loaded set DBType and make a new IOManager
			dbType = yamlConfig.getString("Database.DBType","flatfile");
			fileManager = new IOManager(this, dbType);
			regionHandler = new RegionHandler();
			server = getServer();
			//Parse skills and register events
			ParseConfigs();
			RegisterEvents();
		}
		Log.info("Plugin enabled!");
	}
	
	/**
	 * Reloads the plugin by re-parsing the config file,
	 */
	public void ReloadPlugin()
	{
		ParseConfigs();
	}
	
	/**
	 * Registers commands
	 */
	private void RegisterEvents() {
	    pm.registerEvents(new WandListener(), this); 	    
	}
	
	/**
	 * Registers commands, if parameter first run is true all commands  will return a firstrun message rather than returning a command not
	 * found message
	 * @param FirstRun
	 *
	 */
	private void RegisterCommands(Boolean FirstRun) {
		if (!firstRun) {
			getCommand("town").setExecutor(new TownCommand(this));
		}
		else {
			FirstRunCommand frc = new FirstRunCommand();
			getCommand("town").setExecutor(frc);
		}
	}

	/**
	 * Method to parse the skills inside the Skills file
	 */
	public void ParseConfigs() {

	}

	/**
	 * Method to load configuration files (Skills file and config file)
	 */
	private void LoadConfigurations() throws FileNotFoundException, IOException, InvalidConfigurationException {
		yamlConfig.load(configFile);
	}

	/**
	 * Method to be used on plugin's first run, generates fresh config file
	 */
	private void FirstRun() throws IOException {
		//Config settings
		
		//Database settings
		yamlConfig.set("Database.DBType", "flatfile");
		yamlConfig.set("Database.MySQL.Host", "localhost");
		yamlConfig.set("Database.MySQL.Port", "3306");
		yamlConfig.set("Database.MySQL.Username", "User");
		yamlConfig.set("Database.MySQL.Password","Pass");
		yamlConfig.set("Database.MySQL.DBName", "Database");
		
		firstRun = true;


		//Save all config values to file
		yamlConfig.save(configFile);	
	}

}
