package com.ellirion.core.playerdata;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.model.PlayerModel;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private static HashMap<UUID, PlayerModel> PLAYERS = new HashMap<>();

    /**
     * @param p The player.
     * @param raceName The name of the race.
     * @param rank The player rank.
     * @param cash The player cash.
     * @return return a boolean that indicates if it was a success.
     */
    public static boolean newPlayer(Player p, String raceName, String rank, int cash) {
        PlayerModel d = new PlayerModel(p.getUniqueId(), raceName, rank, cash);
        //RaceManager.addPlayerToRace(p, raceName);
        UUID id = p.getUniqueId();
        PLAYERS.putIfAbsent(id, d);
        //dbHandler.saveUser(d, p);
        return true;
    }

    /**
     * @param p The player.
     * @return Return a boolean whether the player exists.
     */
    public static boolean playerexists(Player p) {
        return PLAYERS.containsKey(p.getUniqueId());
    }

    private static PlayerModel getPlayerData(Player p) {
        if (!playerexists(p)) {
            return null;
        }
        return PLAYERS.get(p.getUniqueId());
    }

    /**
     * @param p The player.
     * @return return true if it was a success.
     */
    public static boolean updatePlayer(Player p) {
        //        PlayerModel d = PLAYERS.get(p.getUniqueId());
        //        dbHandler.saveUser(d, p);
        //        return true;
        throw new NotImplementedException();
    }

    //    public static boolean setPlayerRace(Player p, String raceName) {
    //        if (!(playerexists(p))) {
    //            return false;
    //        }
    //
    //        if (!(RaceManager.movePlayerToRace(p, getPlayerRace(p), raceName))) {
    //            return false;
    //        }
    //        return getPlayerData(p).setRace(r);
    //    }

    private static String getPlayerRace(Player p) {
        if (!playerexists(p)) {
            return null;
        }
        return getPlayerData(p).getRace();
    }

    /**
     * @param p The player.
     * @return return the player rank.
     */
    public static String getPlayerRank(Player p) {
        if (!playerexists(p)) {
            return null;
        }
        return getPlayerData(p).getRank();
    }

    /**
     * @param p The player.
     * @return return the player cash amount.
     */
    public static int gerPlayerCash(Player p) {
        if (!playerexists(p)) {
            return -1;
        }
        return getPlayerData(p).getCash();
    }
}
