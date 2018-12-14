package com.ellirion.core.groundwar.model;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.race.model.Race;
import com.ellirion.util.EllirionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroundWar {

    @Getter private Race raceA;
    @Getter private Race raceB;
    private Plot[] plots;
    @Getter private UUID createdBy;
    @Getter private State state;
    private WarTeam[] teams;
    private GroundWarResults results;

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

        results = new GroundWarResults(createdBy);
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
     * Set race raceA's plot in this groundwar.
     * @param plot race raceA's plot.
     */
    public void setPlotA(Plot plot) {
        plots[0] = plot;
    }

    /**
     * Get race raceB's plot.
     * @return race raceB's plot
     */
    public Plot getPlotB() {
        return plots[1];
    }

    /**
     * Set race raceB's plot in the groundwar.
     * @param plot race raceB's plot
     */
    public void setPlotB(Plot plot) {
        plots[1] = plot;
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
     * Remove a participant from this GroundWar.
     * @param player the player to remove
     */
    public void removeParticipant(UUID player) {
        List<Participant> participants = getTeam(player).getParticipants();
        Participant toRemove = null;
        for (Participant p : participants) {
            if (p.getPlayer().equals(player)) {
                toRemove = p;
            }
        }
        participants.remove(toRemove);

        teams[0].getPlayers().remove(player);
        teams[1].getPlayers().remove(player);
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

        EllirionUtil ellirionUtil = (EllirionUtil) EllirionCore.getINSTANCE().getServer().getPluginManager().getPlugin(
                "EllirionUtil");

        //Teleport players
        for (UUID playerID : participantsA) {
            Player player = plugin.getServer().getPlayer(playerID);
            Location loc = plots[0].getCenterLocation(world, player.getLocation().getYaw(),
                                                      player.getLocation().getPitch());
            ellirionUtil.loadChunk(loc);
            player.teleport(loc);
        }
        for (UUID playerID : participantsB) {
            Player player = plugin.getServer().getPlayer(playerID);
            Location loc = plots[1].getCenterLocation(world, player.getLocation().getYaw(),
                                                      player.getLocation().getPitch());
            ellirionUtil.loadChunk(loc);
            player.teleport(loc);
        }

        //Calculate team lives
        //less players = more lives so that 0.5 * the players = 1.5 * the lives
        int livesBase = 3;
        int livesTeamA = (int) ((1f / ((float) teams[0].getPlayers().size() / (float) teams[1].getPlayers().size())) *
                                livesBase);
        int livesTeamB = (int) ((1f / ((float) teams[1].getPlayers().size() / (float) teams[0].getPlayers().size())) *
                                livesBase);
        teams[0].setInitialLives(livesTeamA);
        teams[1].setInitialLives(livesTeamB);
        teams[0].chooseCaptain();
        teams[1].chooseCaptain();

        results.setInitialTeams(copyTeams());

        state = State.IN_PROGRESS;
    }

    private WarTeam[] copyTeams() {
        WarTeam[] copies = new WarTeam[2];
        copies[0] = teams[0].copy();
        copies[1] = teams[1].copy();
        return copies;
    }

    private List<UUID> getParticipants() {
        List<UUID> participants = new ArrayList<>();
        participants.addAll(teams[0].getPlayers());
        participants.addAll(teams[1].getPlayers());
        return participants;
    }

    /**
     * Register a player death.
     * @param playerID the player that died
     */
    public void playerDied(UUID playerID) {
        if (teams[0].getPlayers().contains(playerID)) {
            if (teams[0].getCaptain().equals(playerID)) {
                teams[0].removeAllLives();
            } else {
                teams[0].removeLife();
            }
        } else if (teams[1].getPlayers().contains(playerID)) {
            if (teams[1].getCaptain().equals(playerID)) {
                teams[1].removeAllLives();
            } else {
                teams[1].removeLife();
            }
        }

        if (teams[0].getParticipants().size() <= 0 || teams[1].getParticipants().size() <= 0) {
            finish();
        }
    }

    /**
     * ToString.
     * @return string
     */
    @Override
    public String toString() {
        Server server = EllirionCore.getINSTANCE().getServer();
        if (state != State.IN_PROGRESS) {
            StringBuilder sb = new StringBuilder(147);
            String undefined = "Undefined";

            String raceAName = raceA != null ? raceA.getTeamColor() + raceA.getName() + ChatColor.RESET : undefined;
            String raceBName = raceB != null ? raceB.getTeamColor() + raceB.getName() + ChatColor.RESET : undefined;
            String plotA = plots[0] != null ? raceA.getTeamColor() + plots[0].getName() : undefined;
            String plotB = plots[1] != null ? raceB.getTeamColor() + plots[1].getName() : undefined;
            String participantsA = teams[0] != null ? teams[0].getPlayerNames() : "No participants";
            String participantsB = teams[1] != null ? teams[1].getPlayerNames() : "No participants";

            sb.append("GroundWar between races ").append(raceAName)
                    .append(" and ").append(raceBName)
                    .append(" created by ").append(server.getPlayer(createdBy).getDisplayName())
                    .append("\ncurrent state: ").append(state.name())
                    .append("\nPlots wagered in this ground war:\n")
                    .append(plotA).append('\n')
                    .append(plotB).append(ChatColor.RESET)
                    .append("\nWith participants:\n")
                    .append(raceAName).append('\n').append(participantsA).append("\n==========VS.==========\n")
                    .append(raceBName).append('\n').append(participantsB);
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            //Show teams' captain and lives
            sb.append(raceA.getTeamColor()).append(raceA.getName()).append(": \n")
                    .append(raceA.getTeamColor()).append("Captain: ").append(
                    server.getPlayer(teams[0].getCaptain()).getDisplayName()).append(ChatColor.RESET).append('\n')
                    .append(raceA.getTeamColor()).append(teams[0].getLives()).append(" lives remaining").append(
                    ChatColor.RESET).append('\n')
                    .append(raceB.getTeamColor()).append(raceB.getName()).append(": \n")
                    .append(raceB.getTeamColor()).append("Captain: ").append(
                    server.getPlayer(teams[1].getCaptain()).getDisplayName()).append(ChatColor.RESET).append('\n')
                    .append(teams[1].getLives()).append(" lives remaining").append(ChatColor.RESET);
            return sb.toString();
        }
    }

    /**
     * Get the winner of the ground war.
     * @return the team that had more lives
     */
    public Race getWinner() {
        if (teams[0].getParticipants().isEmpty()) {
            return raceB;
        } else {
            return raceA;
        }
    }

    private void finish() {
        //Finish the war
        state = State.FINISHED;

        results.setEnded();

        //Announce winner
        Race winner = getWinner();

        EllirionCore.getINSTANCE().getServer().broadcastMessage(winner.getName() + " has won the ground war!");

        if (teams[0].getParticipants().isEmpty()) {
            results.setWinner(teams[1].copy());
            results.setLoser(teams[0].copy());
        } else if (teams[1].getParticipants().isEmpty()) {
            results.setWinner(teams[0].copy());
            results.setLoser(teams[1].copy());
        }

        //Give plots to winner
        for (Plot plot : plots) {
            plot.setOwner(winner);
        }

        //Teleport players back
        List<Participant> participants = new ArrayList<>();
        participants.addAll(teams[0].getParticipants());
        participants.addAll(teams[1].getParticipants());
        for (Participant p : participants) {
            Player player = EllirionCore.getINSTANCE().getServer().getPlayer(p.getPlayer());
            Location loc = p.getRespawnLocationAfterGroundWar();
            EllirionCore.getINSTANCE().getLogger().info(
                    "Teleporting player " + player.getDisplayName() + " back to " + loc.toString());
            player.teleport(loc);
        }

        //TODO Save GroundWar to database

        //String warResult = results.toString();
        //Broadcast a report of the results.
        EllirionCore.getINSTANCE().getServer().broadcastMessage(results.toString());
        
        //Remove ground war
        GroundWarManager.removeGroundWar(createdBy);
    }

    /**
     * Get the team a player is in.
     * @param playerID the player to check the team of
     * @return the team the player is in, or null if the player is not in this groundwar
     */
    public WarTeam getTeam(UUID playerID) {
        if (teams[0].getPlayers().contains(playerID)) {
            return teams[0];
        } else if (teams[1].getPlayers().contains(playerID)) {
            return teams[1];
        }
        return null;
    }

    public enum State {
        SETUP,
        CONFIRMED,
        IN_PROGRESS,
        FINISHED
    }
}