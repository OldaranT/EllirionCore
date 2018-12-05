package com.ellirion.core.database.model;

import lombok.Getter;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Field;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Index;
import xyz.morphia.annotations.IndexOptions;
import xyz.morphia.annotations.Indexed;
import xyz.morphia.annotations.Indexes;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.UUID;

@Entity(value = "PlotCoord", noClassnameStored = true)
@Indexes({
        @Index(fields = {
                @Field(value = "gameID"),
                @Field(value = "plotCoord")
        }, options = @IndexOptions(unique = true)
        )
})
public class PlotCoordDBModel {

    @Id private final UUID databaseID = UUID.randomUUID();
    @Indexed @Getter private UUID gameID;
    @Indexed @Embedded @Getter private PlotCoord plotCoord;

    /**
     * Default constructor used by morphia.
     */
    public PlotCoordDBModel() {
        // this is intentionally empty.
    }

    /**
     * This class is the database object for the plots.
     * @param gameID The id of the game.
     * @param plotCoord The plotCoordinates according to the custom grid.
     */
    public PlotCoordDBModel(final UUID gameID, final PlotCoord plotCoord) {
        this.gameID = gameID;
        this.plotCoord = plotCoord;
    }
}
