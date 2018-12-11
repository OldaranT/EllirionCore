package com.ellirion.core.race.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.util.StringHelper;

public class DeleteRaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "Wrong number of arguments.");
            return true;
        }
        String raceName = String.join(" ", strings);
        if (!RaceManager.raceExists(raceName)) {
            player.sendMessage(ChatColor.DARK_RED + "Given race does not exist.");
            return true;
        }
        if (!RaceManager.deleteRace(raceName)) {
            player.sendMessage(ChatColor.DARK_RED + "Could not delete the race.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + StringHelper.normalNameCasing(raceName) + " is no more!");
        return true;
    }
}
