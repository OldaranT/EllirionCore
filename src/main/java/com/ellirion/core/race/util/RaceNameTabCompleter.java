package com.ellirion.core.race.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.ellirion.core.race.RaceHelper;

import java.util.ArrayList;
import java.util.List;

public class RaceNameTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            RaceHelper.getAllRaceNames().forEach(name -> {
                if (name.startsWith(args[args.length - 1])) {
                    completions.add(name);
                }
            });
        }
        if (completions.isEmpty()) {
            completions.addAll(RaceHelper.getAllRaceNames());
        }

        return completions;
    }
}
