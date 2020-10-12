package com.ellirion.core.race.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarHelper;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.RaceHelper;
import com.ellirion.core.race.model.Race;

import java.util.List;
import java.util.UUID;

import static com.ellirion.core.playerdata.PlayerHelper.setPlayerRace;
import static com.ellirion.core.playerdata.PlayerHelper.getPlayerRace;
import static com.ellirion.core.playerdata.PlayerHelper.updateScoreboard;
import static com.ellirion.core.util.StringHelper.highlight;

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

            if (!setPlayerRace(playerID, raceID)) {
                player.sendMessage(ChatColor.DARK_RED + "It was not possible to add you to the race " +
                                   highlight(raceName, ChatColor.RESET));
                return true;
            }
        } else {
            raceName = strings[0];

            List<String> raceNames = RaceHelper.getAllRaceNames();
            if (!raceNames.contains(raceName)) {
                player.sendMessage(
                        ChatColor.DARK_RED + "The race " + highlight(raceName, ChatColor.DARK_RED) +
                        " does not exist.");
                return true;
            }

            UUID raceID = RaceHelper.getRaceUUID(raceName);

            if (!canJoinRace(raceID)) {
                return true;
            }

            if (!setPlayerRace(player.getUniqueId(), RaceHelper.getRaceUUID(raceName))) {
                player.sendMessage(ChatColor.DARK_RED + "It was not possible to add you to the race " +
                                   highlight(raceName, ChatColor.RESET));
                return true;
            }
        }

        //Update color of the player.
        updateScoreboard(player);

        player.sendMessage(
                ChatColor.GREEN + "You have joined the race " + highlight(raceName, ChatColor.GREEN) +
                "!");

        return true;
    }

    private boolean canJoinRace(UUID raceID) {
        UUID playerID = player.getUniqueId();
        Race playerRace = getPlayerRace(playerID);
        if (playerRace != null && playerRace.getRaceUUID() == raceID) {
            player.sendMessage(ChatColor.DARK_RED + "You are already in this race.");
            return false;
        }

        if (GroundWarHelper.getGroundWar(playerID) != null) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are currently in a groundwar. You are not allowed to change your race.");
            return false;
        }
        return true;
    }
}
