package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;

public class AssignTradingCenterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can execute this command");
            return true;
        }
        Player player = (Player) commandSender;

        Plot plot = null;
        if (strings.length >= 1) {
            //Coördinates of plot were entered
            try {
                int x = Integer.parseInt(strings[0]);
                int z = Integer.parseInt(strings[1]);
                PlotCoord coord = new PlotCoord(GameManager.getInstance().getGame().getGameID(), x, z, player.getWorld().getName());
                plot = PlotManager.getPlotByCoordinate(coord);
            } catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED +
                                   "Something went wrong when trying to read the plot coordinates you entered.");
                return true;
            }
        } else {
            //Plot player is standing on
            plot = PlotManager.getPlotFromLocation(player.getLocation());
        }

        try {
            plot.setOwner(TradingCenter.getInstance());
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED +
                               "Something went wrong with setting the plot owner. Did you select the plot right?");
        }

        return true;
    }
}
