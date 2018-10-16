package com.ellirion.core.database.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import com.ellirion.core.playerdata.model.PlayerData;
import com.ellirion.core.races.model.Race;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(value = "Player", noClassnameStored = true)
public class PlayerDBModel {

    @Id
    @Indexed
    @Getter private UUID playerID;

    @Getter private String username;

    @Getter private String ip;

    @Property(value = "money")
    @Getter @Setter private int cash;

    @Getter private UUID raceID;

    @Property(value = "rank")
    @Getter @Setter private String rank;

    @Property("ip_history")
    private Set<String> ipHistory = new HashSet<>();

    @Property("name_history")
    private Set<String> nameHistory = new HashSet<>();

    /**
     * @param player The player who owns this data.
     * @param cash the amount of cash the player has.
     * @param raceID the player race.
     * @param rank the player rank
     */
    public PlayerDBModel(final Player player, final int cash, final UUID raceID,
                         final String rank) {
        playerID = player.getUniqueId();
        username = player.getName();
        ip = player.getAddress().getHostName();
        this.cash = cash;
        this.raceID = raceID;
        this.rank = rank;
        ipHistory.add(ip);
        nameHistory.add(username);
    }

    /**
     * This is an alternate constructor which uses the player data and the player.
     * @param data The player data that the game uses.
     * @param player The player that is going to be saved.
     */
    public PlayerDBModel(final PlayerData data, final Player player) {
        playerID = player.getUniqueId();
        username = player.getName();
        ip = player.getAddress().getHostName();
        cash = data.getCash();
        Race race = data.getRace();
        setRaceID(race);
        rank = data.getRank();
        ipHistory.add(ip);
        nameHistory.add(username);
    }

    /**
     * @param username the current username
     */
    public void setUsername(String username) {
        nameHistory.add(username);
        this.username = username;
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

    public Set<String> getNameHistory() {
        return nameHistory;
    }

    /**
     * This method updates the data in this database model.
     * @param data The playerData that is to be copied to this DB model.
     * @param player The player that this DB model belongs to.
     */
    public void update(PlayerData data, Player player) {
        cash = data.getCash();
        setRaceID(data.getRace());
        rank = data.getRank();
        setIp(player.getAddress().getHostName());
        setUsername(player.getName());
    }

    private void setRaceID(Race race) {
        if (race == null) {
            raceID = null;
        } else {
            raceID = race.getRaceUUID();
        }
    }
}
