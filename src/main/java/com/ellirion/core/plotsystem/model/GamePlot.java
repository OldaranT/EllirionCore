package com.ellirion.core.plotsystem.model;

public class GamePlot extends PlotOwner {

    private static GamePlot INSTANCE = new GamePlot();

    private GamePlot() {
    }

    public static GamePlot getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "GamePlot";
    }
}
