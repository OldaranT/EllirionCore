package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import xyz.morphia.query.Query;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.race.model.Race;

import java.util.List;
import java.util.UUID;

public class RaceDAO extends BasicDAO<RaceDBModel, Datastore> {

    private String id = "_id";
    private String gameIDColumn = "gameID";

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
     * @param gameID The ID of the game where the race is from.
     * @return Return the result of the operation.
     */
    public boolean createRace(Race race, int gameID) {
        return saveRace(new RaceDBModel(race, gameID));
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
     * This get's all the races from a specific game.
     * @param gameID The ID of the game to get the races from.
     * @return return the found races.
     */
    public List<RaceDBModel> getGameRaces(int gameID) {
        Query query = createQuery().filter(gameIDColumn, gameID);
        return find(query).asList();
    }

    /**
     * This method updates a race in the database.
     * @param race The race that should be updated in the DB.
     * @param gameID The ID of the game where this race belongs.
     * @return Return the result of the operation.
     */
    public boolean updateRace(Race race, int gameID) {
        RaceDBModel model = getSpecificRace(race.getRaceUUID());
        if (model == null) {
            return createRace(race, gameID);
        }
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
