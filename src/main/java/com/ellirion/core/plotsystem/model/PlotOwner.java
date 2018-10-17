package com.ellirion.core.plotsystem.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class PlotOwner {

    @Getter private final UUID raceUUID;
    @Getter private List<Plot> plots = new ArrayList<>();

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
    public void addPlot(Plot plot) {
        plots.add(plot);
    }

    /**
     * Remove a plot from a owner.
     * @param plot The plot to remove.
     */
    public void removePlot(Plot plot) {
        plots.remove(plot);
    }

    /**
     * Get the name of the plot owner.
     * @return the name of the plot owner.
     */
    public abstract String getName();
}
