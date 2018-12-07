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


    public GroundWarResults(UUID createdBy){
        started = Date.from(Instant.now());
        this.createdBy = createdBy;
    }

    public void setEnded() {
        ended = Date.from(Instant.now());
    }
}
