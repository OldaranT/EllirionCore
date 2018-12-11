package com.ellirion.core.groundwar;

import com.ellirion.core.EllirionCore;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.Logging;
import com.ellirion.util.EllirionUtil;
import com.ellirion.util.async.Promise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

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
            war.setRaceB((Race) plot.getOwner());
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
        GroundWar war = GROUND_WARS.get(player);
        Race race = PlayerManager.getPlayerRace(player);
        if (plot.getOwner().equals(race)) {
            Plot opponentsPlot = war.getPlotB();
            if (opponentsPlot != null) {
                return Arrays.asList(opponentsPlot.getNeighbours()).contains(plot);
            }
            return true;
        }

        if (war != null) {
            Plot ownPlot = war.getPlotA();
            if (ownPlot == null) {
                return true;
            }
            Plot[] neighbours = ownPlot.getNeighbours();
            for (Plot p : neighbours) {
                if (p.equals(plot)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get the ground war of a player.
     * @param playerId the player whose ground war to get
     * @return the ground war of the player
     */
    public static GroundWar getGroundWar(UUID playerId) {
        if (GROUND_WARS.containsKey(playerId)) {
            return GROUND_WARS.get(playerId);
        } else {
            for (GroundWar war : GROUND_WARS.values()) {
                if (war.containsParticipant(playerId)) {
                    return war;
                }
            }
        }
        return null;
    }

    /**
     * Get a GroundWar based on plot.
     * @param plot the plot in the ground war
     * @return the ground war the plot is in
     */
    public static GroundWar getGroundWar(Plot plot) {
        for (GroundWar war : GROUND_WARS.values()) {
            if (war.getPlotB().equals(plot) || war.getPlotA().equals(plot)) {
                return war;
            }
        }

        return null;
    }

    /**
     * Confirm a ground war.
     * @param war the war to confirm
     */
    public static void confirmGroundWar(GroundWar war) {
        war.setState(GroundWar.State.CONFIRMED);
        Promise countdownPromise = new Promise<Boolean>(f -> {
            Set<UUID> players = war.getRaceA().getPlayers();
            players.addAll(war.getRaceB().getPlayers());
            int totalWaitTime = 60000;

            try {
                for (int i = 0; i < 10; i++) {
                    for (UUID id : players) {
                        EllirionCore.getINSTANCE().getServer().getPlayer(id).sendMessage(
                                "Ground war between races " + war.getRaceA().getName() + " and " +
                                war.getRaceB().getName() + " is starting in " +
                                ((totalWaitTime - (i * totalWaitTime / 10)) / 1000) + " seconds");
                    }
                    Thread.sleep(totalWaitTime / 10);
                }
            } catch (Exception exception) {
                Logging.printStackTrace(exception);
            }
            for (UUID id : players) {
                EllirionCore.getINSTANCE().getServer().getPlayer(id).sendMessage("The ground war is now starting");
            }
            f.resolve(true);
        }, true);

        ((EllirionUtil) EllirionCore.getINSTANCE().getServer().getPluginManager().getPlugin("EllirionUtil")).schedulePromise(countdownPromise).then(f -> {
            EllirionCore.getINSTANCE().getServer().getScheduler().runTask(EllirionCore.getINSTANCE(), () -> {
                Logger.getGlobal().info("Staring GroundWar");
                war.start();
            });
        });

    }

    /**
     * Get the GroundWar a Race is currently participating in.
     * @param race the race whose GroundWar to find
     * @return either the GroundWar that race is participating in, or null
     */
    public static GroundWar findGroundWarFromRace(Race race) {
        for (GroundWar war : GROUND_WARS.values()) {
            if (war.getRaceA().equals(race) || war.getRaceB().equals(race)) {
                return war;
            }
        }
        return null;
    }

    /**
     * Remove a ground war.
     * @param player the player whose ground war to remove
     */
    public static void removeGroundWar(UUID player) {
        GROUND_WARS.remove(player);
    }
}
