package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;

import static com.ellirion.core.util.StringHelper.highlight;

public class GetPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        GameManager gameManager = GameManager.getInstance();

        if (gameManager.getState() != GameManager.GameState.IN_PROGRESS) {
            player.sendMessage(
                    ChatColor.DARK_RED + "The game needs to be in the " +
                    highlight("IN_PROGRESS", ChatColor.DARK_RED) + " state to use this command.");
            return true;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());

        if (plot == null) {
            player.sendMessage(ChatColor.DARK_RED + "You need to be in a plot to use this command.");
            return true;
        }

        player.sendMessage("Name: " + plot.getName());

        player.sendMessage("Size: " + GameManager.getInstance().getGame().getPlotSize());

        player.sendMessage("Lower Corner: " + plot.getBoundingBox().getPoint1().toString());

        player.sendMessage("Highest Corner: " + plot.getBoundingBox().getPoint2().toString());

        player.sendMessage("World: " + plot.getPlotCoord().getWorldName());

        player.sendMessage("Owner: \n" + plot.getOwner().getName());

        return true;
    }
}
