package com.ellirion.core.gamemanager.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

public class CancelSetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        GameManager manager = GameManager.getInstance();

        if (manager.getState() == GameManager.GameState.SETUP) {
            manager.cancelSetup();
            player.sendMessage(
                    "The setup of the gamemode was canceled, use /begingamemode to start the setup phase again");
        }

        return true;
    }
}
