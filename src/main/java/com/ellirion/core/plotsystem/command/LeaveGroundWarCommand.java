package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;

import java.util.UUID;

public class LeaveGroundWarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You cannot leave a GroundWar as a non-player.");
            return true;
        }

        Player player = (Player) commandSender;
        UUID playerID = player.getUniqueId();
        GroundWar war = GroundWarManager.getGroundWar(playerID);

        if (war == null) {
            player.sendMessage("You were not in a groundwar.");
            return true;
        }

        war.removeParticipant(playerID);
        player.sendMessage(ChatColor.GREEN + "You successfully left the ground war");

        return true;
    }
}
