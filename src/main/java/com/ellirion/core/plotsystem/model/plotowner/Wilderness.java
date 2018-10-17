package com.ellirion.core.plotsystem.model.plotowner;

import com.ellirion.core.plotsystem.model.PlotOwner;

public class Wilderness extends PlotOwner {

    private static Wilderness INSTANCE = new Wilderness();

    private Wilderness() {
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
