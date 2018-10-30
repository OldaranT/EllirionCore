package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.gamemanager.GameManager;

public class BeginGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        GameManager manager = GameManager.getInstance();
        if (manager.getState().ordinal() < GameManager.GameState.SETUP.ordinal()) {
            manager.changeState(GameManager.GameState.SETUP);
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED + "A game mode can only be created during the NOT_STARTED state.");
        }

        return true;
    }
}
