package com.ellirion.core.race.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.util.StringHelper;

public class DestroyRaceCommand implements CommandExecutor {

    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        if (args.length <= 0) {
            sendmsg("Wrong number of arguments.");
            return true;
        }
        String raceName = String.join(" ", args);
        if (!RaceManager.raceExists(raceName)) {
            sendmsg("Given race does not exist.");
        }
        if (!RaceManager.deleteRace(raceName)) {
            sendmsg("Could not delete the race.");
            return true;
        }
        sendmsg(StringHelper.normalNameCasing(raceName) + " is no more!");
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
