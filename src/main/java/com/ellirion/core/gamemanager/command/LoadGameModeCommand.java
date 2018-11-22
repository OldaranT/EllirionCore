package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

public class LoadGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        String format = "Game-";
        String uName;

        if (args.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "please give a race name and color");
            return false;
        }

        uName = String.join(" ", args);

        if (!args[0].startsWith(format)) {
            uName = format + uName;
        }

        GameManager.getInstance().loadGame(uName);

        return true;
    }
}
