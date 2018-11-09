package com.ellirion.core.database.model;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Indexed;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.model.Point;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.UUID;

@Entity(value = "plot", noClassnameStored = true)
public class PlotDBModel {

    @Getter private String name;
    @Id @Indexed @Embedded @Getter private PlotCoord plotCoord;
    @Getter private int plotSize;
    @Getter private Point lowestCorner;
    @Getter private Point highestCorner;
    @Getter private UUID worldUUID;
    @Getter private String worldName;

    //    @Getter private String name;

    @Getter
    private UUID plotOwnerID;

    /**
     * This class is the database object for the plots.
     * @param name name of the plot.
     * @param plotCoord The plotCoordinates according to the custom grid.
     * @param plotSize Size of the plot when it was created.
     * @param lowestCorner Lowest point of the plot.
     * @param highestCorner Highest point of the plot.
     * @param worldUUID The id of the world the plot is saved in.
     * @param worldName The name of the world the plot is saved in.
     * @param plotOwnerID The UUID of the plot owner.
     */
    public PlotDBModel(final String name, final PlotCoord plotCoord, final int plotSize, final Point lowestCorner,
                       final Point highestCorner, final UUID worldUUID, final String worldName,
                       final UUID plotOwnerID) {
        this.name = name;
        this.plotCoord = plotCoord;
        this.plotSize = plotSize;
        this.lowestCorner = lowestCorner;
        this.highestCorner = highestCorner;
        this.worldUUID = worldUUID;
        this.worldName = worldName;
        this.plotOwnerID = plotOwnerID;
    }

    /**
     * The overloaded constructor.
     * @param plot The plot to be saved.
     */
    public PlotDBModel(final Plot plot) {
        name = plot.getName();
        plotCoord = plot.getPlotCoord();
        plotSize = plot.getPlotSize();
        lowestCorner = plot.getLowestCorner();
        highestCorner = plot.getHighestCorner();
        worldUUID = plot.getWorldUUID();
        worldName = plot.getWorld().getName();
        plotOwnerID = plot.getOwner().getRaceUUID();
    }

    /**
     * Default constructor used by morphia.
     */
    public PlotDBModel() {
        // this is intentionally empty.
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

    /**
     * convert a plotDBmodel to a plot.
     * @return return a plot from plotDBmodel.
     */
    public Plot convertPlotFromDatabase() {
        World worldToCheck = EllirionCore.getINSTANCE().getServer().getWorld(worldUUID);

        //Check if the world is loaded if not create a dummy and then load it.
        if (worldToCheck == null) {
            World world = new WorldCreator(worldName).createWorld();
            EllirionCore.getINSTANCE().getServer().getWorlds().add(world);
        }

        return new Plot(name, plotCoord, lowestCorner, highestCorner, plotSize,
                        EllirionCore.getINSTANCE().getServer().getWorld(worldUUID), worldUUID);
    }
}
