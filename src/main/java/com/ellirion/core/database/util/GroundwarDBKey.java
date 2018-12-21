package com.ellirion.core.database.util;

import lombok.Getter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class GroundwarDBKey {

    @Getter private Date started;
    @Getter private Date ended;
    @Getter private UUID createdBy;
    @Getter private UUID gameID;

    /**
     * This is the constructor used by morphia.
     */
    public GroundwarDBKey() {
        // empty on purpose.
    }

    /**
     * This is the key for the groundwar in the database.
     * @param started The date that the game started.
     * @param ended The date that the game ended.
     * @param createdBy The UUID of the player that created the groundwar.
     * @param gameID The ID of the game.
     */
    public GroundwarDBKey(final Date started, final Date ended, final UUID createdBy, final UUID gameID) {
        this.started = started;
        this.ended = ended;
        this.createdBy = createdBy;
        this.gameID = gameID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(started, ended, createdBy, gameID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroundwarDBKey that = (GroundwarDBKey) o;
        return Objects.equals(started, that.started) &&
               Objects.equals(ended, that.ended) &&
               Objects.equals(createdBy, that.createdBy) &&
               Objects.equals(gameID, that.gameID);
    }
}
