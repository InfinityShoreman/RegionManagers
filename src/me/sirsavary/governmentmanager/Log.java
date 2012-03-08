package me.sirsavary.governmentmanager;

import java.util.logging.Logger;

public class Log {
	static Logger log = Logger.getLogger("Minecraft");
	public static void info(String string) {
		log.info("[" + Main.name + "] " + string);
	}
	public static void warning(String string) {
		log.warning("[" + Main.name + "] " + string);
	}
	public static void severe(String string) {
		log.severe("[" + Main.name + "] " + string);
	}
}
