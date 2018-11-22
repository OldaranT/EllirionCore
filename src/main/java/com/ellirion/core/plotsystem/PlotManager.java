package com.ellirion.core.plotsystem;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.model.PlotDBModel;
import com.ellirion.core.database.model.TradingCenterDBModel;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.PlotOwner;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.Logging;
import com.ellirion.util.model.BoundingBox;
import com.ellirion.util.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class PlotManager {

    private static final HashMap<PlotCoord, Plot> SAVED_PLOTS = new HashMap<>();
    @Getter private static final int LOWEST_Y = 0;
    @Getter private static final int HIGHEST_Y = 255;
    @Getter private static final int CHUNK_SIZE = 16;
    @Setter @Getter private static int CENTER_OFFSET_X;
    @Setter @Getter private static int CENTER_OFFSET_Z;
    @Setter @Getter private static int PLOT_SIZE;

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

        PlotCoord plotCoord = new PlotCoord(GameManager.getInstance().getGame().getGameID(), plotCordX, plotCordZ,
                                            location.getWorld().getName());

        return SAVED_PLOTS.get(plotCoord);
    }

    /**
     * Return the plot by coordinate.
     * @param plotCoord plotCoord of the plot to return.
     * @return the plot that is requested.
     */
    public static Plot getPlotByCoordinate(PlotCoord plotCoord) {
        //Not using SAVED_PLOTS.get because it wouldn't find the race homePlot after loading.
        for (Map.Entry entry : SAVED_PLOTS.entrySet()) {
            PlotCoord key = (PlotCoord) entry.getKey();
            if (key.equals(plotCoord)) {
                return (Plot) entry.getValue();
            }
        }

        return SAVED_PLOTS.get(plotCoord);
    }

    /**
     * Get all the plots that are saved in the database and create them.
     * @param plots plots to create.
     * @return returns true if the plots are created.
     */
    public static boolean createPlotsFromDatabase(List<PlotDBModel> plots) {
        for (PlotDBModel plotDBModel : plots) {
            Plot plot = new Plot(plotDBModel);
            PlotCoord coord = plot.getPlotCoord();

            if (SAVED_PLOTS.get(coord) == null) {
                SAVED_PLOTS.put(coord, plot);
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
    public static List<Plot> createPlots(World world, int mapRadius, int centerX, int centerZ) {
        int mapCenterX = centerX * CHUNK_SIZE;
        int mapCenterZ = centerZ * CHUNK_SIZE;
        int currentPlot = 0;
        int amountOfPlots = mapRadius * mapRadius * 4;
        int interval = 10;

        UUID gameID = GameManager.getInstance().getGame().getGameID();
        PLOT_SIZE = GameManager.getInstance().getPlotSize();
        List<Plot> result = new ArrayList<>();

        for (int startCountX = -mapRadius; startCountX < mapRadius; startCountX++) {
            for (int startCountZ = -mapRadius; startCountZ < mapRadius; startCountZ++) {
                currentPlot++;
                if (amountOfPlots > interval && Math.floorMod(currentPlot, (amountOfPlots / interval)) == 0) {
                    EllirionCore.getINSTANCE().getLogger().info("Progress: " + currentPlot + " / " + amountOfPlots);
                }

                PlotCoord plotCoord = new PlotCoord(gameID, startCountX, startCountZ, world.getName());

                try {
                    //If plot already exist skip it.
                    if (SAVED_PLOTS.get(plotCoord) == null) {
                        String name = plotCoord.toString();
                        int currentX = startCountX * PLOT_SIZE + mapCenterX;
                        int currentZ = startCountZ * PLOT_SIZE + mapCenterZ;

                        Point lowerPoint = new Point(currentX, LOWEST_Y, currentZ);
                        Point highestPoint = new Point(currentX + PLOT_SIZE - 1, HIGHEST_Y,
                                                       currentZ + PLOT_SIZE - 1);
                        BoundingBox boundingBox = new BoundingBox(lowerPoint, highestPoint);

                        result.add(new Plot(name, plotCoord, boundingBox, PLOT_SIZE));
                    }
                } catch (Exception e) {
                    Logging.printStackTrace(e);
                    return new ArrayList<>();
                }
            }
        }
        return result;
    }

    /**
     * This method assigns the trading center plots that where saved in the database.
     * @param tradingCenterDBModel The database model that stores the trading center owned plots.
     */
    public static void assignTradingCenterPlots(TradingCenterDBModel tradingCenterDBModel) {
        tradingCenterDBModel.getOwnedPlots().forEach(
                plotCoord -> PlotManager.getPlotByCoordinate(plotCoord).setOwner(TradingCenter.getInstance()));
    }

    /**
     * Get a plot map.
     * @param center the center plot of the plot map
     * @param radius the radius of the plot map
     * @param owner the owner that generates the plot map
     * @return the plot map
     */
    public static String getPlotMap(Plot center, int radius, PlotOwner owner) {
        StringBuilder builder = new StringBuilder();
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (i == 0 && j == 0) {
                    builder.append(ChatColor.WHITE).append('O');
                    continue;
                }

                PlotCoord coord = center.getPlotCoord().translate(j, i);
                Plot plot = getPlotByCoordinate(coord);

                //Get color + symbol for plot
                if (plot != null) {
                    Tuple<ChatColor, Character> chatData = getPlotMapSymbol(owner, plot);
                    builder.append(chatData.a()).append(chatData.b());
                } else {
                    builder.append(ChatColor.BLACK).append('#');
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * Remove all plots.
     */
    public static void removeAllPlots() {
        SAVED_PLOTS.clear();
    }

    private static Tuple<ChatColor, Character> getPlotMapSymbol(PlotOwner a, Plot b) {
        if (b.getOwner().equals(Wilderness.getInstance())) {
            return new Tuple(ChatColor.GRAY, '#');
        }
        if (b.getOwner().equals(TradingCenter.getInstance())) {
            return new Tuple(ChatColor.GOLD, '$');
        }
        if (a.equals(b.getOwner())) {
            return new Tuple(ChatColor.GREEN, '+');
        }

        return new Tuple(((Race) b.getOwner()).getTeamColor(), '-');
    }
}

