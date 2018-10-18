package com.ellirion.core.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.ellirion.core.database.dao.PlayerDAO;
import com.ellirion.core.database.dao.PlotDAO;
import com.ellirion.core.database.dao.RaceDAO;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.database.model.PlotDBModel;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.Logging;

import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private FileConfiguration connectionConfig;

    private Session session = null;
    private JSch jsch = new JSch();

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
    private Morphia morphia;
    private Datastore datastore;

    // The DAO's
    private PlayerDAO playerDAO;
    private RaceDAO raceDAO;
    private PlotDAO plotDAO;

    /**
     * The database manager opens a session the moment it gets created which allows for access to a remote db server.
     * @param configuration The connection configuration that contains the data to be used to connect.
     */
    public DatabaseManager(final FileConfiguration configuration) {
        connectionConfig = configuration;
        applyConfig();
        // try to reach the remote database.
        connectToServer();
        // create your mongo client.
        mc = new MongoClient(localHost, localPort);

        morphia = new Morphia();

        mapDataClasses();

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
            Logging.printStackTrace(e);
        }
    }

    private void applyConfig() {
        // ssh connection data.
        String sshHeader = connectionConfig.getString("sshHeader", "ssh_connection.");
        username = connectionConfig.getString(sshHeader + "username", "root");
        host = connectionConfig.getString(sshHeader + "host");
        port = connectionConfig.getInt(sshHeader + "port", 22);
        privateKeyPath = connectionConfig.getString(sshHeader + "privateKeyPath");
        passPhrase = connectionConfig.getString(sshHeader + "passPhrase");
        // db coonection data.
        String forwardingHeader = connectionConfig.getString("forwardingHeader", "forwarding_data.");
        localPort = connectionConfig.getInt(forwardingHeader + "localPort", 27017);
        remotePort = connectionConfig.getInt(forwardingHeader + "remotePort", 27017);
        localHost = connectionConfig.getString(forwardingHeader + "localHost", "localhost");
        remoteHost = connectionConfig.getString(forwardingHeader + "remoteHost", "localhost");
        dbName = connectionConfig.getString(forwardingHeader + "DBName", "EllirionCore");
    }

    private void mapDataClasses() {
        morphia.map(PlayerDBModel.class);
        morphia.map(RaceDBModel.class);
        morphia.map(PlotDBModel.class);
    }

    private void createDatabaseAccessObjects() {
        playerDAO = new PlayerDAO(PlayerDBModel.class, datastore);
        raceDAO = new RaceDAO(RaceDBModel.class, datastore);
        plotDAO = new PlotDAO(PlotDBModel.class, datastore);
    }

    /**
     * This function should be called in the onDisable function to close the connection.
     */
    public void disconnectFromServer() {
        try {
            session.delPortForwardingL(localPort);
            session.disconnect();
        } catch (JSchException e) {
            Logging.printStackTrace(e);
        }
    }

    /**
     * This saves a new race to the Database.
     * @param race The race to be stored in the database.
     * @return Return the outcome of the operation.
     */
    public boolean createRace(Race race) {
        return raceDAO.createRace(race);
    }

    public List<RaceDBModel> getAllRaces() {
        return raceDAO.getAllRaces();
    }

    /**
     * Get a specific race from the database.
     * @param raceID The UUID of the race to fetch.
     * @return return the found raceModel.
     */
    public RaceDBModel getSpecificRace(UUID raceID) {
        return raceDAO.getSpecificRace(raceID);
    }

    /**
     * This updates the race in the DB.
     * @param race The race to be updated.
     * @return Return the result of the operation.
     */
    public boolean updateRace(Race race) {
        return raceDAO.updateRace(race);
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
    public List<PlayerDBModel> getAllPlayers() {
        return playerDAO.getAllPlayers();
    }

    /**
     * Get a specific player from the database.
     * @param uuid the id of the player
     * @return the player model
     */
    public PlayerDBModel getOnePlayer(UUID uuid) {
        return playerDAO.getSpecificPlayer(uuid);
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

    /**
     * This creates a plot in the DB.
     * @param plot The plot to be saved in the database.
     * @return Return the result of the operation.
     */
    public boolean createPlot(Plot plot) {
        return plotDAO.createPlot(plot);
    }

    /**
     * This creates a plot in the DB from raw data.
     * @param plotCoord The plotCoord class corrisponding with the plot.
     * @param plotOwnerID The UUID of the plotOwner.
     * @return Return the result of the operation.
     */
    public boolean createPlot(PlotCoord plotCoord, UUID plotOwnerID) {
        return plotDAO.createPlot(plotCoord, plotOwnerID);
    }

    public List<PlotDBModel> getAllPlots() {
        return plotDAO.getAllPlots();
    }

    /**
     * Get all the plots owned by the specified plot owner.
     * @param plotOwnerID The UUID of the plot owner.
     * @return Return the found plots.
     */
    public List<PlotDBModel> getPlotOwnerPlots(UUID plotOwnerID) {
        return plotDAO.getAllPlotsFromPlotOwner(plotOwnerID);
    }

    /**
     * Get the specified plot from the DB.
     * @param plotCoord The plot coord corrisponding with the plot
     * @return Return the found plot.
     */
    public PlotDBModel getSpecificPlot(PlotCoord plotCoord) {
        return plotDAO.getSpecificPlot(plotCoord);
    }

    /**
     * This updates the plot in the DB.
     * @param plot The plot to update.
     * @return Return the result of the operation.
     */
    public boolean updatePlot(Plot plot) {
        return plotDAO.update(plot);
    }
}
