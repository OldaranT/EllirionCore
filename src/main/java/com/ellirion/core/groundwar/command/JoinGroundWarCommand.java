package com.ellirion.core.groundwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ellirion.core.groundwar.GroundWarManager;
import com.ellirion.core.groundwar.model.GroundWar;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.Logging;

import java.util.UUID;

public class JoinGroundWarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        UUID playerID = player.getUniqueId();

        try {
            Race race = PlayerManager.getPlayerRace(playerID);
            GroundWar groundWar = GroundWarManager.findGroundWarFromRace(race);
            groundWar.addPlayer(race, playerID);
            player.sendMessage(ChatColor.GREEN + "Successfully joined ground war");
        } catch (Exception ex) {
            Logging.printStackTrace(ex);
            player.sendMessage(ChatColor.DARK_RED + "Could not find a ground war for you to join");
        }


        return true;
    }
}
