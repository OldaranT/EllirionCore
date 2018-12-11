package com.ellirion.core.groundwar.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.ellirion.core.EllirionCore;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.model.Plot;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    /**
     * Listen for player deaths for GroundWars.
     * @param e the playerDeathEvent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        UUID playerID = player.getUniqueId();
        GroundWar groundWar = GroundWarManager.getGroundWar(playerID);

        if (groundWar == null || groundWar.getState() != GroundWar.State.IN_PROGRESS) {
            return;
        }

        groundWar.playerDied(playerID);
    }

    /**
     * Listen for player respawn events for GroundWars.
     * @param e the player respawn event
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        Player player = e.getPlayer();
        UUID playerID = player.getUniqueId();
        GroundWar groundWar = GroundWarManager.getGroundWar(playerID);

        if (groundWar == null || groundWar.getState() != GroundWar.State.IN_PROGRESS) {
            return;
        }

        //make it so the player respawns in the desired plot
        if (PlayerManager.getPlayerRace(playerID).equals(groundWar.getRaceA())) {
            Plot plot = groundWar.getPlotA();
            World w = EllirionCore.getINSTANCE().getServer().getWorld(plot.getPlotCoord().getWorldName());
            e.setRespawnLocation(plot.getCenterLocation(w, player.getLocation().getYaw(), player.getLocation().getPitch()));
        } else {
            Plot plot = groundWar.getPlotB();
            World w = EllirionCore.getINSTANCE().getServer().getWorld(plot.getPlotCoord().getWorldName());
            e.setRespawnLocation(plot.getCenterLocation(w, player.getLocation().getYaw(), player.getLocation().getPitch()));
        }
    }
}
