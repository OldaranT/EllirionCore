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
import com.ellirion.core.plotsystem.model.PlotOwner;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;
import com.ellirion.core.races.model.Race;

public class ClaimPlotCommand implements CommandExecutor {

    private Player player;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        player = (Player) commandSender;

        Plot plotToCheck;

        // Check if coords where entered.
        if (args.length == 2) {
            int xCoord = Integer.parseInt(args[0]);
            int zCoord = Integer.parseInt(args[1]);

            PlotCoord plotCoord = new PlotCoord(xCoord, zCoord);

            plotToCheck = PlotManager.getPlotByCoordinate(plotCoord);
        } else if (args.length == 0) {
            plotToCheck = PlotManager.getPlotFromLocation(player.getLocation());
        } else {
            player.sendMessage(ChatColor.DARK_RED +
                               "Please give the coordinates of the plot: <X> <Z>. \n " +
                               "You can also give no coordinates to claim the plot you are standing in.");
            return true;
        }

        if (plotToCheck.getOwner() instanceof TradingCenter) {
            player.sendMessage(ChatColor.DARK_RED + "This plot is a game plot. you can't claim this plot.");
            return false;
        }

        Plot[] neighbourPlots = plotToCheck.getNeighbours();

        Race playerRace = PlayerManager.getPlayerRace(player.getUniqueId());

        if (playerRace == null) {
            player.sendMessage(ChatColor.DARK_RED + "You need to be in a race to claim a plot.");
            return true;
        }

        //check if there is a plot to connect to.
        if (!ownsNeighbour(neighbourPlots, playerRace)) {
            player.sendMessage(ChatColor.DARK_RED + "None of the neighbouring plots are owned by your race.");
            return true;
        }

        //If the plot is already owned by different race advice to start a ground war instead.
        if ((plotToCheck.getOwner() instanceof Race)) {
            player.sendMessage(ChatColor.DARK_RED + "This plot is already owned by a different race.\n " +
                               "If you still like to claim this plot you need to start a ground war.");
            return true;
        }

        plotToCheck.setOwner(playerRace);
        player.sendMessage(
                ChatColor.GREEN + "Plot " + plotToCheck.getName() + " has been claimed by " +
                playerRace.getNameWithColor() + "!");

        return true;
    }

    private boolean ownsNeighbour(Plot[] plotsToCheck, PlotOwner playerRace) {

        for (Plot plot : plotsToCheck) {
            if (plot == null) {
                continue;
            }
            PlotOwner plotOwner = plot.getOwner();
            player.sendMessage("Plot: " + plotOwner.getName());
            player.sendMessage("Race: " + playerRace.getName());

            if (plotOwner.equals(playerRace)) {
                return true;
            }
        }
        return false;
    }
}
