package com.ellirion.core.database.dao;

import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.playerdata.model.PlayerData;

public class PlayerDAO extends BasicDAO<PlayerDBModel, Datastore> {

    /**
     * Create a new BasicDAO.
     * @param entityClass the class of the POJO you want to persist using this DAO
     * @param ds the Datastore which gives access to the MongoDB instance for this DAO
     */
    public PlayerDAO(final Class<PlayerDBModel> entityClass, final Datastore ds) {
        super(entityClass, ds);
    }

    private boolean savePlayer(PlayerDBModel player) {
        save(player);
        return true;
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
}
