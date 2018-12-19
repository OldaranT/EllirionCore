package com.ellirion.core.database.util;

import lombok.Getter;
import xyz.morphia.annotations.Embedded;

import java.util.UUID;

@Embedded
public class PlayerDBKey {

    @Getter private UUID gameID;
    @Getter private UUID playerID;

    /**
     * default constructor.
     */
    public PlayerDBKey() {
        // empty on purpose.
    }

    /**
     * This is the key for the playerDBModel.
     * @param gameID The game ID.
     * @param playerID The player ID.
     */
    public PlayerDBKey(final UUID gameID, final UUID playerID) {
        this.gameID = gameID;
        this.playerID = playerID;
    }
}
