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
            commandSender.sendMessage("You need to be a player to use this command.");
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

        int radius = 2;
        if (strings.length > 0) {
            try {
                radius = Integer.parseInt(strings[0]);
            } catch (Exception ex) {
                // Do nothing
            }
        }

        // Clamp radius between 1 and 9
        radius = Math.max(1, radius);
        radius = Math.min(radius, 9);

        player.sendMessage(PlotManager.getPlotMap(plot, radius, owner));
        return true;
    }
}
