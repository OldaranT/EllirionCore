package com.ellirion.core.groundwar.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;

import java.util.UUID;

public class PlayerTeleportDuringGroundWarListener implements Listener {

    /**
     * Listen for player teleport events.
     * @param event the player teleport event
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();

        GroundWar groundWar = GroundWarManager.getGroundWar(playerID);
        Location teleportTo = event.getTo();
        Plot plotTo = PlotManager.getPlotFromLocation(teleportTo);
        if (groundWar == null) {
            //If the player isn't in a ground war, make sure they didn't teleport into a ground war

            GroundWar groundWarTo = GroundWarManager.getGroundWar(plotTo);
            if (plotTo != null && groundWarTo != null && groundWarTo.getState() == GroundWar.State.IN_PROGRESS) {
                //If the plot exists and it is involved in a ground war
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED + "You cannot teleport into a ground war you are not involved in");
            }

            return;
        }

        //The player is in a ground war, so make sure they didn't teleport out of the ground war
        if (groundWar .getState() == GroundWar.State.IN_PROGRESS && !groundWar.equals(GroundWarManager.getGroundWar(plotTo))) {
            //If the plot the player teleported to is not in the same ground war the player is in
            event.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot teleport out of a ground war");
            return;
        }
    }
}
