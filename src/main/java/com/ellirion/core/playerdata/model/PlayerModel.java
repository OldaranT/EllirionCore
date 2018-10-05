package com.ellirion.core.playerdata.model;

import java.util.UUID;

public class PlayerModel {

    private UUID playerID;
    private String raceName;
    private String rank;
    private int cash;

    /**
     * @param playerID UUID of the player.
     * @param raceName the name of the player race.
     * @param rank the player rank.
     * @param cash the player cash.
     */
    public PlayerModel(final UUID playerID, final String raceName, final String rank, final int cash) {
        this.playerID = playerID;
        this.raceName = raceName;
        this.rank = rank;
        this.cash = cash;
    }

    public UUID getPlayer() {
        return playerID;
    }

    public String getRace() {
        return raceName;
    }

    /**
     * @param raceName the name of the race.
     * @return whether it succeeded or not.
     */
    public boolean setRace(String raceName) {
        this.raceName = raceName;
        return true;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
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

    @Override
    public String toString() {
        String s = "";
        s += playerID + " ";
        s += raceName + " ";
        s += rank + " ";
        s += cash + " ";
        return s;
    }
}
