package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import xyz.morphia.query.Query;
import xyz.morphia.query.UpdateOperations;
import com.ellirion.core.database.model.TradingCenterDBModel;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;

import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatchUtils.tryCatch;

public class TradingCenterDAO extends BasicDAO<TradingCenterDBModel, Datastore> {

    private static final String id = "_id";
    private static final String gameIDColumn = "gameID";

    /**
     * This creates a new TradingCenterDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public TradingCenterDAO(final Class<TradingCenterDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    private boolean saveTradingCenter(TradingCenterDBModel tradingCenterDBModel) {
        return tryCatch(() -> save(tradingCenterDBModel));
    }

    /**
     * This saves the trading center to the database.
     * @param tradingCenter The trading center to save.
     * @param gameID The ID of the game where this trading center belongs.
     * @return return a boolean indicating success or failure.
     */
    public boolean createTradingCenter(TradingCenter tradingCenter, UUID gameID) {
        TradingCenterDBModel model = new TradingCenterDBModel(tradingCenter, gameID);
        return saveTradingCenter(model);
    }

    /**
     * Gets a list of all trading center DB models.
     * @return returns the list.
     */
    public List<TradingCenterDBModel> getTradingCenters() {
        return find().asList();
    }

    /**
     * This method gets the trading center from a specific game.
     * @param gameID The ID of the game.
     * @return Return the found tradingCenterDBModel
     */
    public TradingCenterDBModel getTradingCenter(UUID gameID) {
        Query query = createQuery().filter(gameIDColumn, gameID);
        return findOne(query);
    }

    /**
     * This updates the tradingCenter in the database.
     * @param tradingCenter The tradingCenter to update.
     * @return return the result of the operation.
     */
    public boolean updateTradingCenter(TradingCenter tradingCenter) {
        Query query = createQuery().filter(id, tradingCenter.getRaceUUID());
        UpdateOperations ops = createUpdateOperations().set("ownedPlots", tradingCenter.getPlotCoords());
        return tryCatch(() -> updateFirst(query, ops));
    }
}
