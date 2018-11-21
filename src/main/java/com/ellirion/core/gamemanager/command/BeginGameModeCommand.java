package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.gamemanager.model.Game;

import java.util.UUID;

public class BeginGameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("this is the command for players.");
        }

        Player player = (Player) sender;

        GameManager manager = GameManager.getInstance();
        UUID gameID = UUID.randomUUID();
        Game game;

        if (args.length <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "please give a unique name for the game.");
            return false;
        }

        String uName = args[0];
        uName = "Game-" + uName.replaceAll("[^a-zA-Z0-9\\s]", "")
                .toLowerCase()
                .replaceFirst(uName.charAt(0) + "", Character.toUpperCase(uName.charAt(0)) + "");

        if (manager.getState().ordinal() < GameManager.GameState.SETUP.ordinal()) {
            manager.changeState(GameManager.GameState.SETUP);
            GameManager.getInstance().setGameId(gameID);
            //TODO NEEDS TO BE CHANGED TESTING PURPOSES.
            game = new Game(gameID, uName, 0, 0, 128);
            EllirionCore.getINSTANCE().getDbManager().createGame(game);

            sender.sendMessage("The GameMode is now in the SETUP stage.");
        } else {
            sender.sendMessage(
                    ChatColor.DARK_RED + "A game mode can only be created during the NOT_STARTED state.");
        }

        return true;
    }
}
