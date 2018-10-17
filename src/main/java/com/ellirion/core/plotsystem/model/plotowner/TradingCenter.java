package com.ellirion.core.plotsystem.model.plotowner;

import com.ellirion.core.plotsystem.model.PlotOwner;

public class TradingCenter extends PlotOwner {

    private static TradingCenter INSTANCE = new TradingCenter();

    private TradingCenter() {
        super(null);
    }

    public static TradingCenter getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "TradingCenter";
    }
}
