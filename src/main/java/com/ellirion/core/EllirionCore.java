package com.ellirion.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.plotsystem.command.CreatePlotCommand;
import com.ellirion.core.plotsystem.command.GetPlotCommand;
import com.ellirion.core.plotsystem.listener.PlotListener;

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
        getCommand("CreatePlots").setExecutor(new CreatePlotCommand());
        getCommand("GetPlot").setExecutor(new GetPlotCommand());
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlotListener(), this);
    }
}

