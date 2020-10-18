package com.ellirion.core.plotsystem.model;

import lombok.Getter;
import xyz.morphia.annotations.Embedded;

import java.util.Objects;

@Embedded
public class PlotCoord {

    @Getter private int x;
    @Getter private int z;
    @Getter private String worldName;

    /**
     * Plot coordinate to locate a plot.
     * @param x x of the coordinate.
     * @param z z of the coordinate.
     * @param worldName The name of the world where this plot coord belongs.
     */
    public PlotCoord(final int x, final int z, final String worldName) {
        this.x = x;
        this.z = z;
        this.worldName = worldName;
    }

    /**
     * Default constructor used by morphia.
     */
    public PlotCoord() {
        // empty on purpose.
    }

    /**
     * Translates an x and an z to the current coordinate.
     * @param x x to add to current coordinate.
     * @param z z to add to current coordinate.
     * @return returns translated coordinate.
     */
    public PlotCoord translate(int x, int z) {
        return new PlotCoord(this.x + x, this.z + z, worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, worldName);
    }

    //WARNING: ?, UselessParentheses
    @SuppressWarnings("PMD")
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlotCoord)) {
            return false;
        }

        PlotCoord other = (PlotCoord) obj;

        return (x == other.x && z == other.z && worldName.equals(other.worldName));
    }

    @Override
    public String toString() {
        return Integer.toString(x) + " , " + Integer.toString(z);
    }

    /**
     * Subtract two plotcoords from each other (used for getting the direction from one plot to another).
     * @param other the other plot
     * @return the difference between this plotcoord and the other plot coord
     */
    public PlotCoord subtract(PlotCoord other) {
        return new PlotCoord(x - other.x, z - other.z, worldName);
    }
}
