package com.ellirion.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        Player p = (Player) sender;
        Set<String> raceNames = RaceManager.getRaceNames();
        if (args.length <= 0) {
            String s = "";
            s += "you forgot to add the race you want to join. you can join one of the following races:\n ";
            s += raceNames;
            sendmsg(s);
            return false;
        }

        if (!RaceManager.raceExists(args[0])) {
            String s = "";
            s += "the given race does not exist. possible races to join are: \n";
            s += raceNames;
            sendmsg(s);
            return false;
        }

        if (RaceManager.hasRace(p)) {
            sendmsg("you already have a race");
            return false;
        }

        //        if (!PlayerManager.setPlayerRace(p, RaceManager.getRaceByName(args[0]))) {
        //            sendmsg("something went wrong please try again with different arguments");
        //            return false;
        //        }
        if (!RaceManager.addPlayerToRace(p, args[0])) {
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
