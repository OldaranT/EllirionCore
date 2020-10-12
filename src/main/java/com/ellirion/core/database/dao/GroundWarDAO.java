package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import xyz.morphia.query.Query;
import com.ellirion.core.database.model.GroundwarDBModel;
import com.ellirion.core.groundwar.model.GroundWar;

import java.util.Date;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatchUtils.tryCatch;

public class GroundWarDAO extends BasicDAO<GroundwarDBModel, Datastore> {

    private String startDateColumn = "key.started";
    private String createdByColumn = "key.createdBy";
    private String gameIDColumn = "key.gameID";

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

    /**
     * This gets a groundwar from the database.
     * @param started The date and time the ground war started.
     * @param createdBy The player that created groundwar.
     * @param gameID The ID of the game.
     * @return return the found groundwar.
     */
    public GroundwarDBModel getGroundWar(Date started, UUID createdBy, UUID gameID) {
        Query query = createQuery().filter(startDateColumn, started).filter(createdByColumn, createdBy).filter(
                gameIDColumn, gameID);
        return findOne(query);
    }

    /**
     * This updates the groundwar in the database.
     * @param groundWar The groundwar to update.
     * @param gameID The gameID of the game.
     * @return return the result of the operation.
     */
    public boolean updateGroundWar(GroundWar groundWar, UUID gameID) {
        GroundwarDBModel groundwarDBModel = getGroundWar(groundWar.getResults().getStarted(), groundWar.getCreatedBy(),
                                                         gameID);
        groundwarDBModel.update(groundWar);
        return saveGroundWar(groundwarDBModel);
    }
}
