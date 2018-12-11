package com.ellirion.core.plotsystem.model;

import lombok.Getter;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class PlotOwner {

    /**
     * This is the database manager that is used by the update method.
     */
    protected static final DatabaseManager DATABASE_MANAGER = EllirionCore.getINSTANCE().getDbManager();
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
     * This constructor exists to turn database objects back into working Plot Owners.
     * @param raceUUID The UUID of the race.
     * @param plotCoords The Owned plots.
     */
    public PlotOwner(final UUID raceUUID, final List<PlotCoord> plotCoords) {
        this(raceUUID);
        if (plotCoords == null) {
            this.plotCoords = new HashSet<>();
        } else {
            this.plotCoords.addAll(plotCoords);
        }
    }

    /**
     * Add a plot to a owner.
     * @param plot The plot to add.
     */
    public void addPlot(PlotCoord plot) {
        plotCoords.add(plot);
        updateDatabase();
    }

    /**
     * Remove a plot from a owner.
     * @param plot The plot to remove.
     */
    public void removePlot(PlotCoord plot) {
        plotCoords.remove(plot);
        updateDatabase();
    }

    /**
     * Get the name of the plot owner.
     * @return the name of the plot owner.
     */
    public abstract String getName();

    /**
     * This method is used to update the database when something changes.
     */
    protected void updateDatabase() {
        // empty on purpose;
    }
}
