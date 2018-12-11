package com.ellirion.core.groundwar.model;

import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class GroundWarResults {
    private Date started;
    private Date ended;
    private UUID createdBy;
    @Setter private WarTeam[] initialTeams;
    @Setter private WarTeam winner;
    @Setter private WarTeam loser;

    /**
     * Constructor for Ground War Results.
     * @param createdBy whoever created the GroundWar
     */
    public GroundWarResults(final UUID createdBy) {
        started = Date.from(Instant.now());
        this.createdBy = createdBy;
    }

    /**
     * Set the end time of the Results to the current time.
     */
    public void setEnded() {
        ended = Date.from(Instant.now());
    }
}
