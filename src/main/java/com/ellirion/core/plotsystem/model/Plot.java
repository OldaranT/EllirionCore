package com.ellirion.core.plotsystem.model;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import com.ellirion.core.model.Point;

import java.util.UUID;

public class Plot {

    //@Getter private UUID id;
    @Getter private String name;
    @Getter private int plotSize;
    @Getter private Point lowestCorner;
    @Getter private Point highestCorner;
    @Getter private World world;
    @Getter private UUID worldUUID;
    @Getter private PlotOwner owner;

    /**
     * Model that defines a piece of land in the map.
     * @param name name of the plot.
     * @param lowestCorner Lowest corner of the cubic form of the plot.
     * @param highestCorner Highest corner of the cubic form of the plot.
     * @param plotSize The size of the plot.
     * @param world The world that the plot is located in.
     * @param worldUUID The UUID of the world the plot is located in.
     */
    public Plot(final String name, final Point lowestCorner, final Point highestCorner, final int plotSize,
                final World world, final UUID worldUUID) {
        this.name = name;
        this.lowestCorner = lowestCorner;
        this.highestCorner = highestCorner;
        this.plotSize = plotSize;
        this.world = world;
        this.worldUUID = worldUUID;
        owner = Wilderness.getInstance();

    }

    /**
     * Set the owner of the plot.
     * @param owner The new owner of the plot.
     */
    public void setOwner(PlotOwner owner) {
        this.owner = owner;
        owner.addPlot(this);
    }

    /**
     * Get highest center location of a plot.
     * @param yaw yaw of the player to teleport.
     * @param pitch pitch of the player to teleport.
     * @return Location of the teleport location.
     */
    public Location getCenterLocation(float yaw, float pitch) {

        double centerX = highestCorner.getX() - ((highestCorner.getX() - lowestCorner.getX()) / 2);
        double centerZ = highestCorner.getZ() - ((highestCorner.getZ() - lowestCorner.getZ()) / 2);
        double centerY = world.getHighestBlockYAt((int) centerX, (int) centerZ);

        return new Location(world, centerX, centerY, centerZ, yaw, pitch);
    }
}

