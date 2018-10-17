package com.ellirion.core.database.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlotDBModel;

public class PlotDAO extends BasicDAO<PlotDBModel, Datastore> {

    /**
     * Create a new BasicDAO
     * @param entityClass the class of the POJO you want to persist using this DAO
     * @param ds the Datastore which gives access to the MongoDB instance for this DAO
     */
    public PlotDAO(final Class<PlotDBModel> entityClass, final Datastore ds) {
        super(entityClass, ds);
    }
}
