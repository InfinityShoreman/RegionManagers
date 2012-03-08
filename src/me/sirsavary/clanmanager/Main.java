package me.sirsavary.clanmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	static String name = "ClanManager";
	
	//Various file variables
	public static File pluginFolder = new File("plugins" + File.separator + name);
	static File townFolder = new File(pluginFolder + File.separator + "TownData");
	static File configFile = new File(pluginFolder, "Config.yml");
	static YamlConfiguration yamlConfig = new YamlConfiguration();
	
	//The IOManager, used to interface with the Database that SkillsSystem uses
	static IOManager fileManager;
	
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
		
		//Now that config is loaded set DBType and make a new IOManager
		dbType = yamlConfig.getString("Database.DBType","flatfile");
		fileManager = new IOManager(this, dbType);
		
		//TODO make sure plugin is disabled on first run (Just prevent anything from registering)
		//Parse skills and register events
		ParseConfigs();
		RegisterEvents();
		RegisterCommands();
		
		Log.info("Plugin enabled!");
	}
	
	public void ReloadPlugin()
	{
		
	}
	
	private void RegisterEvents() {
	    PluginManager pm = getServer().getPluginManager();
	    //pm.registerEvents(new PlayerListener(this), this); 	    
	}
	
	private void RegisterCommands() {
		//getCommand("skill").setExecutor(new SkillCommand(this));
	}

	/*
	 * Method to parse the skills inside the Skills file
	 */
	public void ParseConfigs() {

	}

	/*
	 * Method to load configuration files (Skills file and config file)
	 */
	private void LoadConfigurations() throws FileNotFoundException, IOException, InvalidConfigurationException {
		yamlConfig.load(configFile);
	}

	/*
	 * Method to be used on plugin's first run, generates fresh config file. Can also be called to config files.
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
		//Level settings
		yamlConfig.set("Level.MaxLevel",100);
		yamlConfig.set("Level.StartLevelDivisor",4);
		yamlConfig.set("Level.NumberToAdd",75);
		yamlConfig.set("Level.Multiplier",2);
		yamlConfig.set("Level.ExponentDivisor",7);
		//Default level settings
		yamlConfig.set("Level.Default.Mining",1);
		yamlConfig.set("Level.Default.Attack",1);
		yamlConfig.set("Level.Default.Defense",1);
		yamlConfig.set("Level.Default.Archery",1);
		yamlConfig.set("Level.Default.Magic",1);
		yamlConfig.set("Level.Default.Mining",1);
		yamlConfig.set("Level.Default.Farming",1);
		yamlConfig.set("Level.Default.Fishing",1);
		yamlConfig.set("Level.Default.Woodcutting",1);
		yamlConfig.set("Level.Default.Taming",1);
		yamlConfig.set("Level.Default.Acrobatics",1);
		
		//Default level settings
		yamlConfig.set("Level.Default.Mining.Restrictions.IronOre",5);
		yamlConfig.set("Level.Default.Mining.Restrictions.GoldOre",15);
		yamlConfig.set("Level.Default.Mining.Restrictions.RedstoneOre",20);
		yamlConfig.set("Level.Default.Mining.Restrictions.LapisOre",30);
		yamlConfig.set("Level.Default.Mining.Restrictions.DiamondOre",45);

		//Save all config values to file
		yamlConfig.save(configFile);	
	}

}
