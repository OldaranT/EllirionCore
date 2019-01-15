package com.ellirion.core.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.model.PlayerScoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatch.*;

public class PlayerScoreboardManager {

    private static Map<UUID, PlayerScoreboard> PLAYER_SCOREBOARDS = new HashMap();

    /**
     * Add a scoreboard to the manager.
     * @param player the player whose scoreboard it is
     * @param scoreboard the scoreboad of the player
     */
    public static void addPlayerScoreboard(UUID player, PlayerScoreboard scoreboard) {
        PLAYER_SCOREBOARDS.put(player, scoreboard);
    }

    /**
     * Remove player scoreboard.
     * @param player the player whose scoreboard to remove
     */
    public static void removePlayerScoreboard(UUID player) {
        PLAYER_SCOREBOARDS.remove(player);
    }

    /**
     * Get a player's scoreboard.
     * @param player the player whose scoreboard to get
     * @return the player's scoreboard
     */
    public static PlayerScoreboard getPlayerScoreboard(UUID player) {
        return PLAYER_SCOREBOARDS.get(player);
    }

    /**
     * Update all scoreboards.
     */
    public static void updateBoards() {
        for (Map.Entry entry : PLAYER_SCOREBOARDS.entrySet()) {
            ((PlayerScoreboard) entry.getValue()).showScoreboard();
        }
    }

    /**
     * Updates scoreboard with current teams info.
     * @param joiningPlayer the joiningPlayer to update scoreboard for.
     */
    public static void updateTeams(Player joiningPlayer) {

        List<Player> playerList = new ArrayList<>(EllirionCore.getINSTANCE().getServer().getOnlinePlayers());

        Scoreboard playerScoreboard = joiningPlayer.getScoreboard();

        for (Player onlinePlayer : playerList) {

            Race onlineRace = PlayerManager.getPlayerRace(onlinePlayer.getUniqueId());

            if (onlineRace == null) {
                return;
            }

            ChatColor onlineRaceTeamColor = onlineRace.getTeamColor();
            Team onlineRaceTeam = playerScoreboard.getTeam(onlineRace.getName());

            //Remove online joiningPlayer from any old teams.
            for (Team team : playerScoreboard.getTeams()) {
                tryCatch(() -> team.removePlayer(onlinePlayer));
            }

            if (onlineRaceTeam == null) {
                onlineRaceTeam = playerScoreboard.registerNewTeam(onlineRace.getName());
            }
            onlineRaceTeam.setPrefix(
                    onlineRaceTeamColor + "" + ChatColor.BOLD + onlineRace.getAlias() + ChatColor.RESET + " | ");
            onlineRaceTeam.addPlayer(onlinePlayer);

            //Now update the online joiningPlayer with the new joining joiningPlayer.
            Scoreboard onlinePlayerScoreboard = onlinePlayer.getScoreboard();
            Race joiningPlayerRace = PlayerManager.getPlayerRace(joiningPlayer.getUniqueId());

            if (joiningPlayerRace == null) {
                return;
            }

            ChatColor joiningRaceColor = joiningPlayerRace.getTeamColor();
            Team joiningPlayerTeam = onlinePlayerScoreboard.getTeam(joiningPlayerRace.getName());

            //Remove joining joiningPlayer from any old teams.
            for (Team team : onlinePlayerScoreboard.getTeams()) {
                tryCatch(() -> team.removePlayer(joiningPlayer));
            }

            if (joiningPlayerTeam == null) {
                joiningPlayerTeam = onlinePlayerScoreboard.registerNewTeam(joiningPlayerRace.getName());
            }

            joiningPlayerTeam.setPrefix(
                    joiningRaceColor + "" + ChatColor.BOLD + joiningPlayerRace.getAlias() + ChatColor.RESET + " | ");
            joiningPlayerTeam.addPlayer(joiningPlayer);
        }
    }
}
