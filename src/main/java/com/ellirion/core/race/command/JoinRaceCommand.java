package com.ellirion.core.race.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;

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
        String raceName = PlayerManager.getPlayerRace(player.getUniqueId()).getName();
        sender.getServer().broadcastMessage(sender.getName() + " joined " + raceName);
        return true;
    }

    private void sendmsg(String msg) {
        sender.sendMessage(msg);
    }
}
