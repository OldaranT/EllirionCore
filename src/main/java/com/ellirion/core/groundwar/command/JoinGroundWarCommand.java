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
        Race race = PlayerManager.getPlayerRace(playerID);
        GroundWar groundWar = GroundWarManager.findGroundWarFromRace(race);

        if (race == null) {
            player.sendMessage(ChatColor.DARK_RED + "You need to be in a race to join a ground war.");
            return true;
        }

        if (groundWar == null) {
            player.sendMessage(ChatColor.DARK_RED + "Your race is not in a ground war.");
            return true;
        }

        if (groundWar.getState() != GroundWar.State.CONFIRMED) {
            player.sendMessage(ChatColor.DARK_RED + "You can only join a ground war that is been confirmed");
            return true;
        }

        if (GroundWarManager.getGroundWar(playerID) != null) {
            player.sendMessage(ChatColor.DARK_RED + "You are already in a ground war!");
            return true;
        }

        groundWar.addPlayer(race, playerID);
        player.sendMessage(ChatColor.GREEN + "Successfully joined ground war");

        return true;
    }
}
