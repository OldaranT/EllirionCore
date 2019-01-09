package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.ellirion.core.gamemanager.GameManager;

import java.util.UUID;

import static com.ellirion.core.playerdata.PlayerManager.*;
import static com.ellirion.core.playerdata.util.JoinPlayer.*;

public class OnPlayerJoin implements Listener {

    /**
     * @param event the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();
        GameManager.GameState state = GameManager.getInstance().getState();
        if (state != GameManager.GameState.IN_PROGRESS && state != GameManager.GameState.SAVING) {
            return;
        }

        joinPlayer(playerID);
        updateScoreboard(player);
    }
}
