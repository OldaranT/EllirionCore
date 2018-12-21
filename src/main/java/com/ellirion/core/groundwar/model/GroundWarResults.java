package com.ellirion.core.groundwar.model;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.race.model.Race;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.ellirion.core.util.StringHelper.*;

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
        String newLine = ChatColor.RESET + "\n";
        String spacer = "   ";
        StringBuilder stringBuilder = new StringBuilder(175);
        Player creator = EllirionCore.getINSTANCE().getServer().getOfflinePlayer(createdBy).getPlayer();
        Race warWinner = PlayerManager.getPlayerRace(winner.getCaptain());
        Race warLoser = PlayerManager.getPlayerRace(loser.getCaptain());
        stringBuilder.append(newLine)
                .append(ChatColor.GREEN)
                .append("============== ")
                .append(highlight("GROUNDWAR REPORT", ChatColor.GREEN))
                .append(" ==============").append(newLine)
                .append("Start Date: ").append(started).append(newLine)
                .append("Ended date: ").append(ended).append(newLine)
                .append("CreatedBy: ").append(creator.getDisplayName()).append(newLine)
                .append("Winner: ").append(warWinner.getTeamColor()).append(warWinner.getName()).append(newLine)
                .append("Loser: ").append(warLoser.getTeamColor()).append(warLoser.getName()).append(newLine)
                .append("Teams:").append(newLine);
        for (WarTeam warTeam : initialTeams) {
            stringBuilder.append(PlayerManager.getPlayerRace(warTeam.getCaptain()).getTeamColor())
                    .append(PlayerManager.getPlayerRace(warTeam.getCaptain()).getName()).append(newLine);
            for (Participant participant : warTeam.getParticipants()) {
                stringBuilder.append(spacer).append("- ").append(
                        PlayerManager.getPlayerRace(warTeam.getCaptain()).getTeamColor()).append(
                        participant.getDisplayName())
                        .append(newLine);
            }
        }
        stringBuilder.append(newLine)
                .append(ChatColor.GREEN)
                .append("================= ")
                .append(highlight("END REPORT", ChatColor.GREEN))
                .append(" =================").append(newLine);
        return stringBuilder.toString();
    }
}
