package com.ellirion.core.groundwar.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.groundwar.model.Participant;
import com.ellirion.core.groundwar.model.WarTeam;
import com.ellirion.core.playerdata.PlayerManager;

import java.util.List;
import java.util.UUID;

public class PlayerLeaveDuringGroundWarListener implements Listener {

    /**
     * Listen for player quit events.
     * @param event the player quit event
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        //If players leave during a ground war, count it as a death, and remove them from the participants list.
        //When a player logs back in, re-add them to the participants list
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();

        GroundWar groundWar = GroundWarManager.getGroundWar(playerID);
        if (groundWar == null) {
            return;
        }

        groundWar.playerDied(playerID);
        WarTeam team = groundWar.getTeam(playerID);
        List<Participant> participants = team.getParticipants();
        Participant toRemove = null;
        for (Participant p : participants) {
            if (p.getPlayer().equals(playerID)) {
                toRemove = p;
                break;
            }
        }
        participants.remove(toRemove);
    }

    /**
     * Listen for player join events.
     * @param event the player join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //If players join during a groundwar, check if they need to be added to a ground war
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();
        GroundWar groundWar = GroundWarManager.getGroundWar(playerID);

        //Player is not in a ground war
        if (groundWar == null) {
            return;
        }

        //Player is in a ground war, add them as participant again
        groundWar.addPlayer(PlayerManager.getPlayerRace(playerID), playerID);
    }
}
