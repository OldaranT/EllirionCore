package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;

import static com.ellirion.core.util.GenericTryCatch.*;
import static com.ellirion.core.util.StringHelper.*;

public class AssignTradingCenterCommand implements CommandExecutor {

    private Plot plot;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }
        Player player = (Player) commandSender;
        if (strings.length > 1) {

            int x = Integer.parseInt(strings[0]);
            int z = Integer.parseInt(strings[1]);

            //Coördinates of plot were entered
            if (!tryCatch(
                    () -> plot = PlotManager.getPlotByCoordinate(new PlotCoord(x, z, player.getWorld().getName())))) {
                player.sendMessage(ChatColor.DARK_RED + "The plot with the coords " +
                                   highlight(x + " " + z, ChatColor.DARK_RED) +
                                   " Does not exist.");
                return true;
            }
        } else {
            //Plot player is standing on
            plot = PlotManager.getPlotFromLocation(player.getLocation());
        }

        if (!tryCatch(() -> plot.setOwner(TradingCenter.getInstance()))) {
            player.sendMessage(ChatColor.DARK_RED + "You are not located on a plot.");
            return true;
        }

        player.sendMessage(ChatColor.BOLD + plot.getName() + ChatColor.RESET +
                           ChatColor.GREEN + " is now a trading center plot.");
        return true;
    }
}
