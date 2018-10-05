package com.ellirion.core.races;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.races.model.RaceModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RaceManager {

    private static HashMap<String, RaceModel> RACES = new HashMap<>();
    private static Set<ChatColor> USED_COLORS = new HashSet<>();
    private static RaceModel DEFAULT_RACE;

    /**
     * @param defaultRaceName The name of the default race.
     * @return Return true if successful.
     */
    public static boolean createDefaultRace(String defaultRaceName) {
        if (defaultRaceExists()) {
            return false;
        }
        RaceModel r = new RaceModel(defaultRaceName, ChatColor.DARK_GRAY);
        RACES.put(r.getRaceName(), r);
        DEFAULT_RACE = r;
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
     * @return Return true if successful
     */
    public static boolean addRace(String name, ChatColor color) {
        if (USED_COLORS.contains(color) || raceExists(name)) {
            return false;
        }
        RaceModel r = new RaceModel(name, color);
        RACES.putIfAbsent(name, r);
        USED_COLORS.add(color);
        return true;
    }

    /**
     * @param raceName The name of the race.
     * @return Return the result.
     */
    public static boolean raceExists(String raceName) {
        return RACES.containsKey(raceName);
    }

    /**
     * @param name The current name of the race.
     * @param newName The new name of the race.
     * @return Return true if successful.
     */
    public static boolean changeRaceName(String name, String newName) {
        if (raceExists(newName)) {
            return false;
        }
        RaceModel r = RACES.remove(name);
        r.setRaceName(newName);
        RACES.putIfAbsent(newName, r);
        return true;
    }

    public static Set<String> getRaceNames() {
        return RACES.keySet();
    }

    /**
     * @param p The player to add to the race.
     * @param raceName The name of the race to add the player to.
     * @return return true of successful.
     */
    public static boolean addPlayerToRace(Player p, String raceName) {
        if (!raceExists(raceName) || hasRace(p)) {
            return false;
        }

        RaceModel r = RACES.get(raceName);
        r.addPlayer(p.getUniqueId());
        p.setDisplayName(r.getTeamColor() + "[" + raceName + "] " + ChatColor.RESET + p.getName());
        return true;
    }

    /**
     * @param p The player to move.
     * @param oldRace The old race of the player.
     * @param newRace The new race of the player.
     * @return Return true if successful.
     */
    public static boolean movePlayerToRace(Player p, String oldRace, String newRace) {
        UUID playerID = p.getUniqueId();
        if (!raceExists(oldRace) || !raceExists(newRace) || !RACES.get(oldRace).hasPlayer(playerID)) {
            return false;
        }
        RACES.get(oldRace).removePlayer(playerID);
        return RACES.get(newRace).addPlayer(playerID);
    }

    /**
     * @param p The player to check.
     * @return Return true if player has a race.
     */
    public static boolean hasRace(Player p) {
        for (RaceModel r : RACES.values()) {
            if (r.hasPlayer(p.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param p The player to get the race from.
     * @return The player race.
     */
    public static RaceModel getPlayerRace(Player p) {
        for (RaceModel r : RACES.values()) {
            if (r.hasPlayer(p.getUniqueId())) {
                return r;
            }
        }
        return null;
    }

    /**
     * @return Return the default race.
     */
    public static RaceModel getDefaultRace() {
        if (!defaultRaceExists()) {
            createDefaultRace("outsiders");
            return DEFAULT_RACE;
        }
        return DEFAULT_RACE;
    }

    /**
     * @param color The chatcolor to check.
     * @return Return true if it is in use.
     */
    public static boolean isColerInUse(ChatColor color) {
        return USED_COLORS.contains(color);
    }

    /**
     * @param raceName The name of the race.
     * @return Return the race if it exists.
     */
    public static RaceModel getRaceByName(String raceName) {
        if (!raceExists(raceName)) {
            return DEFAULT_RACE;
        }
        return RACES.get(raceName);
    }
}
