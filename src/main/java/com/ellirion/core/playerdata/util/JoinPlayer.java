package com.ellirion.core.playerdata.util;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.gamemanager.GameManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.ellirion.core.playerdata.PlayerManager.*;

public class JoinPlayer {

    private static Server SERVER = EllirionCore.getINSTANCE().getServer();

    /**
     * This function adds a player to the game.
     * @param playerID The UUID of the player that joins.
     */
    public static void joinPlayer(UUID playerID) {
        Player player = SERVER.getPlayer(playerID);
        UUID gameID = GameManager.getInstance().getGameID();
        if (playerWithGameExistsInDatabase(gameID, playerID)) {
            addPlayerFromDatabase(gameID, playerID);
        } else {
            newPlayer(player, null);
        }
    }

    /**
     * This method gets a list of players from the server and adds all the players that are not yet added to the player list.
     */
    public static void joinPlayers() {
        Set<UUID> players = new HashSet<>();
        Set<UUID> joinedPlayers = getPlayers().keySet();
        SERVER.getOnlinePlayers().iterator().forEachRemaining(
                p -> players.add(p.getUniqueId()));
        players.removeAll(joinedPlayers);
        players.forEach(p -> joinPlayer(p));
    }
}
