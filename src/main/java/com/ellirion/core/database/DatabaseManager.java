package com.ellirion.core.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.model.PlayerModel;
import com.ellirion.core.database.model.RaceModel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private FileConfiguration connectioConfig;

    private Session session = null;
    private JSch jsch = new JSch();

    // ssh connection
    private String username;// = "root";
    private String host;// = "206.189.4.235";
    private int port;// = 22;
    private String privateKeyPath;// = getPathToKey();

    // forwarding ports
    private int localPort;// = 27017;
    private int remotePort;// = 27017;
    private String localHost;// = "localhost";
    private String remoteHost;// = "localhost";

    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;
    private DatabaseAccessObject playerDAO;
    private DatabaseAccessObject raceDAO;

    /**
     * @param configuration The connection configuration that contains the data to be used to connect.
     */
    public DatabaseManager(final FileConfiguration configuration) {
        connectioConfig = configuration;
        applyConfig();
        try {
            jsch.addIdentity(privateKeyPath, "nikro");
            session = jsch.getSession(username, host, port);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            session.setPortForwardingL(localPort, remoteHost, remotePort);

            mc = new MongoClient(localHost, localPort);

            morphia = new Morphia();

            mapDataClasses();

            datastore = morphia.createDatastore(mc, "EllirionCore");
            datastore.ensureIndexes();

            createDatabaseAccessObjects();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private void applyConfig() {
        // ssh connection data.
        String sshHeader = connectioConfig.getString("sshHeader", "ssh_connection.");
        username = connectioConfig.getString(sshHeader + "username", "root");
        host = connectioConfig.getString(sshHeader + "host");
        port = connectioConfig.getInt(sshHeader + "port", 22);
        privateKeyPath = connectioConfig.getString(sshHeader + "privateKeyPath");
        // db coonection data.
        String forwardingHeader = connectioConfig.getString("forwardingHeader", "forwarding_data.");
        localPort = connectioConfig.getInt(forwardingHeader + "localPort", 27017);
        remotePort = connectioConfig.getInt(forwardingHeader + "remotePort", 27017);
        localHost = connectioConfig.getString(forwardingHeader + "localHost", "localhost");
        remoteHost = connectioConfig.getString(forwardingHeader + "remoteHost", "localhost");
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

    private String getPathToKey() {
        String path = "";
        try {
            path = EllirionCore.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            path = path.substring(0, path.lastIndexOf("/"));
            path = path.substring(0, path.lastIndexOf("/"));
            path += "/key/privateKey";
        } catch (URISyntaxException e) {
            EllirionCore.getINSTANCE().getLogger().severe("key path not found!");
        }

        return path;
    }

    /**
     * This function should be called in the onDisable function to close the connection.
     */
    public void closeSession() {
        try {
            session.delPortForwardingL(localPort);
            session.disconnect();
        } catch (JSchException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            EllirionCore.getINSTANCE().getLogger().severe(errors.toString());
        }
    }
}
