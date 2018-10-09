package com.ellirion.core.races;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.races.model.Race;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RaceManager {

    private static HashMap<String, Race> RACES = new HashMap<>();
    private static Set<ChatColor> USED_COLORS = new HashSet<>();
    private static Race DEFAULT_RACE;

    /**
     * @param defaultRaceName The name of the default race.
     * @return Return true if successful.
     */
    public static boolean createDefaultRace(String defaultRaceName) {
        if (defaultRaceExists()) {
            return false;
        }
        Race race = new Race(defaultRaceName, ChatColor.DARK_GRAY);
        RACES.put(race.getRaceName(), race);
        DEFAULT_RACE = race;
        USED_COLORS.add(ChatColor.DARK_GRAY);
        return true;
    }

    private static boolean defaultRaceExists() {
        if (DEFAULT_RACE == null) {
            return false;
        }
        return true;
    }

    /**
     * @param name The name of the team.
     * @param color The color of the team.
     * @return Return true if successfully added the race.
     */
    public static boolean addRace(String name, ChatColor color) {
        if (USED_COLORS.contains(color) || raceExists(name)) {
            return false;
        }
        Race race = new Race(name, color);
        RACES.putIfAbsent(name, race);
        USED_COLORS.add(color);
        return true;
    }

    /**
     * @param raceName The name of the race.
     * @return Return true if the race exists else false.
     */
    public static boolean raceExists(String raceName) {
        return RACES.containsKey(raceName);
    }

    /**
     * @param name The current name of the race.
     * @param newName The new name of the race.
     * @return Return true if successfully changed the name.
     */
    public static boolean changeRaceName(String name, String newName) {
        if (raceExists(newName)) {
            return false;
        }
        Race race = RACES.remove(name);
        race.setRaceName(newName);
        RACES.putIfAbsent(newName, race);
        return true;
    }

    public static Set<String> getRaceNames() {
        return RACES.keySet();
    }

    /**
     * @param player The player to add to the race.
     * @param raceName The name of the race to add the player to.
     * @return return true of successfully added player to race.
     */
    public static boolean addPlayerToRace(Player player, String raceName) {
        if (!raceExists(raceName)) {
            player.sendMessage("Race does not exist.");
            return false;
        } else if (hasRace(player)) {
            player.sendMessage("You are already in a race.");
            return false;
        }

        Race race = RACES.get(raceName);
        race.addPlayer(player.getUniqueId());
        player.setDisplayName(race.getTeamColor() + "[" + raceName + "] " + ChatColor.RESET + player.getName());
        return true;
    }

    /**
     * @param player The player to move.
     * @param oldRace The old race of the player.
     * @param newRace The new race of the player.
     * @return Return true if successful.
     */
    public static boolean movePlayerToRace(Player player, String oldRace, String newRace) {
        UUID playerID = player.getUniqueId();
        if (!raceExists(oldRace) || !raceExists(newRace) || !RACES.get(oldRace).hasPlayer(playerID)) {
            return false;
        }
        RACES.get(oldRace).removePlayer(playerID);
        return RACES.get(newRace).addPlayer(playerID);
    }

    /**
     * @param player The player to check.
     * @return Return true if player has a race.
     */
    public static boolean hasRace(Player player) {
        for (Race race : RACES.values()) {
            if (race.hasPlayer(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param player The player to get the race from.
     * @return The player race.
     */
    public static Race getPlayerRace(Player player) {
        for (Race race : RACES.values()) {
            if (race.hasPlayer(player.getUniqueId())) {
                return race;
            }
        }
        return null;
    }

    /**
     * @return Return the default race.
     */
    public static Race getDefaultRace() {
        if (!defaultRaceExists()) {
            createDefaultRace("outsiders");
            return DEFAULT_RACE;
        }
        return DEFAULT_RACE;
    }

    /**
     * @param color The chatcolor to check.
     * @return Return true if the color is in use.
     */
    public static boolean isColerInUse(ChatColor color) {
        return USED_COLORS.contains(color);
    }

    /**
     * @param raceName The name of the race.
     * @return Return the race if it exists.
     */
    public static Race getRaceByName(String raceName) {
        if (!raceExists(raceName)) {
            return DEFAULT_RACE;
        }
        return RACES.get(raceName);
    }
}
