package com.ellirion.core.race.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.util.StringHelper;

import java.util.Arrays;

import static com.ellirion.core.util.GenericTryCatch.*;

public class CreateRaceCommand implements CommandExecutor {

    private ChatColor color;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "Please give a race name and color");
            return true;
        }
        if (strings.length == 1) {
            player.sendMessage(ChatColor.DARK_RED + "You forgot either the color or the name");
            return true;
        }
        String raceName = StringHelper.normalNameCasing(String.join(" ", Arrays.copyOf(strings, strings.length - 1)));
        if (RaceManager.raceExists(raceName)) {
            player.sendMessage(ChatColor.DARK_RED + "Race already exists");
            return true;
        }

        if (!tryCatch(() -> color = ChatColor.valueOf(strings[strings.length - 1].toUpperCase()))) {
            player.sendMessage(ChatColor.DARK_RED + "The color you entered can not be found");
            return true;
        }

        if (color == null || RaceManager.isColerInUse(color)) {
            player.sendMessage(ChatColor.DARK_RED + "You either miss spelled the color or the color is in use");
            return true;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());
        if (!(plot.getOwner() instanceof Wilderness)) {
            player.sendMessage(ChatColor.DARK_RED + "You can only create race on unowned plots!");
            return true;
        }

        if (!RaceManager.addRace(raceName, color, plot)) {
            player.sendMessage(ChatColor.DARK_RED + "Something went wrong when creating a race.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + raceName + " created.");

        return true;
    }
}
