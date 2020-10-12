package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.gamemanager.model.Game;

import java.util.UUID;

import static com.ellirion.core.util.StringHelper.highlight;

public class BeginGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        GameManager gameManager = GameManager.getInstance();
        UUID gameID = UUID.randomUUID();

        if (strings.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "please give a unique name for the game.");
            return true;
        }

        String uName = String.join(" ", strings);
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

            commandSender.sendMessage("The GameMode is now in the SETUP stage.");
        } else {
            commandSender.sendMessage(
                    ChatColor.DARK_RED + "A game mode can only be created during the NOT_STARTED state.");
        }

        return true;
    }
}
