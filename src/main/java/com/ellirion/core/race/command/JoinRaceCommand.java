package com.ellirion.core.race.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.race.model.Race;

import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.StringHelper.*;

public class JoinRaceCommand implements CommandExecutor {

    private Player player;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        player = (Player) commandSender;
        Plot plot;
        String raceName;
        UUID playerID = player.getUniqueId();

        if (strings.length <= 0) {
            plot = PlotManager.getPlotFromLocation(player.getLocation());
            if (plot == null) {
                player.sendMessage(ChatColor.DARK_RED + "There is no plot on your location.");
                return true;
            }

            raceName = plot.getOwner().getName();
            UUID raceID = plot.getOwner().getRaceUUID();

            if (!canJoinRace(raceID)) {
                return true;
            }

            if (!PlayerManager.setPlayerRace(playerID, raceID)) {
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

            UUID raceID = RaceManager.getRaceUUID(raceName);

            if (!canJoinRace(raceID)) {
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

    private boolean canJoinRace(UUID raceID) {
        UUID playerID = player.getUniqueId();
        Race playerRace = PlayerManager.getPlayerRace(playerID);
        if (playerRace != null && playerRace.getRaceUUID() == raceID) {
            player.sendMessage(ChatColor.DARK_RED + "You are already in this race.");
            return false;
        }

        if (GroundWarManager.getGroundWar(playerID) != null) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are currently in a groundwar. You are not allowed to change your race.");
            return false;
        }
        return true;
    }
}
