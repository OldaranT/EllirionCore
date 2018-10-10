package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.ellirion.core.playerdata.PlayerManager;

public class OnPlayerQuit implements Listener {

    /**
     * @param e The quit event.
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage("bye bye " + player.getName());
        player.getServer().broadcastMessage(player.getName() + " logged out");
        PlayerManager.updatePlayer(player);
    }
}
