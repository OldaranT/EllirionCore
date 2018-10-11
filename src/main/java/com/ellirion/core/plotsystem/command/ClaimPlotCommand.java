package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.util.PlotManager;
import com.ellirion.core.races.RaceManager;
import com.ellirion.core.races.model.Race;

public class ClaimPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        Plot plotToCheck;

        boolean allowedToClaim = false;

        // Check if a name was entered
        if (args.length > 0) {
            if (args.length < 2 || args.length > 2) {
                player.sendMessage(ChatColor.DARK_RED +
                                   "Please give the coordinates of the plot: <X-Cord> <Z-Cord>. \n " +
                                   "You can also give no coordinates to claim the plot you are standing in.");
                return true;
            }

            int xCoord = Integer.parseInt(args[0]);
            int zCoord = Integer.parseInt(args[1]);

            PlotCoord plotCoord = new PlotCoord(xCoord, zCoord);

            plotToCheck = PlotManager.getPlotByCoordinate(plotCoord);
        } else {
            plotToCheck = PlotManager.getPlotFromLocation(player.getLocation());
        }

        Plot[] neighbourPlots = plotToCheck.getNeighbours();

        Race playerRace = RaceManager.getPlayerRace(player);

        for (Plot plot: neighbourPlots) {
            allowedToClaim = plot.getOwner().getName().equals(playerRace.getName());
        }

        player.sendMessage("Plot is claimable: " + allowedToClaim);

        return true;
    }
}
