package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.gamemanager.GameManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.util.EllirionUtil;
import com.ellirion.util.async.Promise;

import java.util.HashMap;
import java.util.List;

public class CreatePlotCommand implements CommandExecutor {

    private JavaPlugin plugin;

    /**
     * Constructor for this command executor.
     * @param plugin the main class.
     */
    public CreatePlotCommand(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        GameManager gameManager = GameManager.getInstance();
        if (gameManager.getState() != GameManager.GameState.SETUP ||
            !gameManager.currentStepMessage().equals(GameManager.getCREATE_PLOT())) {
            player.sendMessage(ChatColor.DARK_RED +
                               "You are either not in correct STATE or STEP.\n" + gameManager.toString());
            return true;
        }

        // Check if a name was entered
        if (strings.length < 4 || strings.length > 4) {
            player.sendMessage(ChatColor.DARK_RED +
                               "To create a plot use the following arguments: \n " +
                               "<PLOT-SIZE> <MAP-RADIUS> <CENTER-X> <CENTER-Z>");
            return true;
        }
        int plotSize = Integer.parseInt(strings[0]);
        int mapRadius = Integer.parseInt(strings[1]);
        int centerX = Integer.parseInt(strings[2]);
        int centerZ = Integer.parseInt(strings[3]);

        //Check if plotsize is chunksize(16) friendly
        if (plotSize <= 0 || plotSize % 16 != 0) {
            player.sendMessage(ChatColor.DARK_RED + "Make sure your plot size is not negative.");
            player.sendMessage(ChatColor.DARK_RED + "The plot size needs to be a factor of 16. Like : 16,32,48,64,128");
            return true;
        }

        gameManager.setPlotSize(plotSize);
        gameManager.setXOffset(centerX);
        gameManager.setZOffset(centerZ);

        Promise<Boolean> createPlotsPromise = new Promise(f -> {
            List<Plot> plots = PlotManager.createPlots(player.getWorld(), mapRadius);
            HashMap<PlotCoord, Plot> plotMap = PlotManager.getSavedPlots();
            for (Plot plot : plots) {
                plotMap.put(plot.getPlotCoord(), plot);
            }
            f.resolve(plots != null);
        }, true);
        EllirionUtil util = (EllirionUtil) plugin.getServer().getPluginManager().getPlugin("EllirionUtil");
        util.schedulePromise(createPlotsPromise).then(f -> {
            if (!createPlotsPromise.getResult()) {
                player.sendMessage(ChatColor.DARK_RED + "Something went wrong with saving the plots.");
            } else {
                player.sendMessage(ChatColor.GREEN + "Plots have been created.");
            }
        });

        commandSender.sendMessage("Plots are now being created....");

        return true;
    }
}

