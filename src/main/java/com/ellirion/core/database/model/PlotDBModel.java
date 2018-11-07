package com.ellirion.core.database.model;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.model.Point;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

@Entity(value = "plot", noClassnameStored = true)
public class PlotDBModel {

    @Id @Indexed @Embedded @Getter private PlotCoord plotCoord;

    /**
     * This class is the database object for the plots.
     * @param plotCoord The plotCoordinates according to the custom grid.
     */
    public PlotDBModel(final PlotCoord plotCoord) {
        this.plotCoord = plotCoord;
    }

    /**
     * The overloaded constructor.
     * @param plot The plot to be saved.
     */
    public PlotDBModel(final Plot plot) {
        plotCoord = plot.getPlotCoord();
    }

    /**
     * Default constructor used by morphia.
     */
    public PlotDBModel() {
        // this is intentionally empty.
    }

    /**
     * convert a plotDBmodel to a plot.
     * @return return a plot from plotDBmodel.
     */
    public Plot convertPlotFromDatabase() {
        World worldToCheck = EllirionCore.getINSTANCE().getServer().getWorld(plotCoord.getWorldName());

        //Check if the world is loaded if not create a dummy and then load it.
        if (worldToCheck == null) {
            World world = new WorldCreator(plotCoord.getWorldName()).createWorld();
            EllirionCore.getINSTANCE().getServer().getWorlds().add(world);
        }

        int plotSize = PlotManager.getPLOT_SIZE();
        int minX = plotCoord.getX() * plotSize;
        int minZ = plotCoord.getZ() * plotSize;
        int maxX = minX + plotSize - 1;
        int maxZ = minZ + plotSize - 1;

        Point lowestCorner = new Point(minX, PlotManager.getLOWEST_Y(), minZ);
        Point highestCorner = new Point(maxX, PlotManager.getHIGHEST_Y(), maxZ);
        String name = plotCoord.toString();

        return new Plot(name, plotCoord, lowestCorner, highestCorner, plotSize,
                        EllirionCore.getINSTANCE().getServer().getWorld(plotCoord.getWorldName()));
    }
}
