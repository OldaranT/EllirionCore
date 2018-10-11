package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.util.PlotManager;

public class TeleportToPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        // Check if a name was entered
        if (args.length < 2 || args.length > 2) {
            player.sendMessage(ChatColor.DARK_RED +
                               "Please give the coordinates of the plot: <X-Cord> <Z-Cord>");
            return true;
        }

        int xCord = Integer.parseInt(args[0]);
        int zCord = Integer.parseInt(args[1]);

        Plot plot = PlotManager.getPlotByName(xCord + " , " + zCord);

        if (plot == null) {
            player.sendMessage(ChatColor.DARK_RED + "This plot does not exist.");
            return true;
        }

        Location teleportToLocation = plot.getCenterLocation(player.getLocation().getYaw(), player.getLocation().getPitch());

        player.teleport(teleportToLocation);

        return true;

    }
}
