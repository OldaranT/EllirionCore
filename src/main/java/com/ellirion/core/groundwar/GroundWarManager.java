package com.ellirion.core.groundwar;

import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.model.Race;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroundWarManager {
    private static Map<UUID, GroundWar> GROUND_WARS = new HashMap();

    /**
     * Add a ground war to the manager.
     * @param player th eplayer that added the ground war
     */
    public static void addGroundWar(UUID player) {
        Race race = PlayerManager.getPlayerRace(player);
        GROUND_WARS.put(player, new GroundWar(player, race));
    }

    /**
     * Add a plot to a ground war.
     * @param player the player adding a plot
     * @param plot the plot to add
     */
    public static void addPlotToGroundWar(UUID player, Plot plot) {
        GroundWar war = GROUND_WARS.get(player);
        Race race = PlayerManager.getPlayerRace(player);
        if (plot.getOwner().equals(race)) {
            war.setPlotA(plot);
        } else {
            war.setPlotB(plot);
        }
    }

    /**
     * Check whether a plot can be added to a ground war.
     * @param player the player trying to add a plot
     * @param plot the plot the player is trying to add
     * @return whether the plot can be added to the ground war
     */
    public static boolean canAddPlot(UUID player, Plot plot) {
        //It can be added if
        //a) your race owns the plot, or
        //b) the plot neighbours your plot
        Race race = PlayerManager.getPlayerRace(player);
        if (plot.getOwner().equals(race)) {
            return true;
        }

        GroundWar war = GROUND_WARS.get(player);
        if (war != null) {
            Plot ownPlot = war.getPlotA();
            Plot[] neighbours = ownPlot.getNeighbours();
            for (Plot p : neighbours) {
                if (p.equals(plot)) {
                    return true;
                }
            }
        }

        return false;
    }
}
