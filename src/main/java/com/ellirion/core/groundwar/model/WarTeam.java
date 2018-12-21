package com.ellirion.core.groundwar.model;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarTeam {

    @Getter private List<UUID> players;
    @Getter private List<Participant> participants;
    @Getter private int lives;
    @Getter private UUID captain;
    @Getter private String name;

    /**
     * constructor.
     * @param name the name of the team
     */
    public WarTeam(final String name) {
        players = new ArrayList<>();
        participants = new ArrayList<>();
        lives = -1;
    }

    /**
     * Add a playerUUID to this WarTeam.
     * @param playerUUID the playerUUID to add
     */
    public void addPlayer(UUID playerUUID) {
        if (!players.contains(playerUUID)) {
            players.add(playerUUID);
        }

        Player player = EllirionCore.getINSTANCE().getServer().getPlayer(playerUUID);
        Location loc = player.getLocation();
        participants.add(new Participant(playerUUID, player.getDisplayName(), loc));
    }

    /**
     * Get a list of all participants' names.
     * @return all the participants' names as a string
     */
    public String getPlayerNames() {
        if (players.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        ChatColor raceColor = PlayerManager.getPlayerRace(players.get(0)).getTeamColor();
        for (UUID player : players) {
            builder.append('-').append(raceColor).append(
                    EllirionCore.getINSTANCE().getServer().getOfflinePlayer(player).getName()).append(
                    ChatColor.RESET).append('\n');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * Set the initial lives of a team. (note: this only works once)
     * @param lives the amount of lives the team has
     */
    public void setInitialLives(int lives) {
        if (this.lives == -1 && lives > 0) {
            this.lives = lives;
        }
    }

    /**
     * Remove a life from the total.
     */
    public void removeLife() {
        if (lives > 0) {
            lives--;
        }
    }

    /**
     * Removes all lives of a team.
     */
    public void removeAllLives() {
        lives = 0;
    }

    /**
     * Make a copy of this WarTeam.
     * @return a copy of this WarTeam
     */
    public WarTeam copy() {
        WarTeam other = new WarTeam(name);

        for (Participant participant : participants) {
            other.participants.add(
                    new Participant(participant.getPlayer(), participant.getDisplayName(),
                                    participant.getRespawnLocationAfterGroundWar().clone()));
        }
        other.players.addAll(players);
        other.captain = captain;
        other.lives = lives;

        return other;
    }

    /**
     * Choose a random captain from the list of players.
     */
    public void chooseCaptain() {
        captain = players.get((int) (Math.random() * players.size()));
    }
}
