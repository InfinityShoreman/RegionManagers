package me.sirsavary.clanmanager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class IOManager {
	Main plugin;
	String dbType;
	
	//Flatfile variables
	File flatfileDB = new File(Main.pluginFolder + File.separator + "database.yml");;
	YamlConfiguration flatfileConfig  = new YamlConfiguration();
	
	
	public IOManager(Main Instance, String DBType) {
		plugin = Instance;
		dbType = DBType.toLowerCase();	
		Initialize();
	}
	
	private void Initialize(){
		try {
			if (VerifyDatabase() == true)
			{
				Log.info("Database verified successfully!");
			}
			else 
			{
				Log.severe("Database verification failed!");
			}
		} catch (IOException e) {
			Log.severe("Failed to verify database! Printing stack trace!");
			e.printStackTrace();
		}
	}
	
	private boolean VerifyDatabase() throws IOException {
		if (dbType.equalsIgnoreCase("flatfile"))
		{
			Log.info("Verifying flatfile DB");
			if (!flatfileDB.exists())
			{
				Log.info("Creating flatfile DB");
				flatfileConfig.save(flatfileDB);
			}
			else if (flatfileDB.exists())
			{
				try {
					Log.info("Loading flatfile DB");
					flatfileConfig.load(flatfileDB);
				} catch (InvalidConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return true;
		}
		
		else if (dbType.equalsIgnoreCase("mysql"))
		{
			
		}
		
		return false;	
	}

	public void AddPlayer(Player playerToAdd) {
		if (dbType.equalsIgnoreCase("flatfile") == true)
		{
			Log.info("Checking to see if player is in DB");
			String playerToAddName = playerToAdd.getName();
			
			if (flatfileConfig.get("Players." + playerToAddName) == null)
			{		
				Log.info("Adding player to DB");

				try {
					flatfileConfig.save(flatfileDB);
					Log.info("Player added to DB");
				} catch (IOException e) {
					Log.severe("Failed to save file! Abort! Abort!");
					e.printStackTrace();
				}
			}	
		}
		else if (dbType.equalsIgnoreCase("mysql") == true)
		{
			Log.warning("MySQL database is unimplemented at this time! Please use Flatfile!");
		}
		else if (dbType.equalsIgnoreCase("sqlite") == true)
		{
			Log.warning("SQLite database is unimplemented at this time! Please use Flatfile!");
		}
		else
		{
			Log.severe("Config file contains unknown database type: " + dbType + "!");
			Log.severe("Valid types are Flatfile, MySQL and SQLite!");
			
		}
	}
	
	public void RemovePlayer(Player playerToRemove) {
		if (dbType.equalsIgnoreCase("flatfile") == true)
		{
			String playerToRemoveName = playerToRemove.getName();
			
			if (flatfileConfig.get("Players." + playerToRemoveName + ".Skills.Mining") != null)
			{						
				flatfileConfig.set("Players." + playerToRemoveName, null);			
				try {
					flatfileConfig.save(flatfileDB);
				} catch (IOException e) {
					Log.severe("Failed to save file! Abort! Abort!");
					e.printStackTrace();
				}
			}	
		}
		else if (dbType.equalsIgnoreCase("mysql") == true)
		{
			Log.warning("MySQL database is unimplemented at this time! Please use Flatfile!");
		}
		else if (dbType.equalsIgnoreCase("sqlite") == true)
		{
			Log.warning("SQLite database is unimplemented at this time! Please use Flatfile!");
		}
		else
		{
			Log.severe("Config file contains unknown database type: " + dbType + "!");
			Log.severe("Valid types are Flatfile, MySQL and SQLite!");
			
		}
	}

	public Object getValue(String valueLocation) {
		if (dbType.equalsIgnoreCase("flatfile") == true)
		{
			return flatfileConfig.get(valueLocation);
		}
		else if (dbType.equalsIgnoreCase("mysql") == true)
		{
			Log.warning("MySQL database is unimplemented at this time! Please use Flatfile!");
		}
		else if (dbType.equalsIgnoreCase("sqlite") == true)
		{
			Log.warning("SQLite database is unimplemented at this time! Please use Flatfile!");
		}
		else
		{
			Log.severe("Config file contains unknown database type: " + dbType + "!");
			Log.severe("Valid types are Flatfile, MySQL and SQLite!");
			
		}
		return null;
	}
	
	public void setValue(String valueLocation, Object value) {
		if (dbType.equalsIgnoreCase("flatfile") == true)
		{
			 flatfileConfig.set(valueLocation, value);
		}
		else if (dbType.equalsIgnoreCase("mysql") == true)
		{
			Log.warning("MySQL database is unimplemented at this time! Please use Flatfile!");
		}
		else if (dbType.equalsIgnoreCase("sqlite") == true)
		{
			Log.warning("SQLite database is unimplemented at this time! Please use Flatfile!");
		}
		else
		{
			Log.severe("Config file contains unknown database type: " + dbType + "!");
			Log.severe("Valid types are Flatfile, MySQL and SQLite!");
			
		}
	}

	public void Save() {
	if (dbType.equalsIgnoreCase("flatfile") == true)
	{
		try {
			flatfileConfig.save(flatfileDB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else if (dbType.equalsIgnoreCase("mysql") == true)
	{
		Log.warning("MySQL database is unimplemented at this time! Please use Flatfile!");
	}
	else if (dbType.equalsIgnoreCase("sqlite") == true)
	{
		Log.warning("SQLite database is unimplemented at this time! Please use Flatfile!");
	}
	else
	{
		Log.severe("Config file contains unknown database type: " + dbType + "!");
		Log.severe("Valid types are Flatfile, MySQL and SQLite!");
		
	}
	}
}