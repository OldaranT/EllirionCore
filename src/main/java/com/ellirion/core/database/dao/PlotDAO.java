package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlotDBModel;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.List;

public class PlotDAO extends BasicDAO<PlotDBModel, Datastore> {

    private String id = "_id";

    /**
     * Create a new PlotDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public PlotDAO(final Class<PlotDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    private boolean savePlot(PlotDBModel plot) {
        save(plot);
        return true;
    }

    /**
     * This creates a new plot in the database.
     * @param plot The plot to be saved.
     * @return Return the result of the operation.
     */
    public boolean createPlot(Plot plot) {
        return savePlot(new PlotDBModel(plot));
    }

    /**
     * This creates a plot from raw data and not a plot object.
     * @param plotCoord The plot coords class.
     * @return Return the result of the operation.
     */
    public boolean createPlot(PlotCoord plotCoord) {
        return savePlot(new PlotDBModel(plotCoord));
    }

    /**
     * Get a specific plot from the DB.
     * @param coord The plotCoord of the plot to fetch.
     * @return return the found plot.
     */
    public PlotDBModel getSpecificPlot(PlotCoord coord) {
        return findOne(id, coord);
    }

    public List<PlotDBModel> getAllPlots() {
        return find().asList();
    }
}
