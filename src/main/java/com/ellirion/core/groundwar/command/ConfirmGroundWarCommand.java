package com.ellirion.core.groundwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;

import java.util.UUID;

public class ConfirmGroundWarCommand implements CommandExecutor {

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
            player.sendMessage(ChatColor.DARK_RED + "You are not in a ground war, therefore you cannot cancel it.");
            return true;
        }

        if (!(groundWar.getState() == GroundWar.State.SETUP)) {
            player.sendMessage(ChatColor.DARK_RED + "You can only cancel when you are in the SETUP state.");
            return true;
        }

        if (groundWar.getPlotA() != null && groundWar.getPlotB() != null) {
            GroundWarManager.confirmGroundWar(groundWar);
        }

        return false;
    }
}
