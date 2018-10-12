package com.ellirion.core.plotsystem.runnable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.ellirion.core.plotsystem.PlotManager;

public class PlotCreateTask extends BukkitRunnable {

    private Player player;
    private int plotSize;
    private int mapRadius;
    private int centerX;
    private int centerZ;
    private PlotManager plotManager = new PlotManager();
    private int counter;

    /**
     * Create all plots in a certain world.
     * @param player Player that is creating the plots
     * @param plotSize The size of the plot.
     * @param mapRadius The radius of the map.
     * @param centerX The center X of the map.
     * @param centerZ The center Y of the map.
     */
    public PlotCreateTask(final Player player, final int plotSize, final int mapRadius, final int centerX,
                          final int centerZ) {
        this.player = player;
        this.plotSize = plotSize;
        this.mapRadius = mapRadius;
        this.centerX = centerX;
        this.centerZ = centerZ;
        counter = 0;
    }

    @SuppressWarnings("PMD")
    @Override
    public void run() {
        counter++;
        System.out.println("Counter count: " + counter);
        plotManager.createPlots(player.getWorld(), plotSize, mapRadius, centerX, centerZ);
    }
}

