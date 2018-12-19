package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.GroundwarDBModel;
import com.ellirion.core.groundwar.model.GroundWar;

import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatch.*;

public class GroundWarDAO extends BasicDAO<GroundwarDBModel, Datastore> {

    /**
     * This is the DAO for the GroundWar database model.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public GroundWarDAO(final Class<GroundwarDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    private boolean saveGroundWar(GroundwarDBModel groundWar) {
        return tryCatch(() -> save(groundWar));
    }

    /**
     * This creates a new groundwar in the database.
     * @param groundWar The groundwar to save.
     * @param gameID The UUID of the game.
     * @return Return the result of the operation.
     */
    public boolean createGroundWar(GroundWar groundWar, UUID gameID) {
        return saveGroundWar(new GroundwarDBModel(groundWar, gameID));
    }
}
