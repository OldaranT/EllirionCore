package com.ellirion.core;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;

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
        getLogger().info("Introduction is enabled.");
    }


    private void registerCommands() {
        throw new NotImplementedException();
    }

    private void registerEvents() {
        throw new NotImplementedException();
    }
}
