package com.ellirion.core.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.morphia.Datastore;
import xyz.morphia.Morphia;
import com.ellirion.core.database.dao.GameDAO;
import com.ellirion.core.database.dao.GroundWarDAO;
import com.ellirion.core.database.dao.PlayerDAO;
import com.ellirion.core.database.dao.PlotCoordDAO;
import com.ellirion.core.database.dao.RaceDAO;
import com.ellirion.core.database.dao.TradingCenterDAO;
import com.ellirion.core.database.model.GameDBModel;
import com.ellirion.core.database.model.GroundwarDBModel;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.database.model.PlotCoordDBModel;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.database.model.TradingCenterDBModel;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.gamemanager.model.Game;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.LoggingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatchUtils.tryCatch;

public class DatabaseManager {

    private final Morphia morphia;
    private final Datastore datastore;
    private FileConfiguration connectionConfig;
    private Session session = null;
    private JSch jsch = new JSch();

    // Is the database local?
    private boolean localDB;

    // ssh connection.
    private String username;
    private String host;
    private int port;
    private String privateKeyPath;
    private String passPhrase;

    // forwarding ports.
    private int localPort;
    private int remotePort;
    private String localHost;
    private String remoteHost;
    private String dbName;

    // MongoDB interfacing.
    private MongoClient mc;

    // The DAO's
    private GameDAO gameDAO;
    private PlayerDAO playerDAO;
    private RaceDAO raceDAO;
    private PlotCoordDAO plotCoordDAO;
    private TradingCenterDAO tradingCenterDAO;
    private GroundWarDAO groundWarDAO;

    /**
     * The database manager opens a session the moment it gets created which allows for access to a remote db server.
     * @param configuration The connection configuration that contains the data to be used to connect.
     */
    public DatabaseManager(final FileConfiguration configuration) {
        connectionConfig = configuration;
        applyConfig();
        if (!localDB)
        // try to reach the remote database.
        {
            connectToServer();
        }
        // create your mongo client.
        mc = new MongoClient(localHost, localPort);

        morphia = new Morphia();
        // This makes it so we can store empty lists and arrays in the database.
        // The reason to do this is that, when you don't store it and then try to retrieve it,
        // it will result in a null pointer for an array or list.
        morphia.getMapper().getOptions().setStoreEmpties(true);
        // This maps all the classes in the model package.
        // This means that all the DBModels are being mapped.
        morphia.mapPackage("model");

        datastore = morphia.createDatastore(mc, dbName);
        datastore.ensureIndexes();

        createDatabaseAccessObjects();
    }

    private void connectToServer() {
        try {
            // tell JSch to use your private key with pass phrase as login method.
            jsch.addIdentity(privateKeyPath, passPhrase);
            // create a session with the location of the remote db server.
            session = jsch.getSession(username, host, port);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            // tell the session to forward any request to our local port to the remote db server.
            session.setPortForwardingL(localPort, remoteHost, remotePort);
        } catch (JSchException e) {
            LoggingUtils.printStackTrace(e);
        }
    }

    private void applyConfig() {
        // Is the DB local?
        localDB = connectionConfig.getBoolean("localDB", false);

        String forwardingHeader = connectionConfig.getString("forwardingHeader", "forwarding_data.");

        localPort = connectionConfig.getInt(forwardingHeader + "localPort", 27017);
        localHost = connectionConfig.getString(forwardingHeader + "localHost", "localhost");
        dbName = connectionConfig.getString(forwardingHeader + "DBName", "EllirionCore");

        if (!localDB) {
            // ssh connection data.
            String sshHeader = connectionConfig.getString("sshHeader", "ssh_connection.");
            username = connectionConfig.getString(sshHeader + "username", "root");
            host = connectionConfig.getString(sshHeader + "host");
            port = connectionConfig.getInt(sshHeader + "port", 22);
            privateKeyPath = connectionConfig.getString(sshHeader + "privateKeyPath");
            passPhrase = connectionConfig.getString(sshHeader + "passPhrase");
            
            // db coonection data.
            remotePort = connectionConfig.getInt(forwardingHeader + "remotePort", 27017);
            remoteHost = connectionConfig.getString(forwardingHeader + "remoteHost", "localhost");
        }
    }

    private void createDatabaseAccessObjects() {
        gameDAO = new GameDAO(GameDBModel.class, datastore);
        playerDAO = new PlayerDAO(PlayerDBModel.class, datastore);
        raceDAO = new RaceDAO(RaceDBModel.class, datastore);
        plotCoordDAO = new PlotCoordDAO(PlotCoordDBModel.class, datastore);
        tradingCenterDAO = new TradingCenterDAO(TradingCenterDBModel.class, datastore);
        groundWarDAO = new GroundWarDAO(GroundwarDBModel.class, datastore);
    }

    /**
     * This function should be called in the onDisable function to close the connection.
     */
    public void disconnectFromServer() {
        if (localDB) {
            LoggingUtils.printMessage("You are connected to a local Database. Ignore this.");
            return;
        }
            try {
                session.delPortForwardingL(localPort);
                session.disconnect();
            } catch (JSchException e) {
                LoggingUtils.printStackTrace(e);
            }
    }

    //region ==== Game ====

    /**
     * This saves a new game to the Database.
     * @param game The game to be stored in the database.
     * @return Return the outcome of the operation.
     */
    public boolean createGame(Game game) {
        return gameDAO.createGame(game);
    }

    /**
     * Get a specific game from the database.
     * @param gameID The UUID of the game to fetch.
     * @return return the found gameDBModel.
     */
    public GameDBModel getGame(UUID gameID) {
        return gameDAO.getGame(gameID);
    }

    /**
     * This fetches a specific game from the DB.
     * @param uName The unique name of the game.
     * @return Return the found game.
     */
    public GameDBModel getGame(String uName) {
        return gameDAO.getGame(uName);
    }

    public List<GameDBModel> getGames() {
        return gameDAO.getGames();
    }

    /**
     * This updates the game in the DB.
     * @param game The game to be updated.
     * @return Return the result of the operation.
     */
    public boolean updateGame(Game game) {
        return gameDAO.updateGame(game);
    }

    /**
     * This deletes the game from the DB.
     * @param game The game to be deleted.
     * @return Return the result of the operation.
     */
    public boolean deleteGame(Game game) {
        return gameDAO.deleteGame(game.getGameID());
    }

    //endregion

    //region ===== RACE =====

    /**
     * This saves a new race to the Database.
     * @param race The race to be stored in the database.
     * @return Return the outcome of the operation.
     */
    public boolean createRace(Race race) {
        return raceDAO.createRace(race, GameManager.getInstance().getGame().getGameID());
    }

    public List<RaceDBModel> getRaces() {
        return raceDAO.getRaces();
    }

    /**
     * Get a specific race from the database.
     * @param raceID The UUID of the race to fetch.
     * @return return the found raceModel.
     */
    public RaceDBModel getRace(UUID raceID) {
        return raceDAO.getRace(raceID);
    }

    /**
     * This updates the race in the DB.
     * @param race The race to be updated.
     * @return Return the result of the operation.
     */
    public boolean updateRace(Race race) {
        return raceDAO.updateRace(race, GameManager.getInstance().getGame().getGameID());
    }

    /**
     * This get's all the races from a specific game from the database.
     * @param gameID The ID of the game to get the races from.
     * @return return the found list or an empty list but not a null to prevent NPE's.
     */
    public List<RaceDBModel> getRaces(UUID gameID) {
        final List<RaceDBModel> result = new ArrayList<>();
        if (!tryCatch(() -> result.addAll(raceDAO.getGameRaces(gameID)))) {
            return new ArrayList<>();
        }
        return result;
    }

    //endregion

    //region ===== Player =====

    /**
     * Delete the race from the database.
     * @param raceID The UUID of the race to delete.
     * @return Return the result of the operation.
     */
    public boolean deleteRace(UUID raceID) {
        return raceDAO.deleteRace(raceID);
    }

    /**
     * This function directs the create request to the playerDAO.
     * @param player The player that owns the Data.
     * @param playerData The Data of the player.
     * @return Return if the creation was successful.
     */
    public boolean createPlayer(PlayerData playerData, Player player) {
        return playerDAO.createPlayer(playerData, player);
    }

    /**
     * Get all players from the database.
     * @return return all the users.
     */
    public List<PlayerDBModel> getPlayers() {
        return playerDAO.getPlayers();
    }

    /**
     * Get a specific player from the database.
     * @param uuid the id of the player
     * @return the player model
     */
    public PlayerDBModel getPlayer(UUID uuid) {
        return playerDAO.getPlayer(uuid);
    }

    /**
     * This gets the player data for a specific game.
     * @param gameID The ID of the game.
     * @param playerID The ID of the player.
     * @return Return the found data.
     */
    public PlayerDBModel getPlayerFromGame(UUID gameID, UUID playerID) {
        return playerDAO.getPlayerFromGame(playerID, gameID);
    }

    /**
     * This tells the playerDAO to update the given player with the given data.
     * @param data The data to be transferred to the DB.
     * @param player The player who owns the data.
     * @return return the result of the operation.
     */
    public boolean updatePlayer(PlayerData data, Player player) {
        return playerDAO.updatePlayer(data, player);
    }

    //endregion

    //region ===== Plot =====

    /**
     * This saves a plotcoord from raw data and not a plot object to the database.
     * @param gameID the game id of the plot.
     * @param plotCoord The plot coords class.
     * @return Return the result of the operation.
     */
    public boolean createPlotCoord(UUID gameID, PlotCoord plotCoord) {
        return plotCoordDAO.createPlotCoord(gameID, plotCoord);
    }

    public List<PlotCoordDBModel> getPlotCoords() {
        return plotCoordDAO.getPlotCoords();
    }

    /**
     * Get all plotcoords by gameID.
     * @param gameID the gameID of the plots to fetch.
     * @return return the found plotcoords.
     */
    public List<PlotCoordDBModel> getPlotCoords(UUID gameID) {
        return plotCoordDAO.getPlotCoords(gameID);
    }

    //endregion

    //region ===== Trading Center =====

    /**
     * Save the trading center to the database.
     * @param tradingCenter The trading center to save.
     * @return return the result of the operation.
     */
    public boolean createTradingCenter(TradingCenter tradingCenter) {
        return tradingCenterDAO.createTradingCenter(tradingCenter, GameManager.getInstance().getGame().getGameID());
    }

    /**
     * Get the trading center db model.
     * @param game the game whose trading center model to retrieve
     * @return a model of a trading center
     */
    public TradingCenterDBModel getTradingCenter(UUID game) {
        return tradingCenterDAO.getTradingCenter(game);
    }

    /**
     * This method calls the update method in the DAO.
     * @param tradingCenter The tradingCenter to update.
     * @return The result of the operation.
     */
    public boolean updateTradingCenter(TradingCenter tradingCenter) {
        return tradingCenterDAO.updateTradingCenter(tradingCenter);
    }

    //endregion

    //region ===== Ground War =====

    /**
     * This creates a groundwar in the database.
     * @param groundWar The groundwar to save to the database.
     * @param gameID The game ID.
     * @return Return the result of the operation.
     */
    public boolean createGroundWar(GroundWar groundWar, UUID gameID) {
        return groundWarDAO.createGroundWar(groundWar, gameID);
    }

    /**
     * This updates the groundwar in the database.
     * @param groundWar The groundwar to update.
     * @param gameID The gameID.
     * @return return the result of the operation.
     */
    public boolean updateGroundWar(GroundWar groundWar, UUID gameID) {
        return groundWarDAO.updateGroundWar(groundWar, gameID);
    }
    //endregion
}
