package com.ellirion.core.database.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(value = "Race", noClassnameStored = true)
public class RaceModel {

    @Id
    @Indexed
    private String raceName;

    @Property(value = "Players")
    private Set<UUID> players;

    @Property(value = "Color")
    private String color;

    /**
     * @param raceName Name of the race.
     * @param players The players in the team.
     * @param color The color of the team.
     */
    public RaceModel(final String raceName, final Set<UUID> players, final String color) {
        this.raceName = raceName;
        this.players = players;
        this.color = color;
    }

    /**
     * @param raceName Name of the race.
     * @param color The color of the team.
     */
    public RaceModel(final String raceName, final String color) {
        this(raceName, new HashSet<>(), color);
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @param playerID The player ID.
     * @return return true if successful.
     */
    public boolean removePlayer(UUID playerID) {
        return players.remove(playerID);
    }
}
