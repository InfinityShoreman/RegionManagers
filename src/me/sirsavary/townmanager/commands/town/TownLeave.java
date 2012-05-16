package me.sirsavary.townmanager.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Town;

public class TownLeave extends AbstractCommand {

	private final Player p;
	
	public TownLeave(CommandSender sender, boolean async, Main plugin, Player player)
			throws Exception {
		super(sender, async, plugin);
		p = player;
	}

	@Override
	public void run() {
		Town t = Main.fileManager.getPlayerTown(p);
		if (t == null) {
			p.sendMessage(Chatter.TagMessage("You are not part of a town!"));
		}
		else if (t.getMayor().equals(p.getName())){
			sender.sendMessage(Chatter.Message("You are currently the mayor of " + t.getFormattedID()));
			sender.sendMessage(Chatter.Message("You must delete " + t.getFormattedID() + ", you cannot leave it"));
		}
		else if (Main.questioner.ask(p, Chatter.Message("Are you sure you want to leave " + t.getFormattedID() + "?"), Chatter.Message("yes"), Chatter.Message("no")).equals("yes")) {
				Main.fileManager.RemoveCitizen(p.getName(), t);
				p.sendMessage("You have left " + t.getFormattedID());
		}
	}
}