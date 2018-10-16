package com.ellirion.core.database.model;

import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(value = "Player", noClassnameStored = true)
public class PlayerModel {

    @Id
    @Indexed
    private UUID playerID;

    private String username;

    private String ip;

    @Property(value = "Money")
    private int cash;

    @Property(value = "Race")
    private String race;

    @Property(value = "Rank")
    private String rank;

    @Property("ip_history")
    private Set<String> ipHistory = new HashSet<>();

    @Property("name_history")
    private Set<String> nameHistory = new HashSet<>();

    /**
     * @param player The player who owns this data.
     * @param cash the amount of cash the player has.
     * @param race the player race.
     * @param rank the player rank
     */
    public PlayerModel(final Player player, final int cash, final String race,
                       final String rank) {
        playerID = player.getUniqueId();
        username = player.getName();
        ip = player.getAddress().getHostName();
        this.cash = cash;
        this.race = race;
        this.rank = rank;
        ipHistory.add(ip);
        nameHistory.add(username);
    }

    public String getUsername() {
        return username;
    }

    /**
     * @param username the current username
     */
    public void setUsername(String username) {
        nameHistory.add(username);
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    /**
     * @param ip the current ip
     */
    public void setIp(String ip) {
        ipHistory.add(ip);
        this.ip = ip;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public Set<String> getIpHistory() {
        return ipHistory;
    }

    public Set<String> getNameHistory() {
        return nameHistory;
    }
}
