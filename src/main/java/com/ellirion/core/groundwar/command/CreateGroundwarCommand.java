package com.ellirion.core.groundwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.race.model.Race;

import java.util.UUID;

public class CreateGroundwarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        UUID playerId = player.getUniqueId();
        Race race = PlayerManager.getPlayerRace(playerId);

        if (GroundWarManager.findGroundWarFromRace(race) != null) {
            player.sendMessage(ChatColor.DARK_RED + "Your race already has a ground war.");
            return true;
        }

        if (race == null) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are not in a race, therefore you are not eligible to start a ground war.");
            return true;
        }

        GroundWarManager.addGroundWar(playerId);
        player.sendMessage(ChatColor.GREEN +
                           "Successfully started a ground war. Now select plots by using the /wagerplot command");

        return true;
    }
}
