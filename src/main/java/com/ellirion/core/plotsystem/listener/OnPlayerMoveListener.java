package com.ellirion.core.plotsystem.listener;

import com.ellirion.core.EllirionCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.util.LoggingUtils;
import com.ellirion.core.util.PlayerScoreboardHelper;
import com.ellirion.core.util.model.PlayerScoreboard;

public class OnPlayerMoveListener implements Listener {

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
                EllirionCore.getINSTANCE().getServer().getScheduler().runTaskLater(EllirionCore.getINSTANCE(), r -> {
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                        LoggingUtils.printStackTrace(e);
                    }
                    new PlotChangeEvent(event.getPlayer(), from, to).call();
                }, 1);
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

        Player player = event.getPlayer();
        PlayerScoreboard board = PlayerScoreboardHelper.getPlayerScoreboard(player.getUniqueId());
        if (board != null) {
            board.updateBoard();
            board.showScoreboard();
        }
    }
}

