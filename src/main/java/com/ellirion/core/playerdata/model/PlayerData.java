package com.ellirion.core.playerdata.model;

import lombok.Getter;
import lombok.Setter;
import com.ellirion.core.race.model.Race;

import java.util.UUID;

public class PlayerData {

    @Getter private UUID playerID;
    @Getter @Setter private Race race;

    /**
     * @param playerID UUID of the player.
     * @param race the player race.
     */
    public PlayerData(final UUID playerID, final Race race) {
        this.playerID = playerID;
        this.race = race;
    }
}
