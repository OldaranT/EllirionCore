package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlotDBModel;
import com.ellirion.core.database.util.GenericTryCatch;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.List;
import java.util.UUID;

public class PlotDAO extends BasicDAO<PlotDBModel, Datastore> {

    private String id = "_id";
    private String gameIDColumn = "gameID";

    /**
     * Create a new PlotDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public PlotDAO(final Class<PlotDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    //    @SuppressWarnings({"Duplicates", "CPD-START"})
    //TODO validate generic try and catch method with chris.
    private boolean savePlot(PlotDBModel plot) {
        return GenericTryCatch.tryCatch(() -> save(plot));
        //        try {
        //            save(plot);
        //            return true;
        //        } catch (Exception exception) {
        //            Logging.printStackTrace(exception);
        //            return false;
        //        }
    }

    /**
     * This saves a plot from raw data and not a plot object to the database.
     * @param plotCoord The plot coords class.
     * @return Return the result of the operation.
     */
    public boolean savePlot(PlotCoord plotCoord) {
        return savePlot(new PlotDBModel(plotCoord));
    }

    /**
     * Get a specific plot from the DB.
     * @param plotCoord The plotCoord of the plot to fetch.
     * @return return the found plot.
     */
    public PlotDBModel getSpecificPlot(PlotCoord plotCoord) {
        return findOne(id, plotCoord);
    }

    public List<PlotDBModel> getAllPlots() {
        return find().asList();
    }

    /**
     * Get all plots by gameID.
     * @param gameID the gameID of the plots to fetch.
     * @return return the found plots.
     */
    public List<PlotDBModel> getPlotsByGameID(UUID gameID) {
        return createQuery().filter(id + "." + gameIDColumn, gameID).asList();
    }
}
