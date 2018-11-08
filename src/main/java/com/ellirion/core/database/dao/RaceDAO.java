package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.race.model.Race;

import java.util.List;
import java.util.UUID;

public class RaceDAO extends BasicDAO<RaceDBModel, Datastore> {

    private String id = "_id";

    /**
     * Create a new RaceDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public RaceDAO(final Class<RaceDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    private boolean saveRace(RaceDBModel race) {
        save(race);
        return true;
    }

    /**
     * This creates a new race in the database.
     * @param race The race that is to be added to the DB.
     * @return Return the result of the operation.
     */
    public boolean createRace(Race race) {
        return saveRace(new RaceDBModel(race));
    }

    /**
     * This fetches a specific race from the DB.
     * @param raceID The UUID of the race.
     * @return Return the found race.
     */
    public RaceDBModel getSpecificRace(UUID raceID) {
        return findOne(id, raceID);
    }

    public List<RaceDBModel> getAllRaces() {
        return find().asList();
    }

    /**
     * This method updates a race in the database.
     * @param race The race that should be updated in the DB.
     * @return Return the result of the operation.
     */
    public boolean updateRace(Race race) {
        RaceDBModel model = getSpecificRace(race.getRaceUUID());
        model.update(race);
        return saveRace(model);
    }

    /**
     * Deletes the race from the database.
     * @param raceID The UUID of the race to delete.
     * @return Return the result of the operation.
     */
    public boolean deleteRace(UUID raceID) {
        RaceDBModel race = findOne(id, raceID);
        delete(race);
        return true;
    }
}
