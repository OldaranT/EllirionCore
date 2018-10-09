package com.ellirion.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.races.RaceManager;

import java.util.Set;

public class JoinRaceCommand implements CommandExecutor {

    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        if (!(sender instanceof Player)) {
            sendmsg("only a player may join a team");
            return false;
        }
        Player player = (Player) sender;
        Set<String> raceNames = RaceManager.getRaceNames();
        if (args.length <= 0) {
            String message = "";
            message += "you forgot to add the race you want to join. you can join one of the following races:\n ";
            message += raceNames;
            sendmsg(message);
            return false;
        }

        if (!RaceManager.raceExists(args[0])) {
            String message = "";
            message += "the given race does not exist. possible races to join are: \n";
            message += raceNames;
            sendmsg(message);
            return false;
        }

        if (RaceManager.hasRace(player)) {
            sendmsg("you already have a race");
            return false;
        }

        if (!PlayerManager.setPlayerRace(player, args[0])) {
            sendmsg("something went wrong please try again with different arguments");
            return false;
        }

        sender.getServer().broadcastMessage(sender.getName() + " joined " + args[0]);
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
