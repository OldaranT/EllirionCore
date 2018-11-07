package com.ellirion.core.race.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.ellirion.core.race.RaceManager;

import java.util.ArrayList;
import java.util.List;

public class CreateRaceTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> availableColors = new ArrayList<>();

        RaceManager.getAVAILABLE_COLORS().forEach(chatColor -> availableColors.add(chatColor.name()));
        availableColors.forEach(s -> { });
    }
}
