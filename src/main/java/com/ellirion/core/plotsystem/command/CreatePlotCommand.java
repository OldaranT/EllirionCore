package com.ellirion.core.plotsystem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.util.EllirionUtil;
import com.ellirion.util.async.Promise;

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
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        // Check if a name was entered
        if (args.length < 4 || args.length > 4) {
            player.sendMessage(ChatColor.DARK_RED +
                               "Please give the template a name with the following arguments: <PLOT-SIZE> <MAP-RADIUS> <CENTER-X> <CENTER-Z>");
            return true;
        }
        int plotSize = Integer.parseInt(args[0]);
        int mapRadius = Integer.parseInt(args[1]);
        int centerX = Integer.parseInt(args[2]);
        int centerZ = Integer.parseInt(args[3]);

        //Check if plotsize is chunksize(16) friendly
        if (plotSize <= 0 || plotSize % 16 != 0) {
            player.sendMessage(ChatColor.DARK_RED + "Make sure your plot size is not negative.");
            player.sendMessage(ChatColor.DARK_RED + "The plot size needs to be a factor of 16. Like : 16,32,48,64,128");
            return true;
        }

        PlotManager.setPLOT_SIZE(plotSize);

        Promise<Boolean> createPlotsPromise = PlotManager.createPlots(player.getWorld(), mapRadius, centerX, centerZ);
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

