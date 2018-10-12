package com.ellirion.core.races;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.Wilderness;
import com.ellirion.core.races.model.Race;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RaceManager {

    private static HashMap<UUID, Race> RACES = new HashMap<>();
    private static Set<ChatColor> USED_COLORS = new HashSet<>();
    private static Race DEFAULT_RACE;
    @Getter private static HashSet<String> RACENAMES = new HashSet<>();
    private static HashMap<UUID, String> RACE_ID_NAME = new HashMap<>();

    /**
     * @param defaultRaceName The name of the default race.
     * @return Return true if successful.
     */
    public static boolean createDefaultRace(String defaultRaceName) {
        if (defaultRaceExists()) {
            return false;
        }
        defaultRaceName = normalCasing(defaultRaceName);
        Race race = new Race(defaultRaceName, ChatColor.DARK_GRAY, null);
        RACES.put(race.getRaceUUID(), race);
        DEFAULT_RACE = race;
        USED_COLORS.add(ChatColor.DARK_GRAY);
        RACENAMES.add(defaultRaceName);
        RACE_ID_NAME.put(race.getRaceUUID(), race.getName());
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
     * @param homePlot The homeplot of the race.
     * @return Return true if successfully added the race.
     */
    public static boolean addRace(String name, ChatColor color, Plot homePlot) {
        if (USED_COLORS.contains(color) || raceNameExists(name)) {
            return false;
        }

        name = normalCasing(name);

        Race race = new Race(name, color, homePlot);
        RACENAMES.add(name);
        RACES.putIfAbsent(race.getRaceUUID(), race);
        USED_COLORS.add(color);
        homePlot.setOwner(race);
        RACE_ID_NAME.put(race.getRaceUUID(), race.getName());
        return true;
    }

    /**
     * @param raceName The name of the race.
     * @return Return true if the race exists else false.
     */
    public static boolean raceNameExists(String raceName) {
        raceName = normalCasing(raceName);
        return RACENAMES.contains(raceName);
    }

    /**
     * @param raceID The ID of the race.
     * @return Return true if the race exists.
     */
    public static boolean raceExists(UUID raceID) {
        return RACES.containsKey(raceID);
    }

    /**
     * @param race The race you want to change the name of.
     * @param newName The new name of the race.
     * @return Return true if successfully changed the name.
     */
    public static boolean changeRaceName(Race race, String newName) {
        newName = normalCasing(newName);
        if (raceNameExists(newName)) {
            return false;
        }
        RACENAMES.remove(race.getName());
        race.setName(newName);
        RACENAMES.add(newName);
        return true;
    }

    /**
     * @param player The player to add to the race.
     * @param raceID The UUID of the race to add the player to.
     * @return return true of successfully added player to race.
     */
    public static boolean addPlayerToRace(UUID player, UUID raceID) {
        if (!raceExists(raceID)) {
            return false;
        }

        Race race = RACES.get(raceID);
        race.addPlayer(player);
        return true;
    }

    /**
     * @param playerID The ID of the player to move.
     * @param oldRace The UUID of the old race of the player.
     * @param newRace The UUID of the new race of the player.
     * @return Return true if successful.
     */
    public static boolean changePlayerRace(UUID playerID, UUID oldRace, UUID newRace) {
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

    private static UUID getUUIDbyName(String name) {
        for (final Iterator<Map.Entry<UUID, String>> iter = RACE_ID_NAME.entrySet().iterator(); iter.hasNext();/**/) {
            Map.Entry<UUID, String> entry = iter.next();
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @param raceID The ID of the race to get.
     * @return Return the race.
     */
    public static Race getRaceByID(UUID raceID) {
        return RACES.get(raceID);
    }

    private static String normalCasing(String string) {
        if (string == null || string.length() == 0) {
            return "";
        }
        String firstLetter = ("" + string.charAt(0)).toUpperCase();
        String nonFirstLetter = string.substring(1).toLowerCase();
        return (firstLetter + nonFirstLetter);
    }

    /**
     * @param raceName The name of the race to remove.
     * @return Return true if successfully removed.
     */
    public static boolean deleteRaceByName(String raceName) {
        raceName = normalCasing(raceName);
        UUID raceID = getUUIDbyName(raceName);
        Race race = getRaceByID(raceID);
        Set<UUID> players = race.getPlayers();

        for (UUID id : players) {
            PlayerManager.setPlayerRace(id, null);
        }

        USED_COLORS.remove(race.getTeamColor());
        RACENAMES.remove(raceName);
        RACES.remove(raceID);

        for (Plot plot : race.getPlots()) {
            plot.setOwner(Wilderness.getInstance());
        }

        RACE_ID_NAME.remove(raceID);
        return true;
    }
}
