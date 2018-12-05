package com.ellirion.core.race.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.RaceManager;

import java.util.List;

import static com.ellirion.core.util.StringHelper.*;

public class JoinRaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        Plot plot;
        String raceName;

        if (strings.length <= 0) {
            plot = PlotManager.getPlotFromLocation(player.getLocation());
            if (plot == null) {
                player.sendMessage(ChatColor.DARK_RED + "There is no plot on your location.");
                return true;
            }

            raceName = plot.getOwner().getName();

            if (!PlayerManager.setPlayerRace(player.getUniqueId(), plot.getOwner().getRaceUUID())) {
                player.sendMessage(ChatColor.DARK_RED + "It was not possible to add you to the race " +
                                   highlight(raceName, ChatColor.RESET));
                return true;
            }
        } else {
            raceName = strings[0];
            List<String> raceNames = RaceManager.getAllRaceNames();
            if (!raceNames.contains(raceName)) {
                player.sendMessage(
                        ChatColor.DARK_RED + "The race " + highlight(raceName, ChatColor.DARK_RED) +
                        " does not exist.");
                return true;
            }

            if (!PlayerManager.setPlayerRace(player.getUniqueId(), RaceManager.getRaceUUID(raceName))) {
                player.sendMessage(ChatColor.DARK_RED + "It was not possible to add you to the race " +
                                   highlight(raceName, ChatColor.RESET));
                return true;
            }
        }
        player.sendMessage(
                ChatColor.GREEN + "You have joined the race " + highlight(raceName, ChatColor.GREEN) +
                "!");

        return true;
    }
}
