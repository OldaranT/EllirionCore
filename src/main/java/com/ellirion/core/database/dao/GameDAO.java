package com.ellirion.core.database.dao;

import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.GameDBModel;
import com.ellirion.core.gamemanager.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatchUtils.tryCatch;

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

    private boolean saveGame(GameDBModel game) {
        return tryCatch(() -> save(game));
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
    public GameDBModel getGame(UUID gameID) {
        return findOne(id, gameID);
    }

    /**
     * This fetches a specific game from the DB.
     * @param uName The unique name of the game.
     * @return Return the found game.
     */
    public GameDBModel getGame(String uName) {
        return findOne(uNameColumn, uName);
    }

    public List<GameDBModel> getGames() {
        return find().asList();
    }

    /**
     * This method updates a game in the database.
     * @param game The game that should be updated in the DB.
     * @return Return the result of the operation.
     */
    public boolean updateGame(Game game) {
        GameDBModel model = getGame(game.getGameID());
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
    public boolean deleteGame(UUID gameID) {
        return tryCatch(() -> delete(findOne(id, gameID)));
    }

    /**
     * Get all the game names from the database.
     * @return return the string
     */
    public List<String> getGameNames() {
        List<String> result = new ArrayList<>();
        tryCatch(() -> find().asList().forEach(model -> result.add(model.getUName())));
        return result;
    }
}
