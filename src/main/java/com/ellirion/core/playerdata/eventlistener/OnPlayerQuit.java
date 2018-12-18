package com.ellirion.core.playerdata.eventlistener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.playerdata.PlayerManager;

public class OnPlayerQuit implements Listener {

    /**
     * @param event The quit event.
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("bye bye " + player.getName());
        player.getServer().broadcastMessage(player.getName() + " logged out");
        PlayerManager.setPlayerOffline(player.getUniqueId());
        EllirionCore.getINSTANCE().getLogger().info(EllirionCore.getINSTANCE().getDbManager().getPlayerFromGame(
                GameManager.getInstance().getGameID(), player.getUniqueId()).toString());
    }
}
