package me.sirsavary.townmanager;

import org.bukkit.ChatColor;

public class Chatter {
	
	/*
	 * Returns a [Tagged] and colored message based off the plugins name and using the default colors
	 */
	public static String TagMessage(String Message) {
		return Main.tagColor + "[" + Main.name + "]" + Main.messageColor  + Message;	
	}
	
	/*
	 * Returns a custom [Tagged] and colored message using the default colors
	 */
	public static String TagMessage(String Message, String Tag) {
		return Main.tagColor + "[" + Tag + "]" + Main.messageColor  + Message;	
	}
	
	/*
	 * Returns a [Tagged] and colored message based off the plugins name, the default message color and a custom tag color
	 */
	public static String TagMessage(String Message, ChatColor TagColor) {
		return TagColor + "[" + Main.name + "]" + Main.messageColor  + Message;	
	}
	
	/*
	 * Returns a [Tagged] and colored message based off the plugins name and using a custom tag and a custom message color
	 */
	public static String TagMessage(String Message, ChatColor TagColor, ChatColor MessageColor) {
		return TagColor + "[" + Main.name + "]" + MessageColor  + Message;	
	}
	
	/*
	 * Returns a colored message using the default message color
	 */
	public static String Message(String Message) {
		return Main.messageColor + Message;
	}
	
	/*
	 * Returns a colored message using a custom message color
	 */
	public static String Message(String Message, ChatColor MessageColor) {
		return MessageColor + Message;
	}


}
