package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static com.ellirion.core.playerdata.PlayerManager.*;

public class OnPlayerJoin implements Listener {

    /**
     * @param event the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();
        if (playerexists(playerID) || playerExistsInDatabase(playerID)) {
            event.setJoinMessage("Welcome back!");
            addPlayerFromDatabase(playerID);
        } else {
            event.setJoinMessage("Welcome!");
            newPlayer(player, null);
        }
    }
}
