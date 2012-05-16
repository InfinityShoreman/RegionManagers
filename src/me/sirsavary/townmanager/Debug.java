package me.sirsavary.townmanager;

import java.util.logging.Logger;

public class Debug {
	static Logger log = Logger.getLogger("Minecraft");
	static Boolean b = Main.debugEnabled;
	public static void info(String string) {
		if (b) log.info("[" + Main.name + "] " + string);
	}
	public static void warning(String string) {
		if (b) log.warning("[" + Main.name + "] " + string);
	}
	public static void severe(String string) {
		if (b) log.severe("[" + Main.name + "] " + string);
	}
}
