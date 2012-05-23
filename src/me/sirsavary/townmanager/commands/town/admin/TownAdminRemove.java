package me.sirsavary.townmanager.commands.town.admin;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownAdminRemove extends AbstractCommand {

	Town t = null;
	
	public TownAdminRemove(CommandSender sender, boolean async, Main plugin, String townName)
			throws Exception {
		super(sender, async, plugin);
		t = Main.fileManager.getTown(townName);
	}

	@Override
	public void run() {
		if (t == null) {
			sender.sendMessage(Chatter.TagMessage("Town does not exist!"));
		}
		else {
			sender.sendMessage(Chatter.Message("If you delete " + t.getColor() + t.getID() + Main.messageColor + " all of it's connections will be dissolved"));
			if (Main.questioner.ask((Player)sender, Chatter.Message("Are you sure you want to delete " + t.getColor() + t.getID() + Main.messageColor + "?"), Chatter.Message("yes"), Chatter.Message("no")).equals("yes")) {
				sender.sendMessage(t.getFormattedID() + " has been removed!");
				Main.fileManager.RemoveTown(t);
			}
		}
	}
}