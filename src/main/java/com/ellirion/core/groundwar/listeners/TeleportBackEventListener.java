package com.ellirion.core.groundwar.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import com.ellirion.core.groundwar.util.LocationHelper;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

public class TeleportBackEventListener implements Listener {

    /**
     * Teleport a player back a few blocks into the plot they came from.
     * @param plotFrom the plot the player came from
     * @param plotTo the plot the player moved to
     * @param player the player that moved
     */
    protected void teleportPlayerBack(Plot plotFrom, Plot plotTo, Player player) {
        //Teleport the player a few blocks back into the plot
        //check which way the player was going
        PlotCoord direction = plotTo.getPlotCoord().subtract(plotFrom.getPlotCoord());
        int deltaX = -5 * direction.getX();
        int deltaZ = -5 * direction.getZ();

        Location newLocation = LocationHelper.getSafeLocation(player.getLocation().getBlock().getLocation()
                                                                      .add(deltaX, 0, deltaZ));
        newLocation.setYaw(player.getLocation().getYaw());
        newLocation.setPitch(player.getLocation().getPitch());
        player.teleport(newLocation);
    }
}
