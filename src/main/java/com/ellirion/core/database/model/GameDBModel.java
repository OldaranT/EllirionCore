package com.ellirion.core.database.model;

import lombok.Getter;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.IndexOptions;
import xyz.morphia.annotations.Indexed;
import com.ellirion.core.gamemanager.model.Game;
import com.ellirion.core.util.Logging;

import java.util.UUID;

@Entity(value = "Game", noClassnameStored = true)
public class GameDBModel {

    @Id @Indexed @Getter private UUID gameID;
    @Indexed(options = @IndexOptions(unique = true)) @Getter private String uName;
    @Getter private int centerOffsetX;
    @Getter private int centerOffsetZ;
    @Getter private int plotSize;

    /**
     * Default constructor used by morphia.
     */
    public GameDBModel() {
        // this is intentionally empty.
    }

    /**
     * An overloaded version of the constructor that can use a game instead of multiple variables.
     * @param game The game that needs to be saved.
     */
    public GameDBModel(final Game game) {
        gameID = game.getGameID();
        uName = game.getUName();
        centerOffsetX = game.getCenterOffsetX();
        centerOffsetZ = game.getCenterOffsetZ();
        plotSize = game.getPlotSize();
    }

    /**
     * This class is the database object for the game data.
     * @param gameID the ID of the game.
     * @param uName the unique name of the game.
     * @param centerOffsetX the plot offset of the x-axis.
     * @param centerOffsetZ the plot offset of the z-axis.
     * @param plotSize the size of the plots.
     */
    public GameDBModel(final UUID gameID, final String uName, final int centerOffsetX, final int centerOffsetZ,
                       final int plotSize) {
        this.gameID = gameID;
        this.uName = uName;
        this.centerOffsetX = centerOffsetX;
        this.centerOffsetZ = centerOffsetZ;
        this.plotSize = plotSize;
    }

    /**
     * This updates the database game with the data from the game.
     * @param game The game that should be copied to the DB.
     * @return Return true to signal that the operation succeeded.
     */
    public boolean update(Game game) {
        try {
            uName = game.getUName();
            return true;
        } catch (Exception exception) {
            Logging.printStackTrace(exception);
            return false;
        }
    }
}
