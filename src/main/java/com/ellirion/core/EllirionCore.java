package com.ellirion.core;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;
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
        getLogger().info("Introduction is enabled.");
        dbManager = new DatabaseManager();
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }
}
