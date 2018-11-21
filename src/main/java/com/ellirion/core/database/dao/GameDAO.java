package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.GameDBModel;
import com.ellirion.core.database.util.GenericTryCatch;
import com.ellirion.core.gamemanager.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameDAO extends BasicDAO<GameDBModel, Datastore> {

    private String id = "_id";
    private String uNameColumn = "uName";

    /**
     * Create a new GameDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public GameDAO(final Class<GameDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    //TODO check with crhis.
    private boolean saveGame(GameDBModel game) {
        return GenericTryCatch.tryCatch(() -> save(game));
        //        try {
        //            save(game);
        //            return true;
        //        } catch (Exception exception) {
        //
        //            Logging.printStackTrace(exception);
        //            return false;
        //        }
    }

    /**
     * This creates a new game in the database.
     * @param game The game that is to be added to the DB.
     * @return Return the result of the operation.
     */
    public boolean createGame(Game game) {
        return saveGame(new GameDBModel(game));
    }

    /**
     * This fetches a specific game from the DB.
     * @param gameID The UUID of the game.
     * @return Return the found game.
     */
    public GameDBModel getSpecificGame(UUID gameID) {
        return findOne(id, gameID);
    }

    /**
     * This fetches a specific game from the DB.
     * @param uName The unique name of the game.
     * @return Return the found game.
     */
    public GameDBModel getSpecificGameByName(String uName) {
        return findOne(uNameColumn, uName);
    }

    public List<GameDBModel> getAllGames() {
        return find().asList();
    }

    /**
     * This method updates a game in the database.
     * @param game The game that should be updated in the DB.
     * @return Return the result of the operation.
     */
    public boolean updateRace(Game game) {
        GameDBModel model = getSpecificGame(game.getGameID());
        if (model == null) {
            return createGame(game);
        }
        model.update(game);
        return saveGame(model);
    }

    /**
     * Deletes the game from the database.
     * @param gameID The UUID of the game to delete.
     * @return Return the result of the operation.
     */
    public boolean deleteGame(UUID gameID) { //TODO check this with chris.
        return GenericTryCatch.tryCatch(() -> delete(findOne(id, gameID)));
        //        try {
        //            GameDBModel model = findOne(id, gameID);
        //            delete(model);
        //            return true;
        //        } catch (Exception exception) {
        //            Logging.printStackTrace(exception);
        //            return false;
        //        }
    }

    /**
     * Get all the game names from the database.
     * @return return the string
     */
    public List<String> getAllGameNames() {
        List<String> result = new ArrayList<>();
        find().asList().forEach(model -> result.add(model.getUName()));
        return result;
    }
}
