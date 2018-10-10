package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.Wilderness;
import com.ellirion.core.plotsystem.util.PlotManager;
import com.ellirion.core.races.RaceManager;
import com.ellirion.core.races.model.Race;

public class SetPlotOwnerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        Race playerRace = RaceManager.getPlayerRace(player);

        if (playerRace == null) {
            player.sendMessage(ChatColor.DARK_RED + "You must be in a race to claim a plot.");
            return true;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());

        if (!(plot.getOwner() instanceof Wilderness)) {
            player.sendMessage(ChatColor.DARK_RED + "This plot is already owned by a different race.");
            return true;
        }

        plot.setOwner(playerRace);
        player.sendMessage(ChatColor.GREEN  + "Your race is now the owner of plot: " + plot.getName());

        return true;
    }
}
