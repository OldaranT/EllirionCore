package com.ellirion.core.plotsystem.model.plotowner;

import com.ellirion.core.plotsystem.model.PlotOwner;

public class TradingCenter extends PlotOwner {

    private static TradingCenter INSTANCE = new TradingCenter();

    private TradingCenter() {
        // This calls the super with null to get a random UUID assigned.
        super(null);
    }

    public static TradingCenter getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "TradingCenter";
    }

    @Override
    protected boolean updateDatabase() {
        if (canUpdateInDatabase()) {
            return PlotOwner.DATABASE_MANAGER.updateTradingCenter(this);
        }
        return true; // return true because there was no error. The game just hasn't been started.
    }
}
