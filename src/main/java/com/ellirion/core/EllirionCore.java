package com.ellirion.core;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.playerdata.eventlistener.OnPlayerJoin;
import com.ellirion.core.playerdata.eventlistener.OnPlayerQuit;
import com.ellirion.core.plotsystem.command.ClaimPlotCommand;
import com.ellirion.core.plotsystem.command.CreatePlotCommand;
import com.ellirion.core.plotsystem.command.GetPlotCommand;
import com.ellirion.core.plotsystem.command.SetPlotOwnerCommand;
import com.ellirion.core.plotsystem.command.TeleportToPlotCommand;
import com.ellirion.core.plotsystem.listener.PlotListener;
import com.ellirion.core.races.command.CreateRaceCommand;
import com.ellirion.core.races.command.JoinRaceCommand;
import com.ellirion.core.races.eventlistener.OnFriendlyFire;
import com.ellirion.core.database.DatabaseManager;

public class EllirionCore extends JavaPlugin {

    private static EllirionCore INSTANCE;
    private DatabaseManager dbManager;

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

    private static void registerCommands() {
        throw new NotImplementedException();
    }

    private static void registerEvents() {
        throw new NotImplementedException();
    }

    @Override
    public void onDisable() {
        getLogger().info("Introduction is disabled.");
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        getLogger().info("Introduction is enabled.");
        dbManager = new DatabaseManager();
    }

    private void registerCommands() {
        getCommand("createRace").setExecutor(new CreateRaceCommand());
        getCommand("joinRace").setExecutor(new JoinRaceCommand());
        getCommand("CreatePlots").setExecutor(new CreatePlotCommand());
        getCommand("GetPlot").setExecutor(new GetPlotCommand());
        getCommand("SetOwner").setExecutor(new SetPlotOwnerCommand());
        getCommand("TeleportToPlot").setExecutor(new TeleportToPlotCommand());
        getCommand("ClaimPlot").setExecutor(new ClaimPlotCommand());
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
}

