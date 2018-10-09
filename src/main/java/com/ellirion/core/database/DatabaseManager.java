package com.ellirion.core.database;

import com.mongodb.MongoClient;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.ellirion.core.database.model.PlayerModel;
import com.ellirion.core.database.model.RaceModel;

import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;

    private DatabaseAccessObject playerDAO;
    private DatabaseAccessObject raceDAO;

    /**
     *
     */
    public DatabaseManager() {
        mc = new MongoClient();

        morphia = new Morphia();

        mapDataClasses();

        datastore = morphia.createDatastore(mc, "EllirionCore");
        datastore.ensureIndexes();

        createDatabaseAccessObjects();
    }

    private void mapDataClasses() {
        morphia.map(PlayerModel.class);
        morphia.map(RaceModel.class);
    }

    private void createDatabaseAccessObjects() {
        playerDAO = new DatabaseAccessObject(PlayerModel.class, datastore);
        raceDAO = new DatabaseAccessObject(RaceModel.class, datastore);
    }

    /**
     * save the player to the database.
     * @param player the player.
     * @param cash amount of cash.
     * @param race the race.
     * @param rank the rank.
     */
    public void savePlayer(Player player, int cash, String race, String rank) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId(), player.getName(),
                                                  player.getAddress().getHostName(), cash, race, rank);
        playerDAO.save(playerModel);
    }

    /**
     * @return return all the users.
     */
    public List<PlayerModel> getAllPlayers() {
        return playerDAO.find().asList();
    }

    /**
     * @param uuid the id of the player
     * @return the player model
     */
    public PlayerModel getOnePlayer(UUID uuid) {
        return (PlayerModel) playerDAO.findOne("_id", uuid);
    }

    public List<RaceModel> getAllRaces() {
        return raceDAO.find().asList();
    }

    /**
     * @param raceName The name of the race to fetch.
     * @return return the found raceModel.
     */
    public RaceModel getSpecificRace(String raceName) {
        return (RaceModel) raceDAO.findOne("_id", raceName);
    }
}
