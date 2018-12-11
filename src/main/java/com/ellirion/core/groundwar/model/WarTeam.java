package com.ellirion.core.groundwar.model;

import lombok.Getter;
import org.bukkit.Location;
import com.ellirion.core.EllirionCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarTeam {
    @Getter private List<UUID> players;
    @Getter private List<Participant> participants;
    @Getter private int lives;

    /**
     * constructor.
     */
    public WarTeam() {
        players = new ArrayList<>();
        participants = new ArrayList<>();
        lives = -1;
    }

    /**
     * Add a player to this WarTeam.
     * @param player the player to add
     */
    public void addPlayer(UUID player) {
        players.add(player);
        Location loc = EllirionCore.getINSTANCE().getServer().getPlayer(player).getLocation();
        participants.add(new Participant(player, loc));
    }

    /**
     * Get a list of all participants' names.
     * @return all the participants' names as a string
     */
    public String getPlayerNames() {
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

    /**
     * Set the initial lives of a team. (note: this only works once)
     * @param lives the amount of lives the team has
     */
    public void setInitialLives(int lives) {
        if (this.lives == -1 && lives > 0) {
            this.lives = lives;
        }
    }

    /**
     * Remove a life from the total.
     */
    public void removeLife() {
        lives--;
    }

    /**
     * Make a copy of this WarTeam.
     * @return a copy of this WarTeam
     */
    public WarTeam copy() {
        WarTeam other = new WarTeam();
        for (UUID id : players) {
            other.players.add(id);
        }

        other.lives = lives;

        return other;
    }
}
