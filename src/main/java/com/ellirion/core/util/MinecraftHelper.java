package com.ellirion.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import com.ellirion.core.EllirionCore;

public class MinecraftHelper {

    /**
     * Translate a chatcolor to a color.
     * @param chatColor the chatcolor to translate.
     * @return Translated Color.
     */
    public static Color translateChatColorToColor(ChatColor chatColor) {
        switch (chatColor) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case DARK_AQUA:
                return Color.BLUE;
            case DARK_BLUE:
                return Color.BLUE;
            case DARK_GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_PURPLE:
                return Color.PURPLE;
            case DARK_RED:
                return Color.RED;
            case GOLD:
                return Color.YELLOW;
            case GRAY:
                return Color.GRAY;
            case GREEN:
                return Color.GREEN;
            case LIGHT_PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
            default:
                break;
        }

        return null;
    }

    /**
     * Remove all teams from scoreboard.
     */
    public static void removeAllTeams() {
        ScoreboardManager scoreboardManager = EllirionCore.getINSTANCE().getServer().getScoreboardManager();
        Scoreboard mainScoreboard = scoreboardManager.getMainScoreboard();
        for (Team team : mainScoreboard.getTeams()) {
            team.unregister();
        }
    }
}
