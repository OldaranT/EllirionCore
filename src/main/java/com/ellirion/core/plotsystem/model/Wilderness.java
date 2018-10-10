package com.ellirion.core.plotsystem.model;

public class Wilderness extends PlotOwner {

    private static Wilderness INSTANCE = new Wilderness();

    private Wilderness() {
    }

    public static Wilderness getInstance() {
        return INSTANCE;
    }

    public String getName() {
        return "Wilderness";
    }
}
