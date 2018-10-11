package com.ellirion.core.playerdata;

import org.apache.commons.lang.NotImplementedException;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.races.RaceManager;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private static HashMap<UUID, PlayerData> PLAYERS = new HashMap<>();

    /**
     * @param player The player.
     * @param raceID The name of the race.
     * @param rank The player rank.
     * @param cash The player cash.
     * @return return a boolean that indicates if creating the new player was a success.
     */
    public static boolean newPlayer(UUID player, UUID raceID, String rank, int cash) {
        PlayerData data = new PlayerData(player, raceID, rank, cash);
        if (raceID == null) {
            data.setRace(null);
        } else {
            RaceManager.addPlayerToRace(player, raceID);
            data.setRace(raceID);
        }
        UUID id = player;
        PLAYERS.putIfAbsent(id, data);
        // commented for when the db get's implemented.
        //dbHandler.saveUser(d, p);
        return true;
    }

    /**
     * @param player The player.
     * @return Return a boolean whether the player exists.
     */
    public static boolean playerexists(UUID player) {
        return PLAYERS.containsKey(player);
    }

    private static PlayerData getPlayerData(UUID player) {
        if (!playerexists(player)) {
            return null;
        }
        return PLAYERS.get(player);
    }

    /**
     * @param player The player.
     * @return return true if the update was a success.
     */
    public static boolean updatePlayer(UUID player) {
        // commented until there is a database.
        // PlayerData d = PLAYERS.get(p.getUniqueId());
        // dbHandler.saveUser(d, p);
        // return true;
        throw new NotImplementedException();
    }

    /**
     * @param player The player that is changing race.
     * @param raceID The UUID from the race the player is changing to.
     * @return Return true if the player changed race.
     */
    public static boolean setPlayerRace(UUID player, UUID raceID) {
        if (!(playerexists(player)) && !(newPlayer(player, raceID, "outsider", 0))) {
            return false;
        }

        if ((getPlayerRace(player) != null) && !(RaceManager.movePlayerToRace(player, getPlayerRace(player), raceID))) {
            return false;
        }
        if ((getPlayerRace(player) == null) && !(RaceManager.addPlayerToRace(player, raceID))) {
            getPlayerData(player).setRace(raceID);
        }
        return true;
    }

    private static UUID getPlayerRace(UUID player) {
        if (!playerexists(player)) {
            return null;
        }
        return getPlayerData(player).getRace();
    }

    /**
     * @param player The player.
     * @return return the player rank.
     */
    public static String getPlayerRank(UUID player) {
        if (!playerexists(player)) {
            return null;
        }
        return getPlayerData(player).getRank();
    }

    /**
     * @param player The player.
     * @return return the player cash amount.
     */
    public static int getPlayerCash(UUID player) {
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
        UUID raceID1 = PLAYERS.get(player1).getRace();
        UUID raceID2 = PLAYERS.get(player2).getRace();

        return raceID1.equals(raceID2);
    }
}
