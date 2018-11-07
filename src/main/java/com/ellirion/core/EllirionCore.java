package com.ellirion.core;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.playerdata.eventlistener.OnPlayerJoin;
import com.ellirion.core.playerdata.eventlistener.OnPlayerQuit;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.command.ClaimPlotCommand;
import com.ellirion.core.plotsystem.command.CreatePlotCommand;
import com.ellirion.core.plotsystem.command.GetPlotCommand;
import com.ellirion.core.plotsystem.command.TeleportToPlotCommand;
import com.ellirion.core.plotsystem.listener.PlotListener;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.race.command.CreateRaceCommand;
import com.ellirion.core.race.command.DestroyRaceCommand;
import com.ellirion.core.race.command.JoinRaceCommand;
import com.ellirion.core.race.eventlistener.OnFriendlyFire;
import com.ellirion.core.race.util.RaceNameTabCompleter;
import com.ellirion.core.util.Logging;
import com.ellirion.core.race.util.CreateRaceTabCompleter;

import java.io.File;
import java.io.IOException;

public class EllirionCore extends JavaPlugin {

    private static EllirionCore INSTANCE;
    private DatabaseManager dbManager;
    @Getter private FileConfiguration dbConnectionConfig;

    /**
     * Constructor to set instance.
     */
    public EllirionCore() {
        super();

        INSTANCE = this;
    }

    public static EllirionCore getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        dbManager.disconnectFromServer();
        getLogger().info("Introduction is disabled.");
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        registerTabCompleters();
        createDBconnectionConfig();
        getLogger().info(RaceManager.getAVAILABLE_COLORS().toString());
        getLogger().info("Introduction is enabled.");
        dbManager = new DatabaseManager(dbConnectionConfig);
        setup();
    }

    private void registerCommands() {
        getCommand("createRace").setExecutor(new CreateRaceCommand());
        getCommand("joinRace").setExecutor(new JoinRaceCommand());
        getCommand("CreatePlots").setExecutor(new CreatePlotCommand(this));
        getCommand("GetPlot").setExecutor(new GetPlotCommand());
        getCommand("TeleportToPlot").setExecutor(new TeleportToPlotCommand());
        getCommand("ClaimPlot").setExecutor(new ClaimPlotCommand());
        getCommand("RemoveRace").setExecutor(new DestroyRaceCommand());
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new OnPlayerJoin(), this);
        pluginManager.registerEvents(new OnPlayerQuit(), this);
        pluginManager.registerEvents(new PlotListener(), this);
        pluginManager.registerEvents(new OnFriendlyFire(), this);
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    private void createDBconnectionConfig() {
        File dbConnectionConfigFile = new File(getDataFolder(), "DBconnectionConfigFile.yml");
        dbConnectionConfig = YamlConfiguration.loadConfiguration(dbConnectionConfigFile);

        dbConnectionConfig.options().header("These are all the connection variables needed to access the database.");

        String sshHeader = "ssh_connection.";
        dbConnectionConfig.addDefault("sshHeader", sshHeader);
        dbConnectionConfig.addDefault(sshHeader + "privateKeyPath", "place here the system path to the private key.");
        dbConnectionConfig.addDefault(sshHeader + "username", "root");
        dbConnectionConfig.addDefault(sshHeader + "host", "the host ip");
        dbConnectionConfig.addDefault(sshHeader + "port", 22);
        dbConnectionConfig.addDefault(sshHeader + "passPhrase", "The passPhrase for your private key.");

        String forwardingHeader = "forwarding_data.";
        dbConnectionConfig.addDefault("forwardingHeader", forwardingHeader);
        dbConnectionConfig.addDefault(forwardingHeader + "localPort", 27017);
        dbConnectionConfig.addDefault(forwardingHeader + "remotePort", 27017);
        dbConnectionConfig.addDefault(forwardingHeader + "localHost", "localhost");
        dbConnectionConfig.addDefault(forwardingHeader + "remoteHost", "localhost");
        dbConnectionConfig.addDefault(forwardingHeader + "DBName", "EllirionCore");

        dbConnectionConfig.options().copyDefaults(true);
        try {
            dbConnectionConfig.save(dbConnectionConfigFile);
        } catch (IOException e) {
            getLogger().throwing(EllirionCore.class.toString(), "createDBconnectionConfig", e);
        }
    }

    private void setup() {
        try {
            //PlotManager.createPlotsFromDatabase(dbManager.getAllPlots());
            getLogger().info(PlotManager.getCHUNK_SIZE() + "");
        } catch (Exception exception) {
            Logging.printStackTrace(exception);
        }
    }

    private void registerTabCompleters() {
        getCommand("createRace").setTabCompleter(new CreateRaceTabCompleter());
        getCommand("RemoveRace").setTabCompleter(new RaceNameTabCompleter());
        getCommand("joinRace").setTabCompleter(new RaceNameTabCompleter());
        
    }
}

