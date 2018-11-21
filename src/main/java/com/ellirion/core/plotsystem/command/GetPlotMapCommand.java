package com.ellirion.core.plotsystem.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotOwner;
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
        if (plot == null) {
            player.sendMessage("Unable to generate plotmap, please stand on a plot and try again.");
            return true;
        }

        //TODO get player race
        PlotOwner owner = Wilderness.getInstance();

        player.sendMessage(PlotManager.getPlotMap(plot, 2, owner));
        return true;
    }
}
