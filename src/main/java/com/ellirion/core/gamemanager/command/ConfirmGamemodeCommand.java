package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;

import static com.ellirion.core.util.StringHelper.*;

public class ConfirmGamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        GameManager gameManager = GameManager.getInstance();

        if (gameManager.getState() != GameManager.GameState.SETUP ||
            !gameManager.currentStepMessage().equals("Confirm setup")) {
            player.sendMessage("You cannot confirm the gamemode at this time. Finish the setup phase first.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "The game " + highlight(gameManager.getUName(), ChatColor.GREEN) +
                           " confirmed. Now saving...");
        gameManager.confirmGamemode();
        player.sendMessage(ChatColor.GREEN + "The game " +
                           highlight(gameManager.getGame().getUName(), ChatColor.GREEN) + " has been saved.");

        return true;
    }
}
