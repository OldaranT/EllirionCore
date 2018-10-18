package com.ellirion.core.database.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import com.ellirion.core.database.model.PlotDBModel;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.List;
import java.util.UUID;

public class PlotDAO extends BasicDAO<PlotDBModel, Datastore> {

    /**
     * Create a new BasicDAO.
     * @param entityClass the class of the POJO you want to persist using this DAO
     * @param ds the Datastore which gives access to the MongoDB instance for this DAO
     */
    public PlotDAO(final Class<PlotDBModel> entityClass, final Datastore ds) {
        super(entityClass, ds);
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
     * @param plotOwnerID The plot owner UUID.
     * @return Return the result of the operation.
     */
    public boolean createPlot(PlotCoord plotCoord, UUID plotOwnerID) {
        return savePlot(new PlotDBModel(plotCoord, plotOwnerID));
    }

    /**
     * Get a specific plot from the DB.
     * @param coord The plotCoord of the plot to fetch.
     * @return return the found plot.
     */
    public PlotDBModel getSpecificPlot(PlotCoord coord) {
        return findOne("_id", coord);
    }

    public List<PlotDBModel> getAllPlots() {
        return find().asList();
    }

    /**
     * Get the plots belonging to a specific plot owner.
     * @param plotOwnerID The UUID of the plotOwner.
     * @return Return the found plots as a list.
     */
    public List<PlotDBModel> getAllPlotsFromPlotOwner(UUID plotOwnerID) {
        Query query = createQuery().filter("plotOwnerID", plotOwnerID);
        return find(query).asList();
    }

    /**
     * Update the plot in the DB.
     * @param plot The plot to update in the DB.
     * @return Return the result of the operation.
     */
    public boolean update(Plot plot) {
        PlotDBModel dbModel = findOne("_id", plot.getPlotCoord());
        dbModel.update(plot);
        return savePlot(dbModel);
    }
}
