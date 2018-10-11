package com.ellirion.core.races.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotOwner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Race extends PlotOwner {

    //    @Getter private final UUID raceUUID;
    @Getter @Setter private String name;
    @Getter private Set<UUID> players;
    @Getter @Setter private ChatColor teamColor;
    @Getter private Plot homePlot;

    /**
     * @param name The name of the race.
     * @param teamColor The team color.
     * @param homePlot The homeplot of the race.
     */
    public Race(final String name, final ChatColor teamColor, final Plot homePlot) {
        this.name = name;
        this.homePlot = homePlot;
        players = new HashSet<>();
        this.teamColor = teamColor;
    }

    public String getNameWithColor() {
        return teamColor + name + ChatColor.RESET;
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
