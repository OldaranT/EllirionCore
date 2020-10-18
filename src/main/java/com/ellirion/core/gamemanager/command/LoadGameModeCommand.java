package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

import static com.ellirion.core.util.StringHelper.highlight;

public class LoadGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "Please give the name of the game you want to load.");
            return true;
        }

        String uName = String.join(" ", strings);

        if (!GameManager.getInstance().loadGame(uName)) {
            player.sendMessage(ChatColor.DARK_RED + "Could not load the game with name: " +
                               highlight(uName, ChatColor.RESET));
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "The game " + highlight(uName, ChatColor.GREEN) + " has been loaded.");

        return true;
    }
}
