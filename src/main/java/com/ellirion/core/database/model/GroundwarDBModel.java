package com.ellirion.core.database.model;

import lombok.Getter;
import lombok.Setter;
import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import com.ellirion.core.database.util.GroundwarDBKey;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.groundwar.model.GroundWarResults;
import com.ellirion.core.groundwar.model.WarTeam;
import com.ellirion.core.plotsystem.model.PlotCoord;

import java.util.Date;
import java.util.UUID;

@Entity(noClassnameStored = true, value = "Groundwar")
public class GroundwarDBModel {

    @Id @Getter private GroundwarDBKey key;

    @Getter private WarTeam[] teams;

    @Getter private PlotCoord[] wagered = new PlotCoord[2];

    @Getter @Setter private GroundWarResults result;

    @Getter private GroundWar.State state;

    /**
     * This is the database model for the groundwar.
     * @param started The date that it started.
     * @param ended The date that it ended.
     * @param createdBy The player who created it.
     * @param gameID The ID of the game.
     * @param teams The teams that participated.
     * @param wagered The plots that where wagered.
     * @param state The state of the groundwar when saved.
     */
    public GroundwarDBModel(final Date started, final Date ended, final UUID createdBy, final UUID gameID,
                            final WarTeam[] teams,
                            final PlotCoord[] wagered,
                            final GroundWar.State state) {
        key = new GroundwarDBKey(started, ended, createdBy, gameID);
        this.teams = teams;
        this.wagered = wagered;
        this.state = state;
    }

    /**
     * The database model got the groundwar.
     * @param groundWar The groundwar to be saved.
     * @param gameID The ID of the game.
     */
    public GroundwarDBModel(final GroundWar groundWar, final UUID gameID) {
        key = new GroundwarDBKey(groundWar.getResults().getStarted(), groundWar.getResults().getEnded(),
                                 groundWar.getCreatedBy(), gameID);
        teams = groundWar.getTeams();
        wagered[0] = groundWar.getPlotA().getPlotCoord();
        wagered[1] = groundWar.getPlotB().getPlotCoord();
        result = groundWar.getResults();
        state = groundWar.getState();
    }
}
