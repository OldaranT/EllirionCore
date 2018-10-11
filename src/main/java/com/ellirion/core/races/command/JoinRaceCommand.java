package com.ellirion.core.races.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.util.PlotManager;
import com.ellirion.core.races.RaceManager;

public class JoinRaceCommand implements CommandExecutor {

    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        if (!(sender instanceof Player)) {
            sendmsg("only a player may join a team");
            return false;
        }
        Player player = (Player) sender;

        Location loc = player.getLocation();
        if (!PlayerManager.setPlayerRace(player.getUniqueId(),
                                         PlotManager.getPlotFromLocation(loc).getOwner().getRaceUUID())) {
            sendmsg("something went wrong please try again with different arguments");
            return false;
        }
        String raceName = RaceManager.getPlayerRace(player).getName();
        sender.getServer().broadcastMessage(sender.getName() + " joined " + raceName);
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
