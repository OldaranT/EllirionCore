package com.ellirion.core.races.model;

import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Race {

    private String raceName;
    private Set<UUID> players;
    private ChatColor teamColor;

    /**
     * @param raceName The name of the race.
     * @param teamColor The team color.
     */
    public Race(final String raceName, final ChatColor teamColor) {
        this.raceName = raceName;
        players = new HashSet<>();
        this.teamColor = teamColor;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public ChatColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(ChatColor teamColor) {
        this.teamColor = teamColor;
    }

    /**
     * @param playerID The player UUID.
     * @return return true if successful.
     */
    public boolean addPlayer(UUID playerID) {
        return players.add(playerID);
    }

    /**
     * @param playerID The ID to check.
     * @return Return true if the ID is found.
     */
    public boolean hasPlayer(UUID playerID) {
        return players.contains(playerID);
    }

    /**
     * @param playerID The ID of the player to remove.
     * @return Return true if successful.
     */
    public boolean removePlayer(UUID playerID) {
        return players.remove(playerID);
    }
}
