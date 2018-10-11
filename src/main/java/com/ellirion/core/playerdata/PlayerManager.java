package com.ellirion.core.playerdata;

import org.apache.commons.lang.NotImplementedException;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.races.RaceManager;
import com.ellirion.core.races.model.Race;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private static HashMap<UUID, PlayerData> PLAYERS = new HashMap<>();

    /**
     * @param playerID The player UUID.
     * @param raceID The UUID of the race.
     * @param rank The player rank.
     * @param cash The player cash.
     * @return return a boolean that indicates if creating the new player was a success.
     */
    public static boolean newPlayer(UUID playerID, UUID raceID, String rank, int cash) {
        PlayerData data = new PlayerData(playerID, RaceManager.getRaceByID(raceID), rank, cash);
        if (raceID != null) {
            RaceManager.addPlayerToRace(playerID, raceID);
        }
        UUID id = playerID;
        PLAYERS.putIfAbsent(id, data);
        // commented for when the db get's implemented.
        //dbHandler.saveUser(d, p);
        return true;
    }

    /**
     * @param playerID The player UUID.
     * @return Return a boolean whether the player exists.
     */
    public static boolean playerexists(UUID playerID) {
        return PLAYERS.containsKey(playerID);
    }

    private static PlayerData getPlayerData(UUID player) {
        if (!playerexists(player)) {
            return null;
        }
        return PLAYERS.get(player);
    }

    /**
     * @param playerID The player UUID.
     * @return return true if the update was a success.
     */
    public static boolean updatePlayer(UUID playerID) {
        // commented until there is a database.
        // PlayerData d = PLAYERS.get(p.getUniqueId());
        // dbHandler.saveUser(d, p);
        // return true;
        throw new NotImplementedException();
    }

    /**
     * @param playerID The player UUID from the player that is changing race.
     * @param raceID The UUID from the race the player is changing to.
     * @return Return true if the player changed race.
     */
    public static boolean setPlayerRace(UUID playerID, UUID raceID) {
        if (!(playerexists(playerID)) && !(newPlayer(playerID, raceID, "outsider", 0))) {
            return false;
        }

        if ((getPlayerRaceID(playerID) != null) &&
            !(RaceManager.changePlayerRace(playerID, getPlayerRaceID(playerID), raceID))) {
            return false;
        }
        if ((getPlayerRaceID(playerID) == null) && !(RaceManager.addPlayerToRace(playerID, raceID))) {
            getPlayerData(playerID).setRace(RaceManager.getRaceByID(raceID));
        }
        return true;
    }

    private static UUID getPlayerRaceID(UUID playerID) {
        if (!playerexists(playerID)) {
            return null;
        }
        if (getPlayerData(playerID).getRace() == null) {
            return null;
        }
        return getPlayerData(playerID).getRace().getRaceUUID();
    }

    /**
     * Get the player race by payer UUID.
     * @param playerID UUID of the player to be checked.
     * @return Race of the player.
     */
    public static Race getPlayerRace(UUID playerID) {
        if (!playerexists(playerID)) {
            return null;
        }
        return getPlayerData(playerID).getRace();
    }

    /**
     * @param playerID The player UUID.
     * @return return the player rank.
     */
    public static String getPlayerRank(UUID playerID) {
        if (!playerexists(playerID)) {
            return null;
        }
        return getPlayerData(playerID).getRank();
    }

    /**
     * @param playerID The player UUID.
     * @return return the player cash amount.
     */
    public static int getPlayerCash(UUID playerID) {
        if (!playerexists(playerID)) {
            return -1;
        }
        return getPlayerData(playerID).getCash();
    }

    /**
     * @param player1 The first player to get the race from.
     * @param player2 The second player to get the race from.
     * @return Return true if the teams are the same.
     */
    public static boolean comparePlayerTeams(UUID player1, UUID player2) {
        UUID raceID1 = PLAYERS.get(player1).getRace().getRaceUUID();
        UUID raceID2 = PLAYERS.get(player2).getRace().getRaceUUID();

        return raceID1.equals(raceID2);
    }
}
