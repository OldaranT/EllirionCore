package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.Wilderness;
import com.ellirion.core.races.model.Race;

import java.util.UUID;

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

        // Check if coords where entered.
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

        //Get neighbouring plots to check if there is a plot to connect to.
        Plot[] neighbourPlots = plotToCheck.getNeighbours();

        Race playerRace = PlayerManager.getPlayerRace(player.getUniqueId());

        if (playerRace == null) {
            player.sendMessage(ChatColor.DARK_RED + "You need to be in a race to claim a plot.");
            return true;
        }

        for (Plot plot : neighbourPlots) {
            if (plot == null) {
                continue;
            }
            UUID plotUUID = plot.getOwner().getRaceUUID();
            UUID raceUUID = playerRace.getRaceUUID();
            player.sendMessage("Plot: " + plot.getName() + " uuid:" + plotUUID.toString());
            player.sendMessage("Race: " + raceUUID.toString());

            allowedToClaim = plotUUID.equals(raceUUID);

            if (allowedToClaim) {
                break;
            }
        }

        if (!allowedToClaim) {
            player.sendMessage(ChatColor.DARK_RED + "There is no neighbouring plot to connect to.");
            return true;
        }

        //If the plot is already owned by different race advice to start a ground war instead.
        if (!(plotToCheck.getOwner() instanceof Wilderness)) {
            player.sendMessage(ChatColor.DARK_RED + "This plot is already owned by a different race.\n" +
                               "If you still like to claim this plot you need to start a ground war.");
            return true;
        }

        plotToCheck.setOwner(playerRace);
        player.sendMessage(
                ChatColor.GREEN + "Plot " + plotToCheck.getName() + " has been claimed by " +
                playerRace.getNameWithColor() + "!");

        return true;
    }
}
