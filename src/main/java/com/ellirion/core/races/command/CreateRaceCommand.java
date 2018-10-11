package com.ellirion.core.races.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.Wilderness;
import com.ellirion.core.plotsystem.util.PlotManager;
import com.ellirion.core.races.RaceManager;

public class CreateRaceCommand implements CommandExecutor {

    private Player player;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("this is the command for players.");
        }
        player = (Player) sender;

        if (args.length <= 0) {
            sendmsg("please give a race name and color");
            return false;
        }
        if (args.length == 1) {
            sendmsg("you forgot either the color or the name");
            return false;
        }
        if (RaceManager.raceNameExists(args[0])) {
            sendmsg("race already exists");
            return false;
        }
        ChatColor color = ChatColor.valueOf(args[1].toUpperCase());

        if (color == null || RaceManager.isColerInUse(color)) {
            sendmsg("you either miss spelled the color or the color is in use");
            return false;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());
        if (!(plot.getOwner() instanceof Wilderness)) {
            sendmsg(ChatColor.RED + "you can only create races on unowned plots!");
            return true;
        }

        if (!RaceManager.addRace(args[0], color, plot)) {
            sendmsg("something went wrong please try with different values");
            return false;
        }
        sendmsg("race created");
        return true;
    }

    private void sendmsg(String msg) {
        player.sendMessage(msg);
    }
}
