package com.ellirion.core.database.model;

import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import com.ellirion.core.race.model.Race;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(value = "Race", noClassnameStored = true)
public class RaceDBModel {

    @Id
    @Indexed
    @Getter private UUID raceID;

    @Getter @Setter private String raceName;

    @Property(value = "players")
    @Getter private Set<UUID> players;

    @Property(value = "color")
    @Getter @Setter private String color;

    /**
     * This class is the database object for the race data.
     * @param raceID The UUID of the race.
     * @param raceName Name of the race.
     * @param players The players in the team.
     * @param color The color of the team.
     */
    public RaceDBModel(final UUID raceID, final String raceName, final Set<UUID> players, final String color) {
        this.raceID = raceID;
        this.raceName = raceName;
        this.players = players;
        this.color = color;
    }

    /**
     * An overloaded version of the constructor that can use a race instead of multiple variables.
     * @param race The race that needs to be saved.
     */
    public RaceDBModel(final Race race) {
        raceID = race.getRaceUUID();
        raceName = race.getName();
        players = race.getPlayers();
        color = race.getTeamColor().toString();
    }

    /**
     * An overloaded constructor for when you don't have players.
     * @param raceID The UUID of the race.
     * @param raceName Name of the race.
     * @param color The color of the team.
     */
    public RaceDBModel(final UUID raceID, final String raceName, final String color) {
        this(raceID, raceName, new HashSet<>(), color);
    }

    /**
     * @param playerID The player ID.
     * @return return true if successful.
     */
    public boolean removePlayer(UUID playerID) {
        return players.remove(playerID);
    }

    /**
     * This updates the database race with the data from the game race.
     * @param race The race that should be copied to the DB.
     * @return Return true to signal that the operation succeeded.
     */
    public boolean update(Race race) {
        raceName = race.getName();
        players = race.getPlayers();
        color = race.getTeamColor().toString();
        return true;
    }
}