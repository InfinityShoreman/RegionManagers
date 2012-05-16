package me.sirsavary.townmanager.commands.town;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Plot;
import me.sirsavary.townmanager.objects.PlotType;
import me.sirsavary.townmanager.objects.Selection;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TownCreate extends AbstractCommand
{
	Town t = Main.fileManager.getPlayerTown((Player) sender);

	public TownCreate(CommandSender sender, boolean async, Main plugin) throws Exception {
		super(sender, async, plugin);
	}

	private void CreateTown() {
		sender.sendMessage("");
		sender.sendMessage("Please head to your town hall");
		if (Main.questioner.ask((Player)sender, "When you are there type anything to continue") != null) {

			sender.sendMessage("");
			sender.sendMessage(Chatter.Message("Before we can go to the next step,"));
			sender.sendMessage(Chatter.Message("Make sure your hand is empty"));
			if (Main.questioner.ask((Player)sender, "When it is empty type anything to continue") != null) {
				ItemStack brickStack = new ItemStack(Material.CLAY_BRICK, 1);
				Player player = (Player) sender;
				player.setItemInHand(brickStack);
				sender.sendMessage("");
				sender.sendMessage(Chatter.Message("This is your town brick!"));
				sender.sendMessage(Chatter.Message("It is used for town construction functions,"));
				sender.sendMessage(Chatter.Message("Including plot management and land claiming!"));
				sender.sendMessage(Chatter.Message("With the brick in your hand,"));
				sender.sendMessage(Chatter.Message("Left click one corner of your town hall"));
				sender.sendMessage(Chatter.Message("Then, right click the other corner"));
				if (Main.questioner.ask((Player)sender, "When both corners are selected type anything to continue") != null) {

					Selection sel = Main.regionHandler.getSelection(player);

					if (sel == null) {
						sender.sendMessage("");
						player.sendMessage(Chatter.Message("You did not select your town hall!"));
						player.sendMessage(Chatter.Message("Now we have to do this all over again!"));
						player.sendMessage(Chatter.Message("Type /town create when you'd like to try again"));
					}
					else {
						sender.sendMessage("");
						sender.sendMessage(Chatter.Message("Your town is almost complete!"));
						sender.sendMessage(Chatter.Message("The last step is to choose a name for your town!"));
						String townName = Main.questioner.ask((Player)sender, "Please choose a name for your town:");

						Plot region = new Plot(townName + "_townhall", sel.getMinPoint(), sel.getMaxPoint(), townName, PlotType.GOVERNMENT, sender.getName());
						
						Town newTown = new Town(townName, region, player.getName());
						Main.fileManager.SaveTown(newTown);
						Main.fileManager.AddCitizen(player.getName(), newTown);
						Main.fileManager.SavePlot(region);
						Main.fileManager.TrackPlotChunks(region);

						sender.sendMessage("");
						sender.sendMessage(Chatter.Message("Congratulations!"));
						sender.sendMessage(Chatter.Message("You survived the town creation process!"));
						sender.sendMessage(Chatter.Message("Don't forget to setup your town flags and settings!"));

						sender.sendMessage("");
						if (Main.questioner.ask((Player)sender, "Edit flags and settings now?", "yes", "no").equals("yes")) {
							//new ConfigureTown(sender, true, null);
						}
					}
				}
			}

		}
	}
	@Override
	public void run() {
		sender.sendMessage(Chatter.TagMessage("Welcome to the town create interface!"));
		if (t != null) {
			sender.sendMessage("");
			if (t.getMayor().equalsIgnoreCase(sender.getName())) {
				sender.sendMessage(Chatter.Message("You are currently the mayor of " + t.getColor() + t.getID()));
				sender.sendMessage(Chatter.Message("You must first delete " + t.getColor() + t.getID() + Main.messageColor + " before making a new town"));
			}
			else {
				sender.sendMessage(Chatter.Message("You are currently a resident of " + t.getColor() + t.getID()));
				sender.sendMessage(Chatter.Message("If you leave you will lose your home protection in the process"));
				if (Main.questioner.ask((Player)sender, "Are you sure you want to leave it to create your own town?", "yes", "no").equals("yes"))
					CreateTown();
			}
		}
		if (t == null)
			CreateTown();
	}
}