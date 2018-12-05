package com.ellirion.core.util;

import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

public class CommandHelper {

    /**
     * A method that helps to check if a player has entered coords of a plot or not.
     * @param strings arguments of the players.
     * @param player the player that used the command.
     * @return the plot the player referred to.
     */
    public static Plot getPlot(String[] strings, Player player) {
        // Check if coords where entered.
        if (strings.length == 2) {
            int xCoord = Integer.parseInt(strings[0]);
            int zCoord = Integer.parseInt(strings[1]);

            PlotCoord plotCoord = new PlotCoord(xCoord, zCoord, player.getWorld().getName());

            return PlotManager.getPlotByCoordinate(plotCoord);
        } else if (strings.length == 0) {
            return PlotManager.getPlotFromLocation(player.getLocation());
        } else {
            return null;
        }
    }
}
