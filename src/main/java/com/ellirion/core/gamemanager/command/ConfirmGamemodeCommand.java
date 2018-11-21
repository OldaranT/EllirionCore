package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

public class ConfirmGamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        GameManager manager = GameManager.getInstance();

        if (manager.getState() != GameManager.GameState.SETUP || !manager.currentStepMessage().equals("Confirm setup")) {
            player.sendMessage("You cannot confirm the gamemode at this time. Finish the setup phase first.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "Gamemode confirmed. Now saving...");
        manager.confirmGamemode();
        player.sendMessage(ChatColor.GREEN + "Gamemode saved.");

        return true;
    }
}
