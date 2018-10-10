package com.ellirion.core.plotsystem.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.util.PlotManager;

public class GetPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());

        player.sendMessage("Name: " + plot.getName());

        player.sendMessage("Size: " + plot.getPlotSize());

        player.sendMessage("Lower Corner: " + plot.getLowestCorner().toString());

        player.sendMessage("Highest Corner: " + plot.getHighestCorner().toString());

        player.sendMessage("World: " + plot.getWorld().getName());

        player.sendMessage("Owner: \n" + plot.getOwner().getName());

        return true;
    }
}
