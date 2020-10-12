package com.ellirion.core.race.eventlistener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.ellirion.core.playerdata.PlayerHelper;

import java.util.UUID;

public class OnFriendlyFire implements Listener {

    /**
     * @param event The event that fires when one entity damages another.
     */
    @EventHandler
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity defender = event.getEntity();

        if (defender instanceof Player && attacker instanceof Player) {
            UUID attackerID = attacker.getUniqueId();
            UUID defenderID = defender.getUniqueId();

            if (PlayerHelper.comparePlayerTeams(attackerID, defenderID)) {
                event.setCancelled(true);
            }
        }
    }
}
