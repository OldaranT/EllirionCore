package com.ellirion.core.database.model;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Property;
import com.ellirion.core.database.util.PlayerDBKey;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.race.model.Race;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(value = "Player", noClassnameStored = true)
public class PlayerDBModel {

    @Id @Embedded @Getter private PlayerDBKey playerDBKey;

    @Getter private UUID raceID;

    @Property("ip_history")
    private Set<String> ipHistory = new HashSet<>();

    /**
     * This constructor is used by morphia.
     */
    public PlayerDBModel() {
        // empty on purpose.
    }

    /**
     * This class is the database object for the player data.
     * @param player The player who owns this data.
     * @param raceID the player race.
     * @param gameID The UUID of the game.
     */
    public PlayerDBModel(final Player player, final UUID raceID, final UUID gameID) {
        playerDBKey = new PlayerDBKey(gameID, raceID);
        String ip = player.getAddress().getHostName();
        this.raceID = raceID;
        ipHistory.add(ip);
    }

    /**
     * This is an alternate constructor which uses the player data and the player.
     * @param data The player data that the game uses.
     * @param player The player that is going to be saved.
     */
    public PlayerDBModel(final PlayerData data, final Player player) {
        playerDBKey = new PlayerDBKey(GameManager.getInstance().getGameID(), player.getUniqueId());
        String ip = player.getAddress().getHostName();
        Race race = data.getRace();
        raceID = retrieveRaceID(race);
        ipHistory.add(ip);
    }

    public Set<String> getIpHistory() {
        return ipHistory;
    }

    /**
     * This method updates the data in this database model.
     * @param data The playerData that is to be copied to this DB model.
     * @param player The player that this DB model belongs to.
     */
    public void update(PlayerData data, Player player) {
        Race race = data.getRace();
        UUID raceID = retrieveRaceID(race);
        this.raceID = raceID;
        ipHistory.add(player.getAddress().getHostName());
    }

    private UUID retrieveRaceID(Race race) {
        if (race == null) {
            return null;
        } else {
            return race.getRaceUUID();
        }
    }
}

