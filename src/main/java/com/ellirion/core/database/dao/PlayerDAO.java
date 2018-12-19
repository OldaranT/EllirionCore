package com.ellirion.core.database.dao;

import org.bukkit.entity.Player;
import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import xyz.morphia.query.Query;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.playerdata.model.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatch.*;

public class PlayerDAO extends BasicDAO<PlayerDBModel, Datastore> {

    private String id = "_id";
    private String raceIDColumn = "raceID";
    private String keyPlayerIDField = ".playerID";
    private String keyGameIDField = ".gameID";

    /**
     * Create a new PlayerDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public PlayerDAO(final Class<PlayerDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    private boolean savePlayer(PlayerDBModel player) {
        return tryCatch(() -> save(player));
    }

    /**
     * Creates a new player and saves this to the database.
     * @param data The Player data
     * @param player The player
     * @return Return true if successfully saved the player.
     */
    public boolean createPlayer(PlayerData data, Player player) {
        PlayerDBModel model = new PlayerDBModel(data, player);
        return savePlayer(model);
    }

    /**
     * This function finds and returns a player with the given player UUID.
     * @param playerID The UUID of the player to fetch.
     * @return Return the found player.
     */
    public PlayerDBModel getPlayer(UUID playerID) {
        return findOne(id + keyPlayerIDField, playerID);
    }

    public List<PlayerDBModel> getPlayers() {
        return find().asList();
    }

    /**
     * This method returns all player data from players from the specified race.
     * @param raceID The race ID of the players their race.
     * @return Return the list of players in that race.
     */
    public List<PlayerDBModel> getPlayers(UUID raceID) {
        List<PlayerDBModel> result = new ArrayList<>();
        if (!tryCatch(() -> result.addAll(createQuery().filter(raceIDColumn, raceID).asList()))) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * This method get's the player data for a specific game.
     * @param playerID The ID of the player.
     * @param gameID The ID of the game.
     * @return return the found data.
     */
    public PlayerDBModel getPlayerFromGame(UUID playerID, UUID gameID) {
        Query query = createQuery().filter(id + keyPlayerIDField, playerID).filter(id + keyGameIDField, gameID);
        return findOne(query);
    }

    /**
     * This method updates the data in the database.
     * @param data The data that needs to be copied to the DB.
     * @param player The player who owns the Data.
     * @return Return true if the save was successful.
     */
    public boolean updatePlayer(PlayerData data, Player player) {
        UUID gameID = GameManager.getInstance().getGameID();
        Query query = createQuery().filter(id + keyPlayerIDField, player.getUniqueId())
                .filter(id + keyGameIDField, gameID);
        PlayerDBModel playerDBModel = findOne(query);
        if (playerDBModel == null) {
            return createPlayer(data, player);
        }
        playerDBModel.update(data, player);
        return savePlayer(playerDBModel);
    }
}
