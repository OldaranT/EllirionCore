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
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.core.race.model.Race;

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
                PlotManager.getPlotByCoordinate(coord.translate(1, 1)),
                PlotManager.getPlotByCoordinate(coord.translate(-1, -1)),
                PlotManager.getPlotByCoordinate(coord.translate(1, -1))
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
        PlotOwner owner = PlayerManager.getPlayerRace(player.getUniqueId());
        for (int i = 0; i < allNeighbours.length; i++) {
            if (allNeighbours[i].getOwner().equals(owner)) {                                //If the neighbouring plot has the same owner
                builder.append(((Race) owner).getTeamColor()).append('+');
            } else if (allNeighbours[i].getOwner().equals(Wilderness.getInstance())) {      //If the neighbouring plot is wilderness
                builder.append(ChatColor.GRAY).append('#');
            } else if (allNeighbours[i].getOwner().equals(TradingCenter.getInstance())) {   //If the neighbouring plot is trading center
                builder.append(ChatColor.GOLD).append('$');
            } else {                                                                        //neighbouring plot is enemy race
                builder.append(((Race) allNeighbours[i].getOwner()).getTeamColor()).append('-');
            }
            if (i == 3) {
                builder.append(ChatColor.GREEN).append('O');
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
