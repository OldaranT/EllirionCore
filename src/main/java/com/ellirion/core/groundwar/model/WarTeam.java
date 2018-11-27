package com.ellirion.core.groundwar.model;

import lombok.Getter;
import com.ellirion.core.EllirionCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarTeam {
    @Getter private List<UUID> players;

    /**
     * constructor.
     */
    public WarTeam() {
        players = new ArrayList<>();
    }

    /**
     * Add a player to this WarTeam.
     * @param player the player to add
     */
    public void addPlayer(UUID player) {
        players.add(player);
    }

    /**
     * Get a list of all participants' names.
     * @return all the participants' names as a string
     */
    public String getParticipants() {
        if (players.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (UUID player : players) {
            builder.append('-').append(EllirionCore.getINSTANCE().getServer().getPlayer(player).getDisplayName()).append('\n');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
