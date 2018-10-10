package com.ellirion.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.plotsystem.command.CreatePlotCommand;
import com.ellirion.core.plotsystem.command.GetPlotCommand;
import com.ellirion.core.plotsystem.command.SetPlotOwnerCommand;
import com.ellirion.core.plotsystem.listener.PlotListener;
import com.ellirion.core.command.CreateRaceCommand;
import com.ellirion.core.command.JoinRaceCommand;

public class EllirionCore extends JavaPlugin {

    private static EllirionCore INSTANCE;

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
        getLogger().info("Introduction is disabled.");
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        getLogger().info("Introduction is enabled.");
    }

    private void registerCommands() {
        getCommand("createRace").setExecutor(new CreateRaceCommand());
        getCommand("joinRace").setExecutor(new JoinRaceCommand());
        getCommand("CreatePlots").setExecutor(new CreatePlotCommand());
        getCommand("GetPlot").setExecutor(new GetPlotCommand());
        getCommand("SetOwner").setExecutor(new SetPlotOwnerCommand());
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlotListener(), this);
    }
}

