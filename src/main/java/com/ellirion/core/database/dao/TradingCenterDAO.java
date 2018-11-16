package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.TradingCenterDBModel;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;
import com.ellirion.core.util.Logging;

import java.util.List;
import java.util.UUID;

public class TradingCenterDAO extends BasicDAO<TradingCenterDBModel, Datastore> {

    private final String id = "_id";

    /**
     * This creates a new TradingCenterDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public TradingCenterDAO(final Class<TradingCenterDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    @SuppressWarnings({"Duplicates", "CPD-START"})
    private boolean saveTradingCenter(TradingCenterDBModel tradingCenterDBModel) {
        try {
            save(tradingCenterDBModel);
            return true;
        } catch (Exception exception) {
            Logging.printStackTrace(exception);
            return false;
        }
    }

    /**
     * This saves the trading center to the database.
     * @param tradingCenter The trading center to save.
     * @param gameID The ID of the game where this trading center belongs.
     * @return return a boolean indicating success or failure.
     */
    public boolean saveTradingCenter(TradingCenter tradingCenter, int gameID) {
        TradingCenterDBModel model = new TradingCenterDBModel(tradingCenter, gameID);
        return saveTradingCenter(model);
    }

    /**
     * Get a specific trading center from the database.
     * @param tradingCenterID The UUID of the trading center.
     * @return return the found tradingCenterDBModel.
     */
    public TradingCenterDBModel getSpecificTradingCenter(UUID tradingCenterID) {
        return findOne(id, tradingCenterID);
    }

    public List<TradingCenterDBModel> getAllTradingCenters() {
        return find().asList();
    }
}
