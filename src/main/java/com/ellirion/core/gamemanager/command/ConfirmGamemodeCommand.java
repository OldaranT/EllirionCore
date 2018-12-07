package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.util.EllirionUtil;
import com.ellirion.util.async.Promise;

import static com.ellirion.core.util.StringHelper.*;

public class ConfirmGamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        GameManager gameManager = GameManager.getInstance();

        if (gameManager.getState() != GameManager.GameState.SETUP ||
            !gameManager.currentStepMessage().equals(GameManager.getCONFIRM_SETUP())) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are either not in correct STATE or STEP.\n" + gameManager.toString());
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "The game " + highlight(gameManager.getUName(), ChatColor.GREEN) +
                           " confirmed. Now saving...");

        Promise promise = new Promise(f -> {
            gameManager.confirmGamemode();
            f.resolve(gameManager.getState() == GameManager.GameState.IN_PROGRESS);
        }, true);
        EllirionUtil ellirionUtil = (EllirionUtil) EllirionCore.getINSTANCE().getServer().getPluginManager().getPlugin(
                "EllirionUtil");
        ellirionUtil.schedulePromise(promise).then(f -> {
            player.sendMessage(ChatColor.GREEN + "The game " +
                               highlight(gameManager.getGame().getUName(), ChatColor.GREEN) + " has been saved.");
        });

        return true;
    }
}
