package com.ellirion.core.groundwar.model;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;

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
        ChatColor raceColor = PlayerManager.getPlayerRace(players.get(0)).getTeamColor();
        for (UUID player : players) {
            builder.append('-').append(raceColor).append(EllirionCore.getINSTANCE().getServer().getPlayer(player).getDisplayName()).append(ChatColor.RESET).append('\n');
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
}
