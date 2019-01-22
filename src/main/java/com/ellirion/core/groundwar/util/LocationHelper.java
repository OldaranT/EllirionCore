package com.ellirion.core.groundwar.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import com.ellirion.core.EllirionCore;

import java.util.ArrayList;
import java.util.List;

public class LocationHelper {

    /**
     * This checks if the location is a safe place to spawn too.
     * @param location This is the location that is going to be checked.
     * @return Return true if safe to teleport or false if not safe.
     */
    public static boolean checkLocation(Location location) {
        List<String> allowedBlocks = EllirionCore.getINSTANCE().getConfig().getStringList(
                "GroundWar.DefaultAllowedTeleportBlocks");

        List<Material> allowedMaterial = new ArrayList<>();

        for (String block : allowedBlocks) {
            allowedMaterial.add(Material.getMaterial(block));
        }

        return allowedMaterial.contains(location.getBlock().getType()) &&
               !location.getBlock().getRelative(0, 1, 0).getType().isSolid() &&
               !location.getBlock().getRelative(0, 2, 0).getType().isSolid();
    }

    /**
     * This gets a safe location to teleport to from a given location.
     * @param location The location to start looking from.
     * @return Return the found safe teleport location.
     */
    public static Location getSafeLocation(Location location) {
        Location safeLocation = location.getWorld().getHighestBlockAt(location).getLocation();
        boolean isAllowedToTeleport = false;
        while (!isAllowedToTeleport) {
            if (checkLocation(safeLocation)) {
                isAllowedToTeleport = true;
            } else {
                safeLocation.setY(safeLocation.getBlockY() - 1);
            }
        }
        return safeLocation;
    }

    /**
     * This gets a safe location to teleport to with the given world and coordinates.
     * @param world The world to get the location from.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return Return the found safe location.
     */
    public static Location getSafeLocation(World world, int x, int z) {
        Location teleportToLocation = world.getHighestBlockAt(x, z).getLocation();
        return getSafeLocation(teleportToLocation);
    }
}
