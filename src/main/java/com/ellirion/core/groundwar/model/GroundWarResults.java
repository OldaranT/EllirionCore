package com.ellirion.core.groundwar.model;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.StringHelper;

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
        String newLine = ChatColor.RESET + "\n";
        String spacer = "   ";
        StringBuilder stringBuilder = new StringBuilder(175);
        Player creator = EllirionCore.getINSTANCE().getServer().getPlayer(createdBy);
        Race warWinner = PlayerManager.getPlayerRace(winner.getCaptain());
        Race warLoser = PlayerManager.getPlayerRace(loser.getCaptain());
        stringBuilder.append(newLine)
                .append(ChatColor.GREEN)
                .append("============== ")
                .append(StringHelper.highlight("GROUND - WAR REPORT", ChatColor.GREEN))
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
                        EllirionCore.getINSTANCE().getServer().getPlayer(participant.getPlayer()).getDisplayName())
                        .append(newLine);
            }
        }
        stringBuilder.append(newLine)
                .append(ChatColor.GREEN)
                .append("================= ")
                .append(StringHelper.highlight("END REPORT", ChatColor.GREEN))
                .append(" ==================").append(newLine);
        return stringBuilder.toString();
    }
}
