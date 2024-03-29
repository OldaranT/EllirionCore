package com.ellirion.core.groundwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarHelper;
import com.ellirion.core.groundwar.model.GroundWar;

import java.util.UUID;

public class CancelGroundWarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        UUID playerID = player.getUniqueId();
        GroundWar groundWar = GroundWarHelper.getGroundWar(playerID);

        if (groundWar == null) {
            player.sendMessage(ChatColor.DARK_RED + "You are not in a ground war, therefore you cannot cancel it.");
            return true;
        }

        if (!(groundWar.getState() == GroundWar.State.SETUP)) {
            player.sendMessage(
                    ChatColor.DARK_RED + "You can only cancel a ground war when you are in the SETUP state.");
            return true;
        }

        if (!groundWar.getCreatedBy().equals(playerID)) {
            player.sendMessage(ChatColor.DARK_RED + "Only the player that started this ground war can cancel it.");
            return true;
        }

        //Maybe notify participants the ground war has been canceled
        GroundWarHelper.removeGroundWar(playerID);
        player.sendMessage(ChatColor.GREEN + "The ground war has been canceled.");

        return true;
    }
}
