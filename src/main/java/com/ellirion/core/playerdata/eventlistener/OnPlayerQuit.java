package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.ellirion.core.playerdata.PlayerHelper;
import com.ellirion.core.util.PlayerScoreboardHelper;

public class OnPlayerQuit implements Listener {

    /**
     * @param event The quit event.
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("bye bye " + player.getName());
        player.getServer().broadcastMessage(player.getName() + " logged out");

        PlayerScoreboardHelper.removePlayerScoreboard(player.getUniqueId());

        PlayerHelper.setPlayerOffline(player.getUniqueId());
    }
}
