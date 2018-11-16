package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.util.PlayerScoreboardManager;
import com.ellirion.core.util.model.PlayerScoreboard;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    /**
     * @param event the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();

        if (PlotManager.getSavedPlots().size() > 0) {
            PlayerScoreboard board = new PlayerScoreboard(player, EllirionCore.getINSTANCE().getServer().getScoreboardManager().getNewScoreboard());
            board.showScoreboard();
            PlayerScoreboardManager.addPlayerScoreboard(playerID, board);
        }

        if (PlayerManager.playerexists(playerID)) {
            event.setJoinMessage("Welcome back!");
            PlayerManager.updatePlayer(playerID);
        } else {
            event.setJoinMessage("Welcome!");
            PlayerManager.newPlayer(player, null);
        }
    }
}
