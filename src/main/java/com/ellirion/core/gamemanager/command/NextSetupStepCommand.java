package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

public class NextSetupStepCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        GameManager gameManager = GameManager.getInstance();

        if (gameManager.getState() != GameManager.GameState.SETUP) {
            commandSender.sendMessage(ChatColor.DARK_RED +
                                      "The game mode is currently not in the SETUP phase, therefore there is no next step");
            return true;
        }

        if (!gameManager.nextStep()) {
            commandSender.sendMessage(ChatColor.DARK_RED + "This step has requirements that have not been met yet");
            return true;
        }

        commandSender.sendMessage("The gamemode has advanced to the next setup step");
        commandSender.sendMessage(gameManager.toString());

        return true;
    }
}
