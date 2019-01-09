package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

import static com.ellirion.core.util.StringHelper.*;

public class UnloadGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        String uName = GameManager.getInstance().getUName();

        if (!GameManager.getInstance().unloadGame()) {
            player.sendMessage(ChatColor.DARK_RED + "Could not unload the game with name: " +
                               highlight(uName, ChatColor.RESET));
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "The game " + highlight(uName, ChatColor.GREEN) + " has been unloaded.");

        return true;
    }
}
