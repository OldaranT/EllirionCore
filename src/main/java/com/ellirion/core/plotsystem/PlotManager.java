package com.ellirion.core.plotsystem;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.database.model.PlotDBModel;
import com.ellirion.core.model.Point;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.util.Logging;

import java.util.HashMap;
import java.util.List;

public class PlotManager {

    private static final HashMap<PlotCoord, Plot> SAVED_PLOTS = new HashMap<>();
    @Getter private static final int LOWEST_Y = 0;
    @Getter private static final int HIGHEST_Y = 255;
    @Getter private static final int CHUNK_SIZE = 16;
    @Setter @Getter private static int CENTER_OFFSET_X;
    @Setter @Getter private static int CENTER_OFFSET_Z;
    @Setter @Getter private static int PLOT_SIZE;

    private static DatabaseManager DATABASE_MANAEGR = EllirionCore.getINSTANCE().getDbManager();

    public static HashMap<PlotCoord, Plot> getSavedPlots() {
        return SAVED_PLOTS;
    }

    /**
     * Get the plot that the player is standing in.
     * @param location location of the player.
     * @return The plot player is standing in.
     */
    public static Plot getPlotFromLocation(Location location) {
        int x = location.getBlockX() - (CENTER_OFFSET_X * CHUNK_SIZE);
        int z = location.getBlockZ() - (CENTER_OFFSET_Z * CHUNK_SIZE);

        int plotCordX = Math.floorDiv(x, PLOT_SIZE);
        int plotCordZ = Math.floorDiv(z, PLOT_SIZE);

        PlotCoord plotCoord = new PlotCoord(plotCordX, plotCordZ, location.getWorld().getName());

        return SAVED_PLOTS.get(plotCoord);
    }

    /**
     * Return the plot by coordinate.
     * @param plotCoord plotCoord of the plot to return.
     * @return the plot that is requested.
     */
    public static Plot getPlotByCoordinate(PlotCoord plotCoord) {
        return SAVED_PLOTS.get(plotCoord);
    }

    /**
     * Get all the plots that are saved in the database and create them.
     * @param plots plots to create.
     * @return returns true if the plots are created.
     */
    public static boolean createPlotsFromDatabase(List<PlotDBModel> plots) {
        for (PlotDBModel plotDBModel : plots) {
            Plot plot = plotDBModel.convertPlotFromDatabase();

            if (SAVED_PLOTS.get(plotDBModel.getPlotCoord()) == null) {
                SAVED_PLOTS.put(plot.getPlotCoord(), plot);
                EllirionCore.getINSTANCE().getLogger().info(plot.getName());
            }
        }
        return true;
    }

    /**
     * Create a hashmap with plots.
     * @param world The world plots being created in.
     * @param mapRadius The radius of the map.
     * @param centerX The center X of the map.
     * @param centerZ The center Y of the map.
     * @return Returns true if the plots are successfully created.
     */
    public static Boolean createPlots(World world, int mapRadius, int centerX, int centerZ) {
        //return new Promise<Boolean>(f -> {
            int mapCenterX = centerX * CHUNK_SIZE;
            int mapCenterZ = centerZ * CHUNK_SIZE;
            int currentPlot = 0;
            int amountOfPlots = mapRadius * mapRadius * 4;

            for (int startCountX = -mapRadius; startCountX < mapRadius; startCountX++) {
                for (int startCountZ = -mapRadius; startCountZ < mapRadius; startCountZ++) {
                    currentPlot++;
                    if (Math.floorMod(currentPlot, (amountOfPlots / 10)) == 0) {
                        EllirionCore.getINSTANCE().getLogger().info("Progress: " + currentPlot + " / " + amountOfPlots);
                    }

                    PlotCoord plotCoord = new PlotCoord(startCountX, startCountZ, world.getName());

                    try {
                        //If plot already exist skip it.
                        if (SAVED_PLOTS.get(plotCoord) == null) {
                            String name = Integer.toString(startCountX) + " , " + Integer.toString(startCountZ);
                            int currentX = startCountX * PLOT_SIZE + mapCenterX;
                            int currentZ = startCountZ * PLOT_SIZE + mapCenterZ;

                            Point lowerPoint = new Point(currentX, LOWEST_Y, currentZ);
                            Point highestPoint = new Point(currentX + PLOT_SIZE - 1, HIGHEST_Y,
                                                           currentZ + PLOT_SIZE - 1);

                            SAVED_PLOTS.put(plotCoord,
                                            new Plot(name, plotCoord, lowerPoint, highestPoint, PLOT_SIZE, world,
                                                     world.getUID()));
                        }
                    } catch (Exception e) {
                        Logging.printStackTrace(e);
                        return false;
                        //f.resolve(false);
                    }
                }
            }
            return true;
            //f.resolve(true);
        //}, true);
    }

    /**
     * This updates the plot in the database.
     * @param plot The plot to update in the database.
     * @return Return the result of the operation.
     */
    public static boolean updatePlotInDB(Plot plot) {
        return DATABASE_MANAEGR.updatePlot(plot);
    }
}

