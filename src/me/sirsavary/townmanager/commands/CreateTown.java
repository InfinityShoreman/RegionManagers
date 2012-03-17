package me.sirsavary.townmanager.commands;

import me.sirsavary.questioner.LogBlockQuestioner;
import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CreateTown extends AbstractCommand
{
	String playerTown = (String) Main.fileManager.getValue("Players." + sender.getName() + ".Town");
	
	private final LogBlockQuestioner questioner;
	private final WorldGuardPlugin worldGuard;
	private WorldEditPlugin worldEdit;
	
	public CreateTown(CommandSender sender, boolean async, Main plugin) throws Exception {
		super(sender, async, plugin);
		
		questioner = (LogBlockQuestioner)plugin.getServer().getPluginManager().getPlugin("Questioner");
		this.worldGuard = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		try {
			this.worldEdit = worldGuard.getWorldEdit();
		} catch (CommandException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {		
			sender.sendMessage(Chatter.TagMessage("Welcome to the TownManager interface!"));
			if (playerTown == null)
			{
				sender.sendMessage("");
				sender.sendMessage(Chatter.Message("You are currently a resident of " + playerTown));
				sender.sendMessage(Chatter.Message("If you leave you will lose your home protection in the process"));					
				if (questioner.ask((Player)sender, Chatter.Message("Are you sure you want to leave it to create your own town?"), Chatter.Message("yes"), Chatter.Message("no")).equals("yes")) {
					
					sender.sendMessage("");
					sender.sendMessage(Chatter.Message("Please head to your town hall"));
					 if (questioner.ask((Player)sender, Chatter.Message("When you are there type anything to continue")) != null) {

						sender.sendMessage("");
						sender.sendMessage(Chatter.Message("Before we can go to the next step,"));
						sender.sendMessage(Chatter.Message("Make sure your hand is empty"));
						 if (questioner.ask((Player)sender, Chatter.Message("When it is empty type anything to continue")) != null) {
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
							 if (questioner.ask((Player)sender, Chatter.Message("When both corners are selected type anything to continue")) != null) {
								
								Selection sel = worldEdit.getSelection(player);
								
								if (sel == null) {
									sender.sendMessage("");
									player.sendMessage(Chatter.Message("You did not select your town hall!"));
									player.sendMessage(Chatter.Message("Now we have to do this all over again!"));
									player.sendMessage(Chatter.Message("Type /town create when you'd like to try again"));
								}
								else {
									RegionManager mgr = worldGuard.getGlobalRegionManager().get(sel.getWorld());
									
									sender.sendMessage("");
									sender.sendMessage(Chatter.Message("Your town is almost complete!"));
									sender.sendMessage(Chatter.Message("The last step is to choose a name for your town!"));
									String townName = questioner.ask((Player)sender, Chatter.Message("Please choose a name for your town:"));
									
									ProtectedRegion region;
									
									DefaultDomain owners = new DefaultDomain();
									owners.addPlayer(player.getName());
									
									if (sel instanceof Polygonal2DSelection) {
							            Polygonal2DSelection polySel = (Polygonal2DSelection) sel;
							            int minY = polySel.getNativeMinimumPoint().getBlockY();
							            int maxY = polySel.getNativeMaximumPoint().getBlockY();
							            region = new ProtectedPolygonalRegion("town_" + townName, polySel.getNativePoints(), minY, maxY);
										region.setOwners(owners);
										mgr.addRegion(region);
							        } else if (sel instanceof CuboidSelection) {
							            BlockVector min = sel.getNativeMinimumPoint().setY(0).toBlockVector();
							            BlockVector max = sel.getNativeMaximumPoint().setY(255).toBlockVector();
							            region = new ProtectedCuboidRegion("town_" + townName, min, max);
										region.setOwners(owners);
										mgr.addRegion(region);
							        }
									
									try {
										mgr.save();
									} catch (ProtectionDatabaseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sender.sendMessage("");
									sender.sendMessage(Chatter.Message("Congratulations!"));
									sender.sendMessage(Chatter.Message("You survived the town creation process!"));
									sender.sendMessage(Chatter.Message("Don't forget to setup your town flags and settings!"));
								}
							}
						}
						
					}
				}
			}
	}
}