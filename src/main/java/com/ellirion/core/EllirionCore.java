package com.ellirion.core;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.database.model.GameDBModel;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.gamemanager.command.AssignTradingCenterCommand;
import com.ellirion.core.gamemanager.command.BeginGameModeCommand;
import com.ellirion.core.gamemanager.command.CancelSetupCommand;
import com.ellirion.core.gamemanager.command.ConfirmGamemodeCommand;
import com.ellirion.core.gamemanager.command.GetGameStateCommand;
import com.ellirion.core.gamemanager.command.LoadGameModeCommand;
import com.ellirion.core.gamemanager.command.NextSetupStepCommand;
import com.ellirion.core.groundwar.command.ConfirmGroundWarCommand;
import com.ellirion.core.groundwar.command.CreateGroundwarCommand;
import com.ellirion.core.groundwar.command.GetGroundWarCommand;
import com.ellirion.core.groundwar.command.JoinGroundWarCommand;
import com.ellirion.core.groundwar.command.WagerPlotCommand;
import com.ellirion.core.groundwar.listeners.MoveIntoGroundWarListener;
import com.ellirion.core.groundwar.listeners.MoveOffGroundWarListener;
import com.ellirion.core.groundwar.listeners.PlayerDeathListener;
import com.ellirion.core.playerdata.eventlistener.OnPlayerJoin;
import com.ellirion.core.playerdata.eventlistener.OnPlayerQuit;
import com.ellirion.core.groundwar.command.CancelGroundWarCommand;
import com.ellirion.core.gamemanager.util.GameNameTabCompleter;
import com.ellirion.core.plotsystem.command.ClaimPlotCommand;
import com.ellirion.core.plotsystem.command.CreatePlotCommand;
import com.ellirion.core.plotsystem.command.GetPlotCommand;
import com.ellirion.core.plotsystem.command.GetPlotMapCommand;
import com.ellirion.core.groundwar.command.LeaveGroundWarCommand;
import com.ellirion.core.plotsystem.command.TeleportToPlotCommand;
import com.ellirion.core.plotsystem.listener.PlotListener;
import com.ellirion.core.race.command.CreateRaceCommand;
import com.ellirion.core.race.command.DeleteRaceCommand;
import com.ellirion.core.race.command.JoinRaceCommand;
import com.ellirion.core.race.eventlistener.OnFriendlyFire;
import com.ellirion.core.race.util.CreateRaceTabCompleter;
import com.ellirion.core.race.util.RaceNameTabCompleter;
import com.ellirion.core.util.Logging;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        getLogger().info("EllirionCore is disabled.");
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        registerTabCompleters();
        createDBconnectionConfig();
        dbManager = new DatabaseManager(dbConnectionConfig);
        setup();
        getLogger().info("EllirionCore is enabled.");
    }

    private void registerCommands() {
        //Race
        getCommand("CreateRace").setExecutor(new CreateRaceCommand());
        getCommand("JoinRace").setExecutor(new JoinRaceCommand());
        getCommand("RemoveRace").setExecutor(new DeleteRaceCommand());

        //Plots
        getCommand("CreatePlots").setExecutor(new CreatePlotCommand(this));
        getCommand("GetPlot").setExecutor(new GetPlotCommand());
        getCommand("TeleportToPlot").setExecutor(new TeleportToPlotCommand());
        getCommand("ClaimPlot").setExecutor(new ClaimPlotCommand());
        getCommand("PlotMap").setExecutor(new GetPlotMapCommand());

        //Gamemode
        getCommand("BeginGamemode").setExecutor(new BeginGameModeCommand());
        getCommand("GameState").setExecutor(new GetGameStateCommand());
        getCommand("NextStep").setExecutor(new NextSetupStepCommand());
        getCommand("LoadGame").setExecutor(new LoadGameModeCommand());
        getCommand("AssignTradingCenter").setExecutor(new AssignTradingCenterCommand());
        getCommand("ConfirmGamemode").setExecutor(new ConfirmGamemodeCommand());
        getCommand("CancelSetup").setExecutor(new CancelSetupCommand());

        //GroundWar
        getCommand("CreateGroundWar").setExecutor(new CreateGroundwarCommand());
        getCommand("WagerPlot").setExecutor(new WagerPlotCommand());
        getCommand("GetGroundwar").setExecutor(new GetGroundWarCommand());
        getCommand("ConfirmGroundWar").setExecutor(new ConfirmGroundWarCommand());
        getCommand("JoinGroundWar").setExecutor(new JoinGroundWarCommand());
        getCommand("LeaveGroundWar").setExecutor(new LeaveGroundWarCommand());
        getCommand("CancelGroundWar").setExecutor(new CancelGroundWarCommand());
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new OnPlayerJoin(), this);
        pluginManager.registerEvents(new OnPlayerQuit(), this);
        pluginManager.registerEvents(new PlotListener(), this);
        pluginManager.registerEvents(new OnFriendlyFire(), this);
        pluginManager.registerEvents(new MoveOffGroundWarListener(), this);
        pluginManager.registerEvents(new MoveIntoGroundWarListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
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
            List<GameDBModel> gameDBModels = dbManager.getGames();

            for (GameDBModel gameDbModel : gameDBModels) {
                GameManager.addGame(gameDbModel);
            }
        } catch (Exception exception) {
            Logging.printStackTrace(exception);
        }
    }

    private void registerTabCompleters() {
        getCommand("CreateRace").setTabCompleter(new CreateRaceTabCompleter());
        getCommand("RemoveRace").setTabCompleter(new RaceNameTabCompleter());
        getCommand("JoinRace").setTabCompleter(new RaceNameTabCompleter());
        getCommand("LoadGame").setTabCompleter(new GameNameTabCompleter());
    }
}

