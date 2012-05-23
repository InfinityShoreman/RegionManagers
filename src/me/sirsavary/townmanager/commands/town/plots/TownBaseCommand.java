package me.sirsavary.townmanager.commands.town.plots;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Plot;
import me.sirsavary.townmanager.objects.PlotType;
import me.sirsavary.townmanager.objects.Selection;
import me.sirsavary.townmanager.objects.Town;

public class TownBaseCommand extends AbstractCommand {

	private final Player p;
	
	public TownBaseCommand(CommandSender sender, boolean async, Main plugin)
			throws Exception {
		super(sender, async, plugin);
		p = (Player) sender;
	}

	@Override
	public void run() {
		Town t = Main.fileManager.getPlayerTown(p);
		if (t == null) {
			p.sendMessage(Chatter.TagMessage("You are not part of a town!"));
		}
		else {
			Selection sel = Main.regionHandler.getSelection(p);
			if (sel == null) p.sendMessage(Chatter.TagMessage("You have not made a selection!"));
			else {
				
				Location min = sel.getMinPoint();
				Location max = sel.getMaxPoint();

				//for (Plot plot : Main.fileManager.getpl
				
				
				String plotID = Main.questioner.ask(p, "Please choose an ID for this plot:");
				if (plotID.equalsIgnoreCase("timed out")) {
					p.sendMessage(Chatter.TagMessage("You took too long! Operation cancelled!"));
				}
				else {
					Plot plot = new Plot(plotID, sel.getMinPoint(), sel.getMaxPoint(), t.getID(), PlotType.GOVERNMENT, p.getName());
					
					
				}
			}
		}
	}
}