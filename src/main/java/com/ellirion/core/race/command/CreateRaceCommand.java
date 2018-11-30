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
import java.util.concurrent.atomic.AtomicReference;

import static com.ellirion.core.util.GenericTryCatch.*;

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
        String raceName = StringHelper.normalNameCasing(String.join(" ", Arrays.copyOf(args, args.length - 1)));
        if (RaceManager.raceExists(raceName)) {
            sendmsg("race already exists");
            return true;
        }
        AtomicReference<ChatColor> color = new AtomicReference<>();

        tryCatch(() -> color.set(ChatColor.valueOf(args[args.length - 1].toUpperCase())));

        if (color.get() == null || RaceManager.isColerInUse(color.get())) {
            sendmsg("you either miss spelled the color or the color is in use");
            return true;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());
        if (!(plot.getOwner() instanceof Wilderness)) {
            sendmsg(ChatColor.DARK_RED + "you can only create race on unowned plots!");
            return true;
        }

        if (!RaceManager.addRace(raceName, color.get(), plot)) {
            sendmsg("something went wrong when creating a race.");
            return true;
        }
        sendmsg(raceName + " created");
        return true;
    }

    private void sendmsg(String msg) {
        player.sendMessage(msg);
    }
}
