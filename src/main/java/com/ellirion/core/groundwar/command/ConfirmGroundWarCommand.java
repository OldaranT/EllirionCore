package com.ellirion.core.groundwar.command;

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
            return true;
        }
        Player player = (Player) commandSender;
        UUID playerId = player.getUniqueId();
        GroundWar war = GroundWarManager.getGroundWar(playerId);

        if (war.getPlotA() != null && war.getPlotB() != null) {
            GroundWarManager.confirmGroundWar(war);
        }

        return false;
    }
}
