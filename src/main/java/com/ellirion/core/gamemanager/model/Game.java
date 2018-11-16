package com.ellirion.core.gamemanager.model;

import lombok.Getter;

import java.util.UUID;

public class Game {

    @Getter private final UUID gameID;
    @Getter private final String uName;
    @Getter private final int centerOffsetX;
    @Getter private final int centerOffsetZ;
    @Getter private final int plotSize;

    /**
     * This model is used to save the game id and static values of a game.
     * @param gameID the unique ide of the game.
     * @param uName a unique name to refer to the game.
     * @param centerOffsetX the plots offset of the x-axis.
     * @param centerOffsetZ the plots offset of the z-axis.
     * @param plotSize the size of all plots of this game.
     */
    public Game(final UUID gameID, final String uName, final int centerOffsetX, final int centerOffsetZ,
                final int plotSize) {
        //Prevent the overriding of the database value.
        if (gameID == null) {
            this.gameID = UUID.randomUUID();
        } else {
            this.gameID = gameID;
        }

        this.uName = uName;
        this.centerOffsetX = centerOffsetX;
        this.centerOffsetZ = centerOffsetZ;
        this.plotSize = plotSize;
    }
}
