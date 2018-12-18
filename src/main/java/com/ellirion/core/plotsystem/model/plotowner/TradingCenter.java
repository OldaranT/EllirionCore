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

    /**
     * This method is used to update the database when something changes.
     */
    @Override
    protected boolean updateDatabase() {
        if (super.updateDatabase()) {
            return PlotOwner.DATABASE_MANAGER.updateTradingCenter(this);
        }
        return true; // return true because there was no error. The game just hasn't been started.
    }
}
