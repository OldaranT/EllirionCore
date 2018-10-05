package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.ellirion.core.playerdata.PlayerManager;

public class OnPlayerJoin implements Listener {

    /**
     * @param e the event.
     */
    @EventHandler
    public void onPlayerJoinEventListener(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (PlayerManager.playerexists(p)) {
            e.setJoinMessage("Welcome back!");
            PlayerManager.updatePlayer(p);
        } else {
            e.setJoinMessage("Welcome!");
            PlayerManager.newPlayer(p, "Wildernis", "outsider", 0);
        }
    }
}
