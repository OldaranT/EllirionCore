package com.ellirion.core.plotsystem.model;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.util.model.BoundingBox;

public class Plot {

    @Getter private String name;
    @Getter private PlotCoord plotCoord;
    @Getter private int plotSize;
    @Getter private BoundingBox boundingBox;
    @Getter private PlotOwner owner;

    /**
     * Model that defines a piece of land in the map.
     * @param name name of the plot.
     * @param plotCoord the coordinate of the plot.
     * @param plotSize The size of the plot.
     * @param boundingBox the boundingbox of the plot.
     */
    public Plot(final String name, final PlotCoord plotCoord, final BoundingBox boundingBox, final int plotSize) {
        this.name = name;
        this.plotCoord = plotCoord;
        this.boundingBox = boundingBox;
        this.plotSize = plotSize;
        setOwner(Wilderness.getInstance());
    }

    /**
     * Set the owner of the plot.
     * @param owner The new owner of the plot.
     */
    public void setOwner(PlotOwner owner) {
        if (this.owner != null) {
            this.owner.removePlot(plotCoord);
        }
        this.owner = owner;
        owner.addPlot(plotCoord);
    }

    /**
     * Get highest center location of a plot.
     * @param world the world the plot is in.
     * @param yaw yaw of the player to teleport.
     * @param pitch pitch of the player to teleport.
     * @return Location of the teleport location.
     */
    public Location getCenterLocation(World world, float yaw, float pitch) {

        double centerX = boundingBox.getX2() - ((boundingBox.getX2() - boundingBox.getX1()) / 2);
        double centerZ = boundingBox.getZ2() - ((boundingBox.getZ2() - boundingBox.getZ1()) / 2);
        double centerY = world.getHighestBlockYAt((int) centerX, (int) centerZ);

        return new Location(world, centerX, centerY, centerZ, yaw, pitch);
    }

    /**
     * Gets the neighbouring plots.
     * @return returns a array of neighbouring plots.
     */
    public Plot[] getNeighbours() {

        return new Plot[] {
                PlotManager.getPlotByCoordinate(plotCoord.translate(0, 1)),
                PlotManager.getPlotByCoordinate(plotCoord.translate(1, 0)),
                PlotManager.getPlotByCoordinate(plotCoord.translate(0, -1)),
                PlotManager.getPlotByCoordinate(plotCoord.translate(-1, 0)),
                };
    }
}

