package com.ellirion.core.races.eventlistener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.ellirion.core.playerdata.PlayerManager;

import java.util.UUID;

public class OnFriendlyFire implements Listener {

    /**
     * @param event The event that fires when one entity damages another.
     */
    @EventHandler
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if (damaged instanceof Player && damager instanceof Player) {
            UUID damagerID = damager.getUniqueId();
            UUID damagedID = damaged.getUniqueId();

            if (PlayerManager.comparePlayerTeams(damagerID, damagedID)) {
                event.setCancelled(true);
            }
        }
    }
}
