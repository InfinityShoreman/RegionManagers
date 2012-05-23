package me.sirsavary.townmanager.commands.town;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownRemove extends AbstractCommand {


	
	public TownRemove(CommandSender sender, boolean async, Main plugin)
			throws Exception {
		super(sender, async, plugin);
	}

	@Override
	public void run() {
		Town t = Main.fileManager.getPlayerTown((Player) sender);
		if (t == null) {
			sender.sendMessage(Chatter.TagMessage("You are not part of a town!"));
		}
		else {
			if (!t.getMayor().equalsIgnoreCase(sender.getName())) {
				sender.sendMessage(Chatter.TagMessage("You are not the mayor of " + t.getColor() + t.getID() + Main.messageColor + "!"));
			}
			else {
				sender.sendMessage(Chatter.TagMessage("You are currently the mayor of " + t.getColor() + t.getID()));
				sender.sendMessage(Chatter.Message("If you delete " + t.getColor() + t.getID() + Main.messageColor + " all of it's connections will be dissolved"));
				if (Main.questioner.ask((Player)sender, Chatter.Message("Are you sure you want to delete " + t.getColor() + t.getID() + Main.messageColor + "?"), Chatter.Message("yes"), Chatter.Message("no")).equals("yes")) {
					sender.sendMessage(t.getFormattedID() + " has been removed!");
					Main.fileManager.RemoveTown(t);
				}
			}
		}
	}

}
