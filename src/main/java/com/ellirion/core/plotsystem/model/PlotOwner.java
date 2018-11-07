package com.ellirion.core.plotsystem.model;

import lombok.Getter;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class PlotOwner {

    private static DatabaseManager DATABASE_MANAGER = EllirionCore.getINSTANCE().getDbManager();
    @Getter private final UUID raceUUID;
    @Getter private Set<PlotCoord> plotCoords = new HashSet<>();

    /**
     * This constructor exists to facilitate getting races from the database and using the saved UUID's.
     * @param raceUUID The UUID of a race saved in the DB.
     */
    public PlotOwner(final UUID raceUUID) {
        if (raceUUID == null) {
            this.raceUUID = UUID.randomUUID();
        } else {
            this.raceUUID = raceUUID;
        }
    }

    /**
     * Add a plot to a owner.
     * @param plot The plot to add.
     */
    public void addPlot(PlotCoord plot) {
        plotCoords.add(plot);
    }

    /**
     * Remove a plot from a owner.
     * @param plot The plot to remove.
     */
    public void removePlot(PlotCoord plot) {
        plotCoords.remove(plot);
    }

    /**
     * Get the name of the plot owner.
     * @return the name of the plot owner.
     */
    public abstract String getName();
}
