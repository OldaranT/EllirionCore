package com.ellirion.core.gamemanager.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.ellirion.core.gamemanager.GameManager;

import java.util.ArrayList;
import java.util.List;

public class GameNameTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        final List<String> completions = new ArrayList<>();
        List<String> allGameNames = new ArrayList<>();

        if (args.length == 1) {
            GameManager.getGAMES().forEach(((uuid, game) -> allGameNames.add(game.getUName())));
            allGameNames.forEach(s -> {
                if (s.startsWith(args[args.length - 1].toUpperCase())) {
                    completions.add(s);
                }
            });
        }
        if (completions.isEmpty()) {
            completions.addAll(allGameNames);
        }
        return completions;
    }
}
