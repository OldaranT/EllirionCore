package com.ellirion.core.gamemanager.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.gamemanager.setup.Step;

public class NextSetupStepCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        GameManager manager = GameManager.getInstance();
        if (manager.getState() != GameManager.GameState.SETUP) {
            commandSender.sendMessage(ChatColor.DARK_RED + "The game mode is currently not in the SETUP phase, therefore there is no next step");
            return true;
        }
        Step step = manager.getCurrentStep();
        if (step.getRequirement() && !step.requirementComplete(new Object())) {
            commandSender.sendMessage(ChatColor.DARK_RED + "This step has requirements that have not been met yet");
            return true;
        }

        manager.nextStep();

        return true;

    }
}
