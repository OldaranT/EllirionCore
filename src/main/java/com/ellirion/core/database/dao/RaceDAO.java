package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.race.model.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatch.*;

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
        return tryCatch(() -> save(race));
    }

    /**
     * This creates a new race in the database.
     * @param race The race that is to be added to the DB.
     * @param gameID The ID of the game where the race is from.
     * @return Return the result of the operation.
     */
    public boolean createRace(Race race, UUID gameID) {
        return saveRace(new RaceDBModel(race, gameID));
    }

    /**
     * This fetches a specific race from the DB.
     * @param raceID The UUID of the race.
     * @return Return the found race.
     */
    public RaceDBModel getRace(UUID raceID) {
        return findOne(id, raceID);
    }

    public List<RaceDBModel> getRaces() {
        return find().asList();
    }

    /**
     * This get's all the races from a specific game.
     * @param gameID The ID of the game to get the races from.
     * @return return the found races.
     */
    public List<RaceDBModel> getGameRaces(UUID gameID) {
        final List<RaceDBModel> result = new ArrayList<>();
        if (!tryCatch(() -> result.addAll(createQuery().filter(gameIDColumn, gameID).asList()))) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * This method updates a race in the database.
     * @param race The race that should be updated in the DB.
     * @param gameID The ID of the game where this race belongs.
     * @return Return the result of the operation.
     */
    public boolean updateRace(Race race, UUID gameID) {
        RaceDBModel model = getRace(race.getRaceUUID());
        if (model == null) {
            return createRace(race, gameID);
        }
        //        Query query = createQuery().filter(id, race.getRaceUUID());
        model.update(race);
        //        UpdateOperations ops = createUpdateOperations().set("raceName", race.getName())
        //                .set("players", race.getPlayers())
        //                .set("color", race.getTeamColor())
        //                .set("homePlotCoord", race.getHomePlot().getPlotCoord())
        //                .set("ownedPlots", race.getPlotCoords());
        //        return tryCatch(() -> updateFirst(query, ops));
        return saveRace(model);
    }

    /**
     * Deletes the race from the database.
     * @param raceID The UUID of the race to delete.
     * @return Return the result of the operation.
     */
    public boolean deleteRace(UUID raceID) {
        return tryCatch(() -> delete(findOne(id, raceID)));
    }
}
