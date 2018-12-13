package com.ellirion.core.playerdata;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.race.model.Race;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private static HashMap<UUID, PlayerData> PLAYERS = new HashMap<>();
    private static EllirionCore INSTANCE = EllirionCore.getINSTANCE();
    private static Server SERVER = INSTANCE.getServer();
    private static DatabaseManager DATABASE_MANAGER = INSTANCE.getDbManager();

    /**
     * This class manages the players and creates new players when needed.
     * @param player The player UUID.
     * @param raceID The UUID of the race.
     * @return Return a boolean that indicates if creating the new player was a success.
     */
    public static boolean newPlayer(Player player, UUID raceID) {
        PlayerData data;
        if (raceID == null) {
            data = new PlayerData(player.getUniqueId());
        } else {
            data = new PlayerData(player.getUniqueId(), RaceManager.getRaceByID(raceID));
            RaceManager.addPlayerToRace(player.getUniqueId(), raceID);
        }
        UUID id = player.getUniqueId();
        PLAYERS.putIfAbsent(id, data);
        savePlayer(player, data);
        return true;
    }

    /**
     * This method checks if the player exists or not.
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
     * This method updates the player in the database.
     * @param playerID The player UUID.
     * @return Return the result of the operation.
     */
    public static boolean updatePlayer(UUID playerID) {
        PlayerData data = PLAYERS.get(playerID);
        Player player = getPlayerByUUIDFromServer(playerID);
        return DATABASE_MANAGER.updatePlayer(data, player);
    }

    private static boolean savePlayer(Player player, PlayerData data) {
        return DATABASE_MANAGER.createPlayer(data, player);
    }

    /**
     * This method sets the player race.
     * @param playerID The UUID from the player that is changing race.
     * @param raceID The UUID from the race the player is changing to.
     * @return Return true if the player changed race.
     */
    public static boolean setPlayerRace(UUID playerID, UUID raceID) {
        Player player = getPlayerByUUIDFromServer(playerID);
        if (!(playerexists(playerID)) && !(newPlayer(player, raceID))) {
            return false;
        }

        if ((getPlayerRaceID(playerID) != null) &&
            !(RaceManager.changePlayerRace(playerID, getPlayerRaceID(playerID), raceID))) {
            return false;
        }
        if ((getPlayerRaceID(playerID) == null) && !(RaceManager.addPlayerToRace(playerID, raceID))) {
            return false;
        }
        PlayerData data = getPlayerData(playerID);
        data.setRace(RaceManager.getRaceByID(raceID));
        DATABASE_MANAGER.updatePlayer(data, player);
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
     * This method gets the race of the specified player.
     * @param playerID The UUID of the player to get the race from.
     * @return Return the player race.
     */
    public static Race getPlayerRace(UUID playerID) {
        PlayerData data = getPlayerData(playerID);
        if (data == null) {
            return null;
        }
        return data.getRace();
    }

    /**
     * This method compares 2 player teams and returns true if they are the same.
     * @param player1 The first player to get the race from.
     * @param player2 The second player to get the race from.
     * @return Return true if the teams are the same.
     */
    public static boolean comparePlayerTeams(UUID player1, UUID player2) {
        UUID raceID1 = PLAYERS.get(player1).getRace().getRaceUUID();
        UUID raceID2 = PLAYERS.get(player2).getRace().getRaceUUID();

        return raceID1.equals(raceID2);
    }

    /**
     * this checks if the player is in the database.
     * @param playerID The id of the player to check.
     * @return true if player exists.
     */
    public static boolean playerExistsInDatabase(UUID playerID) {
        PlayerDBModel p = DATABASE_MANAGER.getPlayer(playerID);
        return p != null;
    }

    /**
     * This function gets the specified player from the server.
     * @param playerID The UUID of the player.
     * @return Return the found player.
     */
    private static Player getPlayerByUUIDFromServer(UUID playerID) {
        return SERVER.getPlayer(playerID);
    }

    /**
     * This function retrieves a player from the database and adds it to the player list.
     * @param playerID The ID of the player to add.
     */
    public static void addPlayerFromDatabase(UUID playerID) {
        PlayerData data = new PlayerData(DATABASE_MANAGER.getPlayer(playerID));
        PLAYERS.putIfAbsent(playerID, data);
    }

    /**
     * This removes the player from the PLAYERS list.
     * @param playerID The ID of the player to remove.
     */
    public static void setPlayerOffline(UUID playerID) {
        updatePlayer(playerID);
        PLAYERS.remove(playerID);
    }
}
