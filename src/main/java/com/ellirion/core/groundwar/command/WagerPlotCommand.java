package com.ellirion.core.groundwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.UUID;

public class WagerPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        UUID playerId = player.getUniqueId();

        //Get plot
        Plot plot = null;
        if (strings.length > 0) {
            //Assume coordinates entered in command
            try {
                int x = Integer.parseInt(strings[0]);
                int z = Integer.parseInt(strings[1]);
                PlotCoord coord = new PlotCoord(x, z, player.getWorld().getName());
                plot = PlotManager.getPlotByCoordinate(coord);
            } catch (Exception e) {
                player.sendMessage("Could not get plot. Please try again, entering the correct coordinates or standing in the plot you want to add.");
                return true;
            }
        } else {
            //Take plot from player location
            plot = PlotManager.getPlotFromLocation(player.getLocation());
        }

        //Check if plot can be added to ground war
        if (GroundWarManager.canAddPlot(playerId, plot)) {
            GroundWarManager.addPlotToGroundWar(playerId, plot);
            player.sendMessage(ChatColor.GREEN + "The plot was added to the ground war.");
        } else {
            player.sendMessage(ChatColor.DARK_RED + "The plot could not be added to the ground war.");
        }

        return true;
    }
}
