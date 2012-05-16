package me.sirsavary.townmanager.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sirsavary.townmanager.Chatter;
import me.sirsavary.townmanager.Main;
import me.sirsavary.townmanager.commands.AbstractCommand;
import me.sirsavary.townmanager.objects.Town;

public class TownInvite extends AbstractCommand {

	private final Town townToJoin;
	private final Player invitedPlayer;
	
	public TownInvite(CommandSender sender, boolean async, Main plugin, String player)
			throws Exception {
		super(sender, async, plugin);
		townToJoin = Main.fileManager.getPlayerTown((Player) sender);
		invitedPlayer = Main.server.getPlayer(player);
	}

	@Override
	public void run() {
		if (invitedPlayer != null) { //Invited player exists
			if (Main.fileManager.getPlayerTown(invitedPlayer) == null) { //Invited player is not already part of a town
				if (townToJoin.getMayor().equalsIgnoreCase(sender.getName())) { //Invitee is the mayor of the town
					//Send the player an invitation in the form of a question
					invitedPlayer.sendMessage(Chatter.Message("The mayor of " + townToJoin.getColor() + townToJoin.getID() + Main.messageColor + " has invited you to join their town!"));
					if (Main.questioner.ask(invitedPlayer, Chatter.Message("Would you like to join?"), Chatter.Message("yes"), Chatter.Message("no")).equals("yes")) { //If they accept it, add them to the town
						invitedPlayer.sendMessage(Chatter.Message("You have accepted " + townToJoin.getColor() + townToJoin.getMayor() + "'s" + Main.messageColor + " invitation!"));
						sender.sendMessage(Chatter.Message(invitedPlayer.getName() + " has joined " + townToJoin.getColor() + townToJoin.getID() + Main.messageColor + "!"));
						Main.fileManager.AddCitizen(invitedPlayer.getName(), townToJoin);
					}
					else { //If they do not accept it, tell the mayor they're a fag
						invitedPlayer.sendMessage(Chatter.Message("You have denied " + townToJoin.getColor() + townToJoin.getMayor() + "'s" + Main.messageColor + " invitation"));
					}
				}
				else { //Invitee is not the mayor of the town
					sender.sendMessage(Chatter.Message("You are not the mayor of " + townToJoin.getColor() + townToJoin.getID() + Main.messageColor + "!"));
				}
			}
			else { //Invited player is already part of a town
				sender.sendMessage(Chatter.Message(invitedPlayer.getName() + " is already a part of a town!"));
			}
		}
		else { //Invited player does not exist
			sender.sendMessage(Chatter.Message("That player does not exist!"));
		}
	}

}
