package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.event.PlotChangeEvent;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;

public class TeleportToPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        // Check if a name was entered
        int xCord, zCord;
        try {
            xCord = Integer.parseInt(strings[0]);
            zCord = Integer.parseInt(strings[1]);
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED +
                               "Please give the coordinates of the plot: <X> <Z>");
            return true;
        }

        PlotCoord plotCoord = new PlotCoord(xCord, zCord, player.getWorld().getName());

        Plot plot = PlotManager.getPlotByCoordinate(plotCoord);

        if (plot == null) {
            player.sendMessage(ChatColor.DARK_RED + "This plot does not exist.");
            return true;
        }

        Location teleportToLocation = plot.getCenterLocation(player.getWorld(), player.getLocation().getYaw(),
                                                             player.getLocation().getPitch());

        player.teleport(teleportToLocation);

        PlotChangeEvent event = new PlotChangeEvent(player, PlotManager.getPlotFromLocation(player.getLocation()), plot);
        event.call();

        return true;
    }
}
