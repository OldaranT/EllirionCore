package com.ellirion.core.groundwar.model;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.model.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroundWar {

    public enum State {
        SETUP,
        CONFIRMED,
        IN_PROGRESS,
        FINISHED
    }

    @Getter private Race raceA;
    @Getter private Race raceB;
    private Plot[] plots;
    private UUID createdBy;
    private State state;
    private WarTeam[] teams;

    /**
     * Create raceA ground war.
     * @param createdBy the player that created the ground war
     * @param raceA the race of the player that created the ground war
     */
    public GroundWar(final UUID createdBy, final Race raceA) {
        this.createdBy = createdBy;
        this.raceA = raceA;

        plots = new Plot[2];
        state = State.SETUP;

        teams = new WarTeam[2];
        teams[0] = new WarTeam();
        teams[1] = new WarTeam();

        teams[0].addPlayer(createdBy);
    }

    /**
     * Set race raceA's plot in this groundwar.
     * @param plot race raceA's plot.
     */
    public void setPlotA(Plot plot) {
        plots[0] = plot;
    }

    /**
     * Set race raceB's plot in the groundwar.
     * @param plot race raceB's plot
     */
    public void setPlotB(Plot plot) {
        plots[1] = plot;
    }

    /**
     * Set the opponent Race.
     * @param race the opponent race
     */
    public void setRaceB(Race race) {
        raceB = race;
    }

    /**
     * Get race raceA's plot.
     * @return race raceA's plot
     */
    public Plot getPlotA() {
        return plots[0];
    }

    /**
     * Get race raceB's plot.
     * @return race raceB's plot
     */
    public Plot getPlotB() {
        return plots[1];
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * Add a player to the ground war.
     * @param race the race of the player
     * @param player the player's id
     */
    public void addPlayer(Race race, UUID player) {
        if (race.equals(raceA)) {
            teams[0].addPlayer(player);
        } else if (race.equals(raceB)) {
            teams[1].addPlayer(player);
        }
    }

    /**
     * Check whether a player is a participant in this GroundWar.
     * @param playerID the player to check
     * @return whether the given player is in this GroundWar
     */
    public boolean containsParticipant(UUID playerID) {
        for (UUID player : getParticipants()) {
            if (player.equals(playerID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Start the ground war.
     * Teleports the players.
     */
    public void start() {
        EllirionCore plugin = EllirionCore.getINSTANCE();
        World world = plugin.getServer().getWorld(plots[0].getPlotCoord().getWorldName());
        List<UUID> participantsA = teams[0].getPlayers();
        List<UUID> participantsB = teams[1].getPlayers();

        //Teleport players
        for (UUID playerID : participantsA) {
            Player player = plugin.getServer().getPlayer(playerID);
            player.teleport(plots[0].getCenterLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch()));
        }
        for (UUID playerID : participantsB) {
            Player player = plugin.getServer().getPlayer(playerID);
            player.teleport(plots[1].getCenterLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch()));
        }

        //Add playerDeath eventlistener
    }

    private List<UUID> getParticipants() {
        List<UUID> participants = new ArrayList<>();
        participants.addAll(teams[0].getPlayers());
        participants.addAll(teams[1].getPlayers());
        return participants;
    }

    /**
     * ToString.
     * @return string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(147);
        String undefined = "Undefined";

        String raceAName = raceA != null ? raceA.getTeamColor() + raceA.getName() + ChatColor.RESET : undefined;
        String raceBName = raceB != null ? raceB.getTeamColor() + raceB.getName() + ChatColor.RESET : undefined;
        String plotA = plots[0] != null ? plots[0].getName() : undefined;
        String plotB = plots[1] != null ? plots[1].getName() :  undefined;
        String participantsA = teams[0] != null ? teams[0].getParticipants() : "No participants";
        String participantsB = teams[1] != null ? teams[1].getParticipants() : "No participants";

        sb.append("GroundWar between races ").append(raceAName)
                .append(" and ").append(raceBName)
                .append(" created by ").append(EllirionCore.getINSTANCE().getServer().getPlayer(createdBy).getDisplayName())
                .append("\ncurrent state: ").append(state.name())
                .append("\nPlots wagered in this ground war:\n")
                .append(plotA).append('\n')
                .append(plotB).append("\nWith participants:\n")
                .append(raceAName).append('\n').append(participantsA).append("\n==========VS.==========\n")
                .append(raceBName).append('\n').append(participantsB);
        return sb.toString();
    }
}
