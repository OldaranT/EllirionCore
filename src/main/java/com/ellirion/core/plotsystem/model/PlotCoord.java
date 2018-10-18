package com.ellirion.core.plotsystem.model;

import lombok.Getter;
import org.mongodb.morphia.annotations.Embedded;

import java.util.Objects;

@Embedded
public class PlotCoord {

    @Getter private int x;
    @Getter private int z;

    /**
     * Plot coordinate to locate a plot.
     * @param x x of the coordinate
     * @param z z of the coordinate
     */
    public PlotCoord(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Default constructor for morphia.
     */
    public PlotCoord() {
        // default constructor for morphia.
    }

    /**
     * Translates a x and a z to the current coordinate.
     * @param x x to add to current coordinate.
     * @param z z to add to current coordinate.
     * @return returns translated coordinate.
     */
    public PlotCoord translate(int x, int z) {
        return new PlotCoord(this.x + x, this.z + z);
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
}
