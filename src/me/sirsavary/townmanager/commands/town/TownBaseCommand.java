package me.sirsavary.townmanager.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Town;

public class TownBaseCommand extends AbstractCommand {

	private final Player p;
	
	public TownBaseCommand(CommandSender sender, boolean async, Main plugin, Player player)
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
		else {
			
		}
	}
}