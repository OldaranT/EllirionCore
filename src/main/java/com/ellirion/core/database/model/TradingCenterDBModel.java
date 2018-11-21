package com.ellirion.core.database.model;

import lombok.Getter;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Indexed;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;
import com.ellirion.core.util.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(value = "TradingCenter", noClassnameStored = true)
public class TradingCenterDBModel {

    @Id
    @Indexed
    @Getter private UUID tradingCenterID;

    @Indexed
    @Getter private UUID gameID;

    @Embedded
    @Getter private List<PlotCoord> ownedPlots = new ArrayList<>();

    /**
     * This is a default constructor used by morphia.
     */
    public TradingCenterDBModel() {
        // Empty on purpose
    }

    /**
     * This creates a trading center database model.
     * @param tradingCenterID The UUID of the trading center.
     * @param gameID The id of the Game.
     */
    public TradingCenterDBModel(final UUID tradingCenterID, final UUID gameID) {
        this.tradingCenterID = tradingCenterID;
        this.gameID = gameID;
    }

    /**
     * This creates a trading center database model.
     * @param tradingCenterID The UUID of the trading center.
     * @param ownedPlots The List of Plot coords that the trading center owns.
     * @param gameID The id of the Game.
     */
    public TradingCenterDBModel(final UUID tradingCenterID, final List<PlotCoord> ownedPlots, final UUID gameID) {
        this(tradingCenterID, gameID);
        this.ownedPlots.addAll(ownedPlots);
    }

    /**
     * This creates a trading center database model.
     * @param tradingCenter The trading center to convert to a database model.
     * @param gameID The id of the Game.
     */
    public TradingCenterDBModel(final TradingCenter tradingCenter, final UUID gameID) {
        tradingCenterID = tradingCenter.getRaceUUID();
        ownedPlots.addAll(tradingCenter.getPlotCoords());
        this.gameID = gameID;
    }

    /**
     * Remove a plot from the owned plots list.
     * @param coord The coord of the to remove plot.
     * @return return a boolean that indicates success or failure.
     */
    public boolean removePlot(PlotCoord coord) {
        if (!ownedPlots.contains(coord)) {
            return false;
        }
        return ownedPlots.remove(coord);
    }

    /**
     * This adds a single plot coord to the owned plots list.
     * @param coord The coord of the plot to add.
     * @return return the result of the operation.
     */
    public boolean addPlot(PlotCoord coord) {
        if (ownedPlots.contains(coord)) {
            return false;
        }
        return ownedPlots.add(coord);
    }

    /**
     * This updates this model with the new data.
     * @param tradingCenter The trading center to get the data from.
     * @return return a boolean that indicates success or failure.
     */
    public boolean update(TradingCenter tradingCenter) {
        try {
            List<PlotCoord> newPlotCoords = new ArrayList<>(tradingCenter.getPlotCoords());
            if (ownedPlots.size() == newPlotCoords.size() && ownedPlots.containsAll(newPlotCoords)) {
                return true;
            }
            ownedPlots = newPlotCoords;
            return true;
        } catch (Exception exception) {
            Logging.printStackTrace(exception);
            return false;
        }
    }
}
