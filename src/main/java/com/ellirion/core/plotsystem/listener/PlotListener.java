package com.ellirion.core.plotsystem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;

public class PlotListener implements Listener {

    /**
     * Listener that is listening to player movement.
     * @param event player movement event.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        GameManager gameManager = GameManager.getInstance();

        if (gameManager.getPlotSize() != 0) {
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
                                      event.getPlotTo().getName() + "\nThis Plot is owned by " +
                                      event.getPlotTo().getOwner().getName());
    }
}

