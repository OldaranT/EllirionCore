package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlotOwnerDBModel;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.PlotOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlotOwnerDAO extends BasicDAO<PlotOwnerDBModel, Datastore> {

    private String id = "_id";

    /**
     * Create a new BasicDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public PlotOwnerDAO(final Class<PlotOwnerDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    private boolean savePlotOwner(PlotOwnerDBModel plotOwnerDBModel) {
        save(plotOwnerDBModel);
        return true;
    }

    /**
     * Create a new plot owner in the database.
     * @param plotOwner The plot owner to add to the database.
     * @return Return the result of the operation.
     */
    public boolean createPlotOwner(PlotOwner plotOwner) {
        return savePlotOwner(new PlotOwnerDBModel(plotOwner));
    }

    /**
     * Update the plot owner in the database.
     * @param plotOwner The plot owner to update in the database.
     * @return Return the result of the operation.
     */
    public boolean update(PlotOwner plotOwner) {
        PlotOwnerDBModel plotOwnerDBModel = findOne(id, plotOwner.getRaceUUID());
        plotOwnerDBModel.update(plotOwner);
        return savePlotOwner(plotOwnerDBModel);
    }

    /**
     * Get a specific plot owner from the database.
     * @param plotOwnerID The UUID of the plot owner to fetch.
     * @return Return the found plot owner.
     */
    public PlotOwnerDBModel getSpecificPlotOwner(UUID plotOwnerID) {
        return findOne(id, plotOwnerID);
    }

    public List<PlotOwnerDBModel> getAllPlotOwners() {
        return find().asList();
    }

    /**
     * Add a plot coord to the plot owner.
     * @param plotOwnerID The plot owner to add the coord to.
     * @param plotCoord The plot coord to add.
     * @return Return the result of the operation.
     */
    public boolean addPlotCoord(UUID plotOwnerID, PlotCoord plotCoord) {
        PlotOwnerDBModel plotOwnerDBModel = findOne(id, plotOwnerID);
        plotOwnerDBModel.addPlotCoord(plotCoord);
        return savePlotOwner(plotOwnerDBModel);
    }

    /**
     * Remove a plot coord from the plot owner.
     * @param plotOwnerID The plot owner to remove the coord from.
     * @param plotCoord The plot coord to remove.
     * @return Return the result of the operation.
     */
    public boolean removePlotCoord(UUID plotOwnerID, PlotCoord plotCoord) {
        PlotOwnerDBModel plotOwnerDBModel = findOne(id, plotOwnerID);
        plotOwnerDBModel.removePlotCoord(plotCoord);
        return savePlotOwner(plotOwnerDBModel);
    }

    /**
     * Set the list of owned plots for the plot owner.
     * @param plotCoords The plot coords to set.
     * @param plotOwnerID The UUID of the plow owner who owns the plots.
     * @return Return the result of the operation.
     */
    public boolean setPlotCoords(Set<PlotCoord> plotCoords, UUID plotOwnerID) {
        PlotOwnerDBModel plotOwner = findOne(id, plotOwnerID);
        List<PlotCoord> converted = new ArrayList<>();
        converted.addAll(plotCoords);
        plotOwner.setPlotCoords(converted);
        return savePlotOwner(plotOwner);
    }
}
