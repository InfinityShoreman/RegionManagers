package me.sirsavary.townmanager.commands.town;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.TaxType;
import me.sirsavary.townmanager.objects.Town;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownSettings extends AbstractCommand {

	private final Player p;
	
	public TownSettings(CommandSender sender, boolean async, Main plugin)
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
		else if (!t.getMayor().equals(p.getName())){
			sender.sendMessage(Chatter.Message("You are not the mayor of " + t.getFormattedID() + "!"));
		}
		else {
			String setting = Main.questioner.ask(p, false, ("Please choose a setting:"), ("color"), ("spawnlocation"), ("motd"), ("taxtype"), ("taxamount"), ("flags"));
			
			if (setting.equalsIgnoreCase("color")) {
				p.sendMessage(Chatter.TagMessage("Current color is " + t.getColor() + t.getColor().name().toLowerCase()));
				t.setColor(ChatColor.valueOf(ChatColor.stripColor(Main.questioner.ask(p, false, ("Please select a new color:"), (ChatColor.AQUA + "aqua"), (ChatColor.BLUE + "blue"), (ChatColor.DARK_AQUA + "dark_aqua"), (ChatColor.DARK_BLUE + "dark_blue"), (ChatColor.DARK_GRAY + "dark_gray"), (ChatColor.DARK_GREEN + "dark_green"), (ChatColor.DARK_PURPLE + "dark_purple"), (ChatColor.DARK_RED + "dark_red"), (ChatColor.GOLD + "gold"), (ChatColor.GRAY + "gray"), (ChatColor.GREEN + "green"), (ChatColor.RED + "red"), (ChatColor.YELLOW + "yellow"), (ChatColor.WHITE + "white")).toUpperCase())));
				p.sendMessage(Chatter.TagMessage("Color has been changed to " + t.getColor() + t.getColor().name().toLowerCase()));

			}
			else if (setting.equalsIgnoreCase("spawnlocation")) { 
				Location loc = t.getSpawnLocation();
				p.sendMessage(Chatter.TagMessage("Spawn location is currently at " + loc.getX() + "," + loc.getY() + "," + loc.getZ()));
				if (Main.questioner.ask(p, "Go to the new spawn location, and then type anything to continue") != null) {
					t.setSpawnLocation(p.getLocation());
					loc = t.getSpawnLocation();
					p.sendMessage(Chatter.TagMessage("Spawn location set to " + loc.getX() + "," + loc.getY() + "," + loc.getZ()));
				}
			}
			else if (setting.equalsIgnoreCase("motd")) { 
				p.sendMessage(Chatter.TagMessage("Current MOTD is: " + t.getMOTD()));
				t.setMOTD(Main.questioner.ask(p, "Please enter the new MOTD"));
				p.sendMessage(Chatter.TagMessage("MOTD set to: " + t.getMOTD()));
			}
			else if (setting.equalsIgnoreCase("taxtype")) { 
				p.sendMessage(Chatter.TagMessage("Current TaxType is: " + t.getTaxType().name()));
				t.setTaxType(TaxType.valueOf(Main.questioner.ask(p, false, ("Please choose a TaxType:"), ("flat"), ("income"), ("resource"), ("property"), ("none")).toUpperCase()));
				p.sendMessage(Chatter.TagMessage("TaxType set to: " + t.getTaxType().name()));
			}
			else if (setting.equalsIgnoreCase("taxamount")) { 
				p.sendMessage(Chatter.TagMessage("Current tax is: " + t.getTax()));
				t.setMOTD(Main.questioner.ask(p, "Please enter the new tax"));
				p.sendMessage(Chatter.TagMessage("Tax set to: " + t.getTax()));
			}
			else if (setting.equalsIgnoreCase("flags")) { 
				String flag = Main.questioner.ask(p, false, ("Please select a flag:"), ("pvp"), ("freebuild"));
				
				if (flag.equalsIgnoreCase("pvp")) {
					p.sendMessage(Chatter.TagMessage("PVP is currently set to: " + t.isPVPAllowed().toString()));
					t.setPVPAllowed(Boolean.valueOf(Main.questioner.ask(p, false, ("Change PVP to:"), ("true"), ("false"))));
					p.sendMessage(Chatter.TagMessage("PVP set to: " + t.isPVPAllowed().toString()));
				}
				else if (flag.equalsIgnoreCase("freebuild")) {
					p.sendMessage(Chatter.TagMessage("FreeBuild is currently set to: " + t.isFreeBuildAllowed().toString()));
					t.setFreeBuildAllowed(Boolean.valueOf(Main.questioner.ask(p, false, ("Change FreeBuild to:"), ("true"), ("false"))));
					p.sendMessage(Chatter.TagMessage("FreeBuild set to: " + t.isFreeBuildAllowed().toString()));
				}
			}
			Main.fileManager.UpdateTown(t);
		}
	}
}