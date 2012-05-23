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
		Player player = (Player) sender;
		player.sendMessage("");
		player.sendMessage(Chatter.Message("Please head to your town hall"));
		if (Main.questioner.ask(player, Chatter.Message("When you are there type anything to continue")) != null) {

			player.sendMessage("");
			player.sendMessage(Chatter.Message("Before we can go to the next step,"));
			player.sendMessage(Chatter.Message("Make sure your hand is empty"));
			if (Main.questioner.ask((Player)player, "When it is empty type anything to continue") != null) {
				ItemStack brickStack = new ItemStack(Material.CLAY_BRICK, 1);		
				player.setItemInHand(brickStack);
				player.sendMessage("");
				player.sendMessage(Chatter.Message("This is your town brick!"));
				player.sendMessage(Chatter.Message("It is used for town construction functions,"));
				player.sendMessage(Chatter.Message("Including plot management and land claiming!"));
				player.sendMessage(Chatter.Message("With the brick in your hand,"));
				player.sendMessage(Chatter.Message("Left click one corner of your town hall"));
				player.sendMessage(Chatter.Message("Then, right click the other corner"));
				if (Main.questioner.ask((Player)player, "When both corners are selected type anything to continue") != null) {

					Selection sel = Main.regionHandler.getSelection(player);

					if (sel == null) {
						player.sendMessage("");
						player.sendMessage(Chatter.Message("You did not select your town hall!"));
						player.sendMessage(Chatter.Message("Now we have to do this all over again!"));
						player.sendMessage(Chatter.Message("Type /town create when you'd like to try again"));
					}
					else {
						player.sendMessage("");
						player.sendMessage(Chatter.Message("Your town is almost complete!"));
						player.sendMessage(Chatter.Message("The last step is to choose a name for your town!"));
						String townName = Main.questioner.ask((Player)player, "Please choose a name for your town:");
						if (townName.equalsIgnoreCase("timed out")) {
							player.sendMessage(Chatter.TagMessage("You took too long! Operation cancelled!"));
						}
						else {
							Plot region = new Plot("townhall", sel.getMinPoint(), sel.getMaxPoint(), townName, PlotType.GOVERNMENT, player.getName());
							
							Town newTown = new Town(townName, region, player.getName());
							Main.fileManager.SaveTown(newTown);
							Main.fileManager.AddCitizen(player.getName(), newTown);
							Main.fileManager.SavePlot(region);
							Main.fileManager.TrackPlotChunks(region);

							player.sendMessage("");
							player.sendMessage(Chatter.Message("Congratulations!"));
							player.sendMessage(Chatter.Message("You survived the town creation process!"));

							/*player.sendMessage("");
							if (Main.questioner.ask((Player)player, "Edit flags and settings now?", "yes", "no").equals("yes")) {
								//new ConfigureTown(player, true, null);
							}*/
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