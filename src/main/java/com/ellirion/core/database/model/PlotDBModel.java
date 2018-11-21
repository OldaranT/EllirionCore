package com.ellirion.core.database.model;

import lombok.Getter;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Indexed;
import com.ellirion.core.plotsystem.model.PlotCoord;

@Entity(value = "plot", noClassnameStored = true)
public class PlotDBModel {

    @Id @Indexed @Embedded @Getter private PlotCoord plotCoord;

    /**
     * Default constructor used by morphia.
     */
    public PlotDBModel() {
        // this is intentionally empty.
    }

    /**
     * This class is the database object for the plots.
     * @param plotCoord The plotCoordinates according to the custom grid.
     */
    public PlotDBModel(final PlotCoord plotCoord) {
        this.plotCoord = plotCoord;
    }
}
