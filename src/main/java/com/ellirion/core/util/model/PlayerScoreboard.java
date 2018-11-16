package com.ellirion.core.util.model;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;

public class PlayerScoreboard {
    private Player player;
    private Scoreboard scoreboard;

    /**
     * Create a PlayerScoreboard.
     * @param player the player whose scoreboard this is
     * @param scoreboard the scoreboard of that player
     */
    public PlayerScoreboard(final Player player, final Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;
        init();
    }

    private void init() {
        updateBoard();
    }

    /**
     * Update the scoreboard.
     */
    public void updateBoard() {
        Objective objective = scoreboard.getObjective("map");
        if (objective != null) {
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("map", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Map:");

        String plotMap = PlotManager.getPlotMap(PlotManager.getPlotFromLocation(player.getLocation()), 5, Wilderness.getInstance());
        String[] lines = plotMap.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String lineNumber = String.format("%02d", i);
            Score score = objective.getScore(lineNumber + " " + lines[i]);
            score.setScore(0);
        }
    }

    /**
     * Show the scoreboard to the player.
     */
    public void showScoreboard() {
        player.setScoreboard(scoreboard);
    }
}
