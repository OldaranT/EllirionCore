package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.ellirion.core.playerdata.PlayerManager;

public class OnPlayerJoin implements Listener {

    /**
     * @param event the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (PlayerManager.playerexists(p)) {
            event.setJoinMessage("Welcome back!");
            PlayerManager.updatePlayer(p);
        } else {
            event.setJoinMessage("Welcome!");
            PlayerManager.newPlayer(p, null, "outsider", 0);
        }
    }
}
