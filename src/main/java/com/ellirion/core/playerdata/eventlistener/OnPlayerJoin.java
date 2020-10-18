package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerHelper;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.util.PlayerScoreboardHelper;
import com.ellirion.core.util.model.PlayerScoreboard;
import com.ellirion.core.gamemanager.GameManager;

import java.util.UUID;

import static com.ellirion.core.playerdata.util.JoinPlayerUtils.joinPlayer;

public class OnPlayerJoin implements Listener {

    /**
     * @param event the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();

        //Give player scoreboard
        if (PlotManager.getSavedPlots().size() > 0) {
            PlayerScoreboard board = new PlayerScoreboard(player, EllirionCore.getINSTANCE().getServer().getScoreboardManager().getMainScoreboard());
            board.showScoreboard();
            PlayerScoreboardHelper.addPlayerScoreboard(playerID, board);
        }

        GameManager.GameState state = GameManager.getInstance().getState();
        if (state != GameManager.GameState.IN_PROGRESS && state != GameManager.GameState.SAVING) {
            return;
        }

        PlayerHelper.updateScoreboard(player);
        joinPlayer(playerID);
    }
}
