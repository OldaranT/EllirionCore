package com.ellirion.core.race.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;

public class JoinRaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        Location loc = player.getLocation();
        if (!PlayerManager.setPlayerRace(player.getUniqueId(),
                                         PlotManager.getPlotFromLocation(loc).getOwner().getRaceUUID())) {
            player.sendMessage("something went wrong when adding you to a race. please try again.");
            return false;
        }
        String raceName = PlayerManager.getPlayerRace(player.getUniqueId()).getName();
        player.getServer().broadcastMessage(player.getName() + " joined " + raceName);
        return true;
    }
}
