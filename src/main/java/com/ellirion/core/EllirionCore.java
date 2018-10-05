package com.ellirion.core;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;
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
        getLogger().info("Introduction is enabled.");
    }

    private void registerCommands() {
        getCommand("createRace").setExecutor(new CreateRaceCommand());
        getCommand("joinRace").setExecutor(new JoinRaceCommand());
    }

    private void registerEvents() {
        throw new NotImplementedException();
    }
}
