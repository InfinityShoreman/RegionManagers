package me.sirsavary.townmanager.commands;

import me.sirsavary.townmanager.Main;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class AbstractCommand implements Runnable
{
	protected CommandSender sender;
	private final BukkitScheduler scheduler;
	
	protected AbstractCommand(CommandSender sender, boolean async, Main plugin) {//throws Exception {
		this.sender = sender;
	    this.scheduler = Main.server.getScheduler();
		if (async) {
			if (scheduler.scheduleAsyncDelayedTask(plugin, this) == -1);
				//throw new Exception("Failed to schedule the command");
		} else
			run();
	}
}