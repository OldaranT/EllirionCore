package com.ellirion.core.race.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.core.race.RaceManager;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.ellirion.core.util.GenericTryCatch.*;
import static com.ellirion.core.util.StringHelper.*;

public class CreateRaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        GameManager gameManager = GameManager.getInstance();
        if (gameManager.getState() != GameManager.GameState.SETUP ||
            !gameManager.currentStepMessage().equals(GameManager.getCREATE_RACE())) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are either not in correct STATE or STEP. \ncurrent state: " +
                               highlight(gameManager.getState().toString(), ChatColor.DARK_RED) + " \ncurrent step: " +
                               highlight(gameManager.getCurrentStep().getMessage(), ChatColor.DARK_RED));
            return true;
        }

        if (strings.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "Please give a race name and color");
            return true;
        }
        if (strings.length == 1) {
            player.sendMessage(ChatColor.DARK_RED + "You forgot either the color or the name");
            return true;
        }
        String raceName = normalNameCasing(String.join(" ", Arrays.copyOf(strings, strings.length - 1)));
        if (RaceManager.raceExists(raceName)) {
            player.sendMessage(ChatColor.DARK_RED + "Race already exists");
            return true;
        }
        AtomicReference<ChatColor> color = new AtomicReference<>();

        tryCatch(() -> color.set(ChatColor.valueOf(strings[strings.length - 1].toUpperCase())));

        if (color.get() == null || RaceManager.isColerInUse(color.get())) {
            player.sendMessage(ChatColor.DARK_RED + "You either miss spelled the color or the color is in use");
            return true;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());
        if (!(plot.getOwner() instanceof Wilderness)) {
            player.sendMessage(ChatColor.DARK_RED + "You can only create race on unowned plots!");
            return true;
        }

        if (!RaceManager.addRace(raceName, color.get(), plot)) {
            player.sendMessage(ChatColor.DARK_RED + "Something went wrong when creating a race.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + raceName + " created.");

        return true;
    }
}
