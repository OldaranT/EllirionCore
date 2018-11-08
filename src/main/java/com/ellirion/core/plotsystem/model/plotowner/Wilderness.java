package com.ellirion.core.plotsystem.model.plotowner;

import com.ellirion.core.plotsystem.model.PlotOwner;

public class Wilderness extends PlotOwner {

    private static Wilderness INSTANCE = new Wilderness();

    private Wilderness() {
        // This calls the super with null to get a random UUID assigned.
        super(null);
    }

    public static Wilderness getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "Wilderness";
    }
}
