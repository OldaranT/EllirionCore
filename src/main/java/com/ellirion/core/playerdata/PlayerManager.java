package com.ellirion.core.playerdata;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.races.RaceManager;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private static HashMap<UUID, PlayerData> PLAYERS = new HashMap<>();

    /**
     * @param player The player.
     * @param raceName The name of the race.
     * @param rank The player rank.
     * @param cash The player cash.
     * @return return a boolean that indicates if creating the new player was a success.
     */
    public static boolean newPlayer(Player player, String raceName, String rank, int cash) {
        PlayerData d = new PlayerData(player.getUniqueId(), raceName, rank, cash);
        if (raceName == null) {
            d.setRace(null);
        } else {
            RaceManager.addPlayerToRace(player, raceName);
            d.setRace(raceName);
        }
        UUID id = player.getUniqueId();
        PLAYERS.putIfAbsent(id, d);
        // commented for when the db get's implemented.
        //dbHandler.saveUser(d, p);
        return true;
    }

    /**
     * @param player The player.
     * @return Return a boolean whether the player exists.
     */
    public static boolean playerexists(Player player) {
        return PLAYERS.containsKey(player.getUniqueId());
    }

    private static PlayerData getPlayerData(Player player) {
        if (!playerexists(player)) {
            return null;
        }
        return PLAYERS.get(player.getUniqueId());
    }

    /**
     * @param player The player.
     * @return return true if the update was a success.
     */
    public static boolean updatePlayer(Player player) {
        //        PlayerData d = PLAYERS.get(p.getUniqueId());
        //        dbHandler.saveUser(d, p);
        //        return true;
        throw new NotImplementedException();
    }

    /**
     * @param player The player that is changing race.
     * @param raceName The race the player is changing to.
     * @return Return true if the player changed race.
     */
    public static boolean setPlayerRace(Player player, String raceName) {
        if (!(playerexists(player))) {
            return false;
        }

        if (!(RaceManager.movePlayerToRace(player, getPlayerRace(player), raceName))) {
            return false;
        }
        getPlayerData(player).setRace(raceName);
        return true;
    }

    private static String getPlayerRace(Player player) {
        if (!playerexists(player)) {
            return null;
        }
        return getPlayerData(player).getRace();
    }

    /**
     * @param player The player.
     * @return return the player rank.
     */
    public static String getPlayerRank(Player player) {
        if (!playerexists(player)) {
            return null;
        }
        return getPlayerData(player).getRank();
    }

    /**
     * @param player The player.
     * @return return the player cash amount.
     */
    public static int getPlayerCash(Player player) {
        if (!playerexists(player)) {
            return -1;
        }
        return getPlayerData(player).getCash();
    }

    /**
     * @param player1 The first player to get the race from.
     * @param player2 The second player to get the race from.
     * @return Return true if the teams are the same.
     */
    public static boolean comparePlayerTeams(UUID player1, UUID player2) {
        String raceName1 = PLAYERS.get(player1).getRace();
        String raceName2 = PLAYERS.get(player2).getRace();

        return raceName1.equals(raceName2);
    }
}
