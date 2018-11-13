package com.ellirion.core.gamemanager.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.gamemanager.GameManager;

public class GetGameStateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        GameManager manager = GameManager.getInstance();
        commandSender.sendMessage(manager.toString());
        return true;
    }
}
