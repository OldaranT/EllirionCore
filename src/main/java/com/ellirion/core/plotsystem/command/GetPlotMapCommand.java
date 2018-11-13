package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;

public class GetPlotMapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can execute this command");
            return true;
        }

        Player player = (Player) commandSender;

        //Get the plot the player is standing on
        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());
        //Get neighbouring plots
        Plot[] neighbours = plot.getNeighbours();
        //Get corners
        PlotCoord coord = plot.getPlotCoord();
        Plot[] corners = new Plot[]{
                PlotManager.getPlotByCoordinate(coord.translate(-1, 1)),
                PlotManager.getPlotByCoordinate(coord.translate(1, -1)),
                PlotManager.getPlotByCoordinate(coord.translate(-1, -1)),
                PlotManager.getPlotByCoordinate(coord.translate(1, 1))
        };

        Plot[] allNeighbours = new Plot[]{
                corners[0],     //Top-Left
                neighbours[0],  //Top
                corners[1],     //Top-Right
                neighbours[3],  //Left
                neighbours[1],  //Right
                corners[2],     //Bottom-Left
                neighbours[2],  //Bottom
                corners[3]      //Bottom-right
        };

        //Build map string
        StringBuilder builder = new StringBuilder();
        int current = 0;
        for (int i = 0; i < allNeighbours.length; i++) {
            if (allNeighbours[i].getOwner().equals(plot.getOwner())) {
                builder.append(ChatColor.GREEN).append('+');
            } else if (allNeighbours[i].getOwner() == Wilderness.getInstance()) {
                builder.append(ChatColor.GRAY).append('#');
            } else {
                builder.append(ChatColor.RED).append('-');
            }
            if (i == 3) {
                builder.append(ChatColor.GREEN).append('+');
                current++;
            }
            if ((current + 1) % 3 == 0) {
                builder.append('\n');
            }
            current++;
        }
        player.sendMessage(builder.toString());
        return true;
    }
}
