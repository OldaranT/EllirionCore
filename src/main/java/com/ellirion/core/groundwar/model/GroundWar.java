package com.ellirion.core.groundwar.model;

import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.model.Race;

import java.util.UUID;

public class GroundWar {
    private Race a;
    private Race b;
    private Plot[] plots;
    private UUID createdBy;

    /**
     * Create a ground war.
     * @param createdBy the player that created the ground war
     * @param a the race of the player that created the ground war
     */
    public GroundWar(final UUID createdBy, final Race a) {
        this.createdBy = createdBy;
        this.a = a;

        plots = new Plot[2];
    }

    /**
     * Set race a's plot in this groundwar.
     * @param plot race a's plot.
     */
    public void setPlotA(Plot plot) {
        plots[0] = plot;
    }

    /**
     * Set race b's plot in the groundwar.
     * @param plot race b's plot
     */
    public void setPlotB(Plot plot) {
        plots[1] = plot;
    }

    /**
     * Set the opponent Race.
     * @param race the opponent race
     */
    public void setRaceB(Race race) {
        b = race;
    }

    /**
     * Get race a's plot.
     * @return race a's plot
     */
    public Plot getPlotA() {
        return plots[0];
    }

    /**
     * ToString.
     * @return string
     */
    public String toString() {
        return a.toString() + b.toString() + createdBy.toString();
    }
}
