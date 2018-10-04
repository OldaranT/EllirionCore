package com.ellirion.core.database;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class DatabaseAccessObject extends BasicDAO {

    /**
     * Create a new BasicDAO.
     * @param entityClass the class of the POJO you want to persist using this DAO.
     * @param ds the Datastore which gives access to the MongoDB instance for this DAO.
     */
    protected DatabaseAccessObject(final Class entityClass, final Datastore ds) {
        super(entityClass, ds);
    }
}
