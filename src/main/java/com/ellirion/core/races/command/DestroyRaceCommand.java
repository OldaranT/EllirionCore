package com.ellirion.core.races.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.races.RaceManager;
import com.ellirion.core.utils.StringUtil;

public class DestroyRaceCommand implements CommandExecutor {

    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        if (args.length <= 0) {
            sendmsg("wrong number of arguments.");
            return true;
        }
        String raceName = String.join(" ", args);
        if (!RaceManager.raceNameExists(raceName)) {
            sendmsg("given race does not exist.");
        }
        if (!RaceManager.deleteRaceByName(raceName)) {
            sendmsg("could not delete the race.");
            return true;
        }
        sendmsg(StringUtil.normalNameCasing(raceName) + " is no more!");
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
