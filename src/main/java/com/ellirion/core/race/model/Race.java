package com.ellirion.core.race.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.PlotOwner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class Race extends PlotOwner {

    @Getter @Setter private String name;
    @Getter private Set<UUID> players;
    @Getter @Setter private ChatColor teamColor;
    @Getter private Plot homePlot;

    /**
     * This is the race that players can join.
     * @param name The name of the race.
     * @param teamColor The team color.
     * @param homePlot The homeplot of the race.
     */
    public Race(final String name, final ChatColor teamColor, final Plot homePlot) {
        // This calls the super with null to get a random UUID assigned.
        super(null);
        this.name = name;
        this.homePlot = homePlot;
        this.teamColor = teamColor;
        players = new HashSet<>();
    }

    /**
     * This is an unfinished constructor because i haven't saved the plots in the database yet.
     * @param raceDBModel The database stored race.
     */
    public Race(final RaceDBModel raceDBModel) {
        super(raceDBModel.getRaceID(), raceDBModel.getOwnedPlots());
        name = raceDBModel.getRaceName();
        players = raceDBModel.getPlayers();
        teamColor = ChatColor.valueOf(raceDBModel.getColor());
        PlotCoord homePlotCoord = raceDBModel.getHomePlotCoord();
        Logger.getGlobal().info("Race " + name + " loading home plot " + homePlotCoord.getX() + ", " + homePlotCoord.getZ() + ", " + homePlotCoord.getGameID() + ", " + homePlotCoord.getWorldName());
        Plot homePlot = PlotManager.getPlotByCoordinate(homePlotCoord);
        Logger.getGlobal().info("Race " + name + " loaded plot " + homePlot.getName() + " as home plot");
        this.homePlot = homePlot;
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
