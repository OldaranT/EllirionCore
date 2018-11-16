package com.ellirion.core.database.model;

import lombok.Getter;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Indexed;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.PlotOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(value = "plotOwner", noClassnameStored = true)
public class PlotOwnerDBModel {

    @Id
    @Indexed
    @Getter private UUID plotOwnerID;

    @Embedded
    @Getter private List<PlotCoord> plotCoords;

    /**
     * Empty constructor used by morphia.
     */
    public PlotOwnerDBModel() {
        // empty on on purpose.
    }

    /**
     * This is the database version of the Plot owner.
     * @param plotOwner The plot owner to be saved to the database.
     */
    public PlotOwnerDBModel(final PlotOwner plotOwner) {
        plotOwnerID = plotOwner.getRaceUUID();
        plotCoords = new ArrayList<>();
        plotCoords.addAll(plotOwner.getPlotCoords());
    }

    /**
     * This is the database version of the Plot owner.
     * @param plotOwnerID The UUID of the plot owner.
     * @param plotCoords The plot coords of the owned plots.
     */
    public PlotOwnerDBModel(final UUID plotOwnerID, final List<PlotCoord> plotCoords) {
        this.plotOwnerID = plotOwnerID;
        this.plotCoords = plotCoords;
    }

    /**
     * This method updates the plot owner with the relevant data.
     * @param plotOwner The owner to update with.
     * @return Return true if the operation was a success.
     */
    public boolean update(PlotOwner plotOwner) {
        plotCoords = new ArrayList<>();
        plotCoords.addAll(plotOwner.getPlotCoords());
        return true;
    }

    /**
     * This method removes a specific plot coord from the plot coords set.
     * @param plotCoord The plot coord to remove.
     * @return Return the outcome of the operation.
     */
    public boolean removePlotCoord(PlotCoord plotCoord) {
        if (plotCoords == null) {
            return false;
        }
        return plotCoords.remove(plotCoord);
    }

    /**
     * This method adds a new plot coord to the set.
     * @param plotCoord The plot coord to add.
     * @return Return the outcome of the operation.
     */
    public boolean addPlotCoord(PlotCoord plotCoord) {
        if (plotCoords == null) {
            plotCoords = new ArrayList<>();
        }
        return plotCoords.add(plotCoord);
    }

    /**
     * Set the plot coords.
     * @param plotCoords The plot coords to set.
     */
    public void setPlotCoords(List<PlotCoord> plotCoords) {
        //        this.plotCoords = new ArrayList<>();
        this.plotCoords = plotCoords;
    }
}
