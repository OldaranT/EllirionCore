package com.ellirion.core.races.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.races.RaceManager;

public class CreateRaceCommand implements CommandExecutor {

    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        if (args.length <= 0) {
            sendmsg("please give a race name and color");
            return false;
        }
        if (args.length == 1) {
            sendmsg("you forgot either the color or the name");
            return false;
        }
        if (RaceManager.raceExists(args[0])) {
            sendmsg("race already exists");
            return false;
        }
        ChatColor color = ChatColor.valueOf(args[1].toUpperCase());

        if (color == null || RaceManager.isColerInUse(color)) {
            sendmsg("you either miss spelled the color or the color is in use");
            return false;
        }

        if (!RaceManager.addRace(args[0], color)) {
            sendmsg("something went wrong please try with different values");
            return false;
        }
        sendmsg("race created");
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
