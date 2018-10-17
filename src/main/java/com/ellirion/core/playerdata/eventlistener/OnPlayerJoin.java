package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.ellirion.core.playerdata.PlayerManager;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    /**
     * @param event the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();
        if (PlayerManager.playerexists(playerID)) {
            event.setJoinMessage("Welcome back!");
            PlayerManager.updatePlayer(playerID);
        } else {
            event.setJoinMessage("Welcome!");
            PlayerManager.newPlayer(player, null, "outsider", 0);
        }
    }
}
