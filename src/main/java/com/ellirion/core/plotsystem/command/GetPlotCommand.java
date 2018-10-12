package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;

public class GetPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());

        if (plot == null) {
            player.sendMessage(ChatColor.DARK_RED + "No plot found.");
            return true;
        }

        player.sendMessage("Name: " + plot.getName());

        player.sendMessage("Size: " + plot.getPlotSize());

        player.sendMessage("Lower Corner: " + plot.getLowestCorner().toString());

        player.sendMessage("Highest Corner: " + plot.getHighestCorner().toString());

        player.sendMessage("World: " + plot.getWorld().getName());

        player.sendMessage("Owner: \n" + plot.getOwner().getName());

        return true;
    }
}
