package com.ellirion.core.groundwar.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;

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

        GroundWar war = GroundWarManager.getGroundWar(player.getUniqueId());
        if (war != null && !(war.getPlotB().equals(plotTo) || war.getPlotA().equals(plotTo))) {
            //Teleport the player back to the block they were standing on
            Location newLocation = player.getLocation().getBlock().getLocation();
            player.teleport(newLocation);
        }
    }
}
