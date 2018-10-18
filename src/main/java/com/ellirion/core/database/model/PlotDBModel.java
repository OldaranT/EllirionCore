package com.ellirion.core.database.model;

import lombok.Getter;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.UUID;

@Entity(value = "plot", noClassnameStored = true)
public class PlotDBModel {

    @Id
    @Indexed
    @Embedded
    @Getter private PlotCoord plotCoord;

    //    @Getter private String name;

    @Getter private UUID plotOwnerID;

    /**
     * The constructor.
     * @param plotCoord The plotCoordinates according to the custom grid.
     * @param plotOwnerID The UUID of the plot owner.
     */
    public PlotDBModel(final PlotCoord plotCoord, final UUID plotOwnerID) {
        this.plotCoord = plotCoord;
        this.plotOwnerID = plotOwnerID;
    }

    /**
     * The overloaded constructor.
     * @param plot The plot to be saved.
     */
    public PlotDBModel(final Plot plot) {
        plotCoord = plot.getPlotCoord();
        plotOwnerID = plot.getOwner().getRaceUUID();
    }

    /**
     * Default constructor for morphia.
     */
    public PlotDBModel() {
        // default constructor for morphia.
    }

    /**
     * Update the database plot.
     * @param plot The plot to get the update data from.
     * @return Return true to signal it was successful.
     */
    public boolean update(Plot plot) {
        plotOwnerID = plot.getOwner().getRaceUUID();
        return true;
    }
}
