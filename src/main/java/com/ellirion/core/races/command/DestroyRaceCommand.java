package com.ellirion.core.races.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.races.RaceManager;

public class DestroyRaceCommand implements CommandExecutor {

    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        if (args.length <= 0 || args.length >= 2) {
            sendmsg("wrong number of arguments.");
            return true;
        }
        if (!RaceManager.raceNameExists(args[0])) {
            sendmsg("given race does not exist.");
        }
        if (!RaceManager.deleteRaceByName(args[0])) {
            sendmsg("could not delete the race.");
            return true;
        }
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
