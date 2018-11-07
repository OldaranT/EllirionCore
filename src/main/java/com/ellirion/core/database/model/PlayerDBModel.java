package com.ellirion.core.database.model;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.race.model.Race;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(value = "Player", noClassnameStored = true)
public class PlayerDBModel {

    @Id
    @Indexed
    @Getter private UUID playerID;

    @Getter private String ip;

    @Getter private UUID raceID;

    @Property("ip_history")
    private Set<String> ipHistory = new HashSet<>();

    /**
     * This class is the database object for the player data.
     * @param player The player who owns this data.
     * @param raceID the player race.
     */
    public PlayerDBModel(final Player player, final UUID raceID) {
        playerID = player.getUniqueId();
        ip = player.getAddress().getHostName();
        this.raceID = raceID;
        ipHistory.add(ip);
    }

    /**
     * This is an alternate constructor which uses the player data and the player.
     * @param data The player data that the game uses.
     * @param player The player that is going to be saved.
     */
    public PlayerDBModel(final PlayerData data, final Player player) {
        playerID = player.getUniqueId();
        ip = player.getAddress().getHostName();
        Race race = data.getRace();
        setRaceID(race);
        ipHistory.add(ip);
    }

    /**
     * This constructor is used by morphia.
     */
    public PlayerDBModel() {
        // empty on purpose.
    }

    /**
     * @param ip the current ip
     */
    public void setIp(String ip) {
        ipHistory.add(ip);
        this.ip = ip;
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
        setRaceID(data.getRace());
        setIp(player.getAddress().getHostName());
    }

    private void setRaceID(Race race) {
        if (race == null) {
            raceID = null;
        } else {
            raceID = race.getRaceUUID();
        }
    }
}
