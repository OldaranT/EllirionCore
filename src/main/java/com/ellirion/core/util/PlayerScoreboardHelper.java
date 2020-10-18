package com.ellirion.core.util;

import com.ellirion.core.util.model.PlayerScoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerScoreboardHelper {
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
}
