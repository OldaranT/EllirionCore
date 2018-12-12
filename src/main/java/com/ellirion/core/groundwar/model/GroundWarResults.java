package com.ellirion.core.groundwar.model;

import lombok.Setter;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;

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

    @Override
    public String toString() {
        String newLine = "\n";
        String spacer = "   ";
        StringBuilder stringBuilder = new StringBuilder(175);
        stringBuilder.append(newLine)
                .append("===============GROUND-WAR REPORT===============").append(newLine)
                .append("Start Date: ").append(started).append(newLine)
                .append("Ended date: ").append(ended).append(newLine)
                .append("CreatedBy: ").append(createdBy).append(newLine)
                .append("Winneer: ").append(winner).append(newLine)
                .append("Loser: ").append(loser).append(newLine)
                .append("Teams:").append(newLine);
        for (WarTeam warTeam : initialTeams) {
            stringBuilder.append(PlayerManager.getPlayerRace(warTeam.getCaptain()).getName()).append(newLine);
            for (Participant participant : warTeam.getParticipants()) {
                stringBuilder.append(spacer).append("- ").append(
                        EllirionCore.getINSTANCE().getServer().getPlayer(participant.getPlayer()).getDisplayName())
                        .append(newLine);
            }
        }
        stringBuilder.append(newLine)
                .append("======================END======================").append(newLine);
        return stringBuilder.toString();
    }
}
