package com.ellirion.core.plotsystem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.util.PlotManager;

public class PlotListener implements Listener {

    /**
     * Listener that is listening to player movement.
     * @param event player movement event.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (PlotManager.getPLOT_SIZE() != 0) {
            Plot from = PlotManager.getPlotFromLocation(event.getFrom());
            Plot to = PlotManager.getPlotFromLocation(event.getTo());

            if (from == null || to == null) {
                return;
            }

            if (!from.equals(to)) {
                new PlotChangeEvent(event.getPlayer(), from, to).call();
            }
        }
    }

    /**
     * Listener that is listening if a player is changing plot.
     * @param event plot change event.
     */
    @EventHandler
    public void onPlotChange(PlotChangeEvent event) {
        event.getPlayer().sendMessage(event.getPlayer().getDisplayName() + " moved from " +
                                      event.getPlotFrom().getName() + " to " +
                                      event.getPlotTo().getName());
    }
}

