package com.ellirion.core.playerdata.model;

import lombok.Getter;
import lombok.Setter;
import com.ellirion.core.database.model.PlayerDBModel;
import com.ellirion.core.race.RaceHelper;
import com.ellirion.core.race.model.Race;

import java.util.UUID;

public class PlayerData {

    @Getter private UUID playerID;
    @Getter @Setter private Race race;

    /**
     * The constructor for when the player hasn't selected a race yet.
     * @param playerID The UUID of the player.
     */
    public PlayerData(final UUID playerID) {
        this.playerID = playerID;
    }

    /**
     * This constructor transforms the database model into a player data object.
     * @param player The player that is retrieved from the database.
     */
    public PlayerData(final PlayerDBModel player) {
        this(player.getPlayerDBKey().getPlayerID(), RaceHelper.getRaceByID(player.getRaceID()));
    }

    /**
     * @param playerID UUID of the player.
     * @param race the player race.
     */
    public PlayerData(final UUID playerID, final Race race) {
        this(playerID);
        this.race = race;
    }
}
