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
import com.ellirion.core.util.PlayerScoreboardManager;

import java.util.Arrays;

import static com.ellirion.core.util.GenericTryCatch.*;
import static com.ellirion.core.util.StringHelper.*;

public class CreateRaceCommand implements CommandExecutor {

    private ChatColor color;

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
                               "You are either not in correct STATE or STEP.\n" + gameManager.toString());
            return true;
        }

        if (strings.length <= 0) {
            player.sendMessage(
                    ChatColor.DARK_RED + "To create a race use the following arguments: \n" +
                    "<RACE-NAME> <ALIAS> <COLOR> \n" +
                    "The alias needs to be between 3 and 7 characters and in uppercase and you can autocomplete the color.");
            return true;
        }
        if (strings.length == 1) {
            player.sendMessage(ChatColor.DARK_RED + "You forgot either the name, the alias or the color");
            return true;
        }
        if (strings.length == 2) {
            player.sendMessage(
                    ChatColor.DARK_RED + "You forgot one of following params: the name, the alias or the color");
            return true;
        }

        String raceName = normalNameCasing(String.join(" ", Arrays.copyOf(strings, strings.length - 2)));
        String raceAlias = strings[strings.length - 2].toUpperCase();

        if (!raceAlias.matches("^[A-Z0-9]{3,7}$")) {
            player.sendMessage(
                    ChatColor.DARK_RED + "The race alias needs to be between 3 to 7 characters and uppercase.");
            return true;
        }

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
            player.sendMessage(ChatColor.DARK_RED + "You can only create race on unowned(Wilderness) plots!");
            return true;
        }

        if (!RaceManager.addRace(raceName, raceAlias, color, plot)) {
            player.sendMessage(ChatColor.DARK_RED + "Something went wrong when creating a race.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + raceName + " created.");
        PlayerScoreboardManager.updateBoards();
        return true;
    }
}
