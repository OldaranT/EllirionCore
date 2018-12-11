package com.ellirion.core.groundwar.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

public class MoveOffGroundWarListener implements Listener {

    /**
     * Listener that is listening if a player is changing plot.
     * @param event plot change event.
     */
    @EventHandler
    public void onPlotChange(PlotChangeEvent event) {
        //Get the player
        Player player = event.getPlayer();

        //See if the player moved to a plot that isn't in the ground war
        Plot plotTo = event.getPlotTo();
        Plot plotFrom = event.getPlotFrom();

        GroundWar groundWar = GroundWarManager.getGroundWar(player.getUniqueId());
        if (groundWar != null && groundWar.getState() == GroundWar.State.IN_PROGRESS && !(groundWar.getPlotB().equals(plotTo) || groundWar.getPlotA().equals(plotTo))) {
            //Teleport the player a few blocks back into the plot
            //check which way the player was going
            PlotCoord direction = plotTo.getPlotCoord().subtract(plotFrom.getPlotCoord());
            int deltaX = -5 * direction.getX();
            int deltaZ = -5 * direction.getZ();

            Location newLocation = player.getLocation().getBlock().getLocation().add(deltaX, 0, deltaZ);
            newLocation.setYaw(player.getLocation().getYaw());
            newLocation.setPitch(player.getLocation().getPitch());
            player.teleport(newLocation);
        }
    }
}
