package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.gamemanager.model.Game;

import java.util.UUID;

import static com.ellirion.core.util.StringHelper.*;

public class BeginGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("this is the command for players.");
        }

        Player player = (Player) sender;

        GameManager gameManager = GameManager.getInstance();
        UUID gameID = UUID.randomUUID();

        if (args.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "please give a unique name for the game.");
            return true;
        }

        String uName = String.join(" ", args);
        uName = uName.replaceAll("[^a-zA-Z0-9\\s]", "")
                .toLowerCase()
                .replaceFirst(uName.charAt(0) + "", Character.toUpperCase(uName.charAt(0)) + "");

        //Check if name already exist in database.
        for (Game game : GameManager.getGAMES().values()) {
            if (game.getUName().equals(uName)) {
                player.sendMessage(ChatColor.DARK_RED + "The name " + highlight(uName, ChatColor.DARK_RED) +
                                   " has already been taken.");
                return true;
            }
        }

        if (gameManager.getState().ordinal() < GameManager.GameState.SETUP.ordinal()) {

            //Start the setup.
            gameManager.changeState(GameManager.GameState.SETUP);

            //temp Save gameid and uName
            gameManager.setGameID(gameID);
            gameManager.setUName(uName);

            sender.sendMessage("The GameMode is now in the SETUP stage.");
        } else {
            sender.sendMessage(
                    ChatColor.DARK_RED + "A game mode can only be created during the NOT_STARTED state.");
        }

        return true;
    }
}
