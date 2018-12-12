package com.ellirion.core.groundwar.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;

public class MoveOffGroundWarListener extends TeleportBackEventListener {

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
        if (groundWar != null && groundWar.getState() == GroundWar.State.IN_PROGRESS &&
            !(groundWar.getPlotB().equals(plotTo) || groundWar.getPlotA().equals(plotTo))) {
            teleportPlayerBack(plotFrom, plotTo, player);
        }
    }
}
