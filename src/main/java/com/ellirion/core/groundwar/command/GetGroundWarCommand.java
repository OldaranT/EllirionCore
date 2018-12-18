package com.ellirion.core.groundwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;

import java.util.UUID;

public class GetGroundWarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }
        Player player = (Player) commandSender;
        UUID playerId = player.getUniqueId();

        GroundWar groundWar = GroundWarManager.getGroundWar(playerId);
        if (groundWar == null) {
            player.sendMessage(ChatColor.DARK_RED + "You are not currently in a ground war.");
            return true;
        }

        player.sendMessage(groundWar.toString());

        return true;
    }
}
