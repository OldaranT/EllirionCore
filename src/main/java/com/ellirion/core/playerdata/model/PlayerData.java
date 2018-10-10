package com.ellirion.core.playerdata.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class PlayerData {

    @Getter private UUID playerID;
    @Getter @Setter private String race;
    @Getter @Setter private String rank;
    @Getter @Setter private int cash;

    /**
     * @param playerID UUID of the player.
     * @param race the name of the player race.
     * @param rank the player rank.
     * @param cash the player cash.
     */
    public PlayerData(final UUID playerID, final String race, final String rank, final int cash) {
        this.playerID = playerID;
        this.race = race;
        this.rank = rank;
        this.cash = cash;
    }

    /**
     * @param change the amount that the cash needs to be changed by.
     * @return return whether the change was possible.
     */
    public boolean changeCash(int change) {
        if (change < 0 && (cash + change) < 0) {
            return false;
        }
        cash += change;
        return true;
    }
}
