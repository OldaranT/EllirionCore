package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlotCoordDBModel;
import com.ellirion.core.database.util.GenericTryCatch;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.List;
import java.util.UUID;

public class PlotCoordDAO extends BasicDAO<PlotCoordDBModel, Datastore> {

    private String id = "_id";
    private String gameIDColumn = "gameID";

    /**
     * Create a new PlotCoordDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public PlotCoordDAO(final Class<PlotCoordDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    //    @SuppressWarnings({"Duplicates", "CPD-START"})
    //TODO validate generic try and catch method with chris.
    private boolean savePlotCoord(PlotCoordDBModel plot) {
        return GenericTryCatch.tryCatch(() -> save(plot));
    }

    /**
     * This saves a plotcoord from raw data and not a plot object to the database.
     * @param gameID the game id of the plot.
     * @param plotCoord The plot coords class.
     * @return Return the result of the operation.
     */
    public boolean savePlotCoord(UUID gameID, PlotCoord plotCoord) {
        return savePlotCoord(new PlotCoordDBModel(gameID, plotCoord));
    }

    /**
     * Get a specific plotcoord from the DB.
     * @param plotCoord The plotCoord of the plot to fetch.
     * @return return the found plotcoord.
     */
    public PlotCoordDBModel getSpecificPlotCoord(PlotCoord plotCoord) {
        return findOne(id, plotCoord);
    }

    public List<PlotCoordDBModel> getAllPlotCoords() {
        return find().asList();
    }

    /**
     * Get all plotcoords by gameID.
     * @param gameID the gameID of the plots to fetch.
     * @return return the found plotcoords.
     */
    public List<PlotCoordDBModel> getPlotCoordsByGameID(UUID gameID) {
        return createQuery().filter(gameIDColumn, gameID).asList();
    }
}
