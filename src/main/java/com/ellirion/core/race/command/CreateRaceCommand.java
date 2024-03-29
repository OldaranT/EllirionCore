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
import com.ellirion.core.race.RaceHelper;
import com.ellirion.core.util.PlayerScoreboardHelper;

import java.util.Arrays;

import static com.ellirion.core.util.GenericTryCatchUtils.tryCatch;
import static com.ellirion.core.util.StringHelper.normalNameCasing;

public class CreateRaceCommand implements CommandExecutor {

    private ChatColor color;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        int maxNameLength = 9;

        GameManager gameManager = GameManager.getInstance();
        if (gameManager.getState() != GameManager.GameState.SETUP ||
            !gameManager.currentStepMessage().equals(GameManager.getCREATE_RACE())) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are either not in correct STATE or STEP.\n" + gameManager.toString());
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

        if (raceName.length() > maxNameLength) {
            player.sendMessage(ChatColor.DARK_RED + "The race name can not be longer then 9 characters");
            return true;
        }

        if (RaceHelper.raceExists(raceName)) {
            player.sendMessage(ChatColor.DARK_RED + "Race already exists");
            return true;
        }

        if (!tryCatch(() -> color = ChatColor.valueOf(strings[strings.length - 1].toUpperCase()))) {
            player.sendMessage(ChatColor.DARK_RED + "The color you entered can not be found");
            return true;
        }

        if (color == null || RaceHelper.isColerInUse(color)) {
            player.sendMessage(ChatColor.DARK_RED + "You either miss spelled the color or the color is in use");
            return true;
        }

        Plot plot = PlotManager.getPlotFromLocation(player.getLocation());
        if (!(plot.getOwner() instanceof Wilderness)) {
            player.sendMessage(ChatColor.DARK_RED + "You can only create race on unowned plots!");
            return true;
        }

        if (!RaceHelper.addRace(raceName, color, plot)) {
            player.sendMessage(ChatColor.DARK_RED + "Something went wrong when creating a race.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + raceName + " created.");
        PlayerScoreboardHelper.updateBoards();
        return true;
    }
}
