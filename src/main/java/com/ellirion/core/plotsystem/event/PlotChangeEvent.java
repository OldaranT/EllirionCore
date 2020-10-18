package com.ellirion.core.plotsystem.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.ellirion.core.plotsystem.model.Plot;

public class PlotChangeEvent extends Event {

    private static HandlerList HANDLERLIST = new HandlerList();
    @Getter private final Player player;
    @Getter private final Plot plotFrom;
    @Getter private final Plot plotTo;

    /**
     * Create plot change event.
     * @param player player that triggered the event
     * @param plotFrom plot where they player is moving from.
     * @param plotTo plot where the player is moving to.
     */
    public PlotChangeEvent(final Player player, final Plot plotFrom, final Plot plotTo) {
        this.player = player;
        this.plotFrom = plotFrom;
        this.plotTo = plotTo;
    }

    /**
     * Return handerlist of the event.
     * @return handlerlist.
     */
    public static HandlerList getHandlerList() {
        return HANDLERLIST;
    }

    /**
     * Create a call to trigger the event.
     */
    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERLIST;
    }
}
