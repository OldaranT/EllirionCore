package com.ellirion.core.groundwar.model;

import lombok.Getter;
import org.bukkit.Location;

import com.ellirion.core.EllirionCore;

import java.util.UUID;

public class Participant {
    @Getter private UUID player;
    @Getter private Location respawnLocationAfterGroundWar;

    /**
     * Create a participant.
     * @param player the player this participant represents
     * @param location the location of the participant
     */
    public Participant(final UUID player, final Location location) {
        this.player = player;
        respawnLocationAfterGroundWar = location;
    }

    /**
     * ToString.
     * @return string.
     */
    public String toString() {
        String res = "Participant ";
        res += EllirionCore.getINSTANCE().getServer().getPlayer(player).getDisplayName();
        res += "\nRespawn location: " + respawnLocationAfterGroundWar.toString();

        return res;
    }
}
