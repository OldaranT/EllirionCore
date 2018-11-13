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
        return Objects.hash(x, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlotCoord)) {
            return false;
        }

        PlotCoord other = (PlotCoord) obj;

        return (x == other.x && z == other.z);
    }

    @Override
    public String toString() {
        return Integer.toString(x) + " , " + Integer.toString(z);
    }
}
