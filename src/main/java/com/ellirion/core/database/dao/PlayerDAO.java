package com.ellirion.core.database.dao;

import org.bukkit.entity.Player;
import xyz.morphia.Datastore;
import xyz.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.database.util.GenericTryCatch;
import com.ellirion.core.playerdata.model.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDAO extends BasicDAO<PlayerDBModel, Datastore> {

    private String id = "_id";
    private String raceIDColumn = "raceID";

    /**
     * Create a new PlayerDAO.
     * @param entityClass the java class you want to persist using this DAO.
     * @param datastore the Datastore which gives access to the MongoDB instance for this DAO.
     */
    public PlayerDAO(final Class<PlayerDBModel> entityClass, final Datastore datastore) {
        super(entityClass, datastore);
    }

    //    @SuppressWarnings({"Duplicates", "CPD-START"})
    // TODO: validate generic try and catch with chris. If this is aproved remove commented code.
    private boolean savePlayer(PlayerDBModel player) {
        return GenericTryCatch.tryCatch(() -> save(player));
        //        try {
        //            save(player);
        //            return true;
        //        } catch (Exception exception) {
        //            Logging.printStackTrace(exception);
        //            return false;
        //        }
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
    public PlayerDBModel getSpecificPlayer(UUID playerID) {
        return findOne(id, playerID);
    }

    public List<PlayerDBModel> getAllPlayers() {
        return find().asList();
    }

    /**
     * This method returns all player data from players from the specified race.
     * @param raceID The race ID of the players their race.
     * @return Return the list of players in that race.
     */
    //    @SuppressWarnings("Duplicates")
    public List<PlayerDBModel> getAllPlayersFromRace(UUID raceID) {
        List<PlayerDBModel> result = new ArrayList<>();
        //        Query query = createQuery().filter(raceIDColumn, raceID);
        GenericTryCatch.tryCatch(() -> result.addAll(createQuery().filter(raceIDColumn, raceID).asList()));
        return result;
        //        try {
        //            Query query = createQuery().filter(raceIDColumn, raceID);
        //            return find(query).asList();
        //        } catch (Exception exception) {
        //            Logging.printStackTrace(exception);
        //            return new ArrayList<>();
        //        }
    }

    /**
     * This method updates the data in the database.
     * @param data The data that needs to be copied to the DB.
     * @param player The player who owns the Data.
     * @return Return true if the save was successful.
     */
    public boolean updatePlayer(PlayerData data, Player player) {
        PlayerDBModel playerDBModel = findOne(id, player.getUniqueId());
        if (playerDBModel == null) {
            return createPlayer(data, player);
        }
        playerDBModel.update(data, player);
        return savePlayer(playerDBModel);
    }
}
