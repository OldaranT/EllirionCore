package com.ellirion.core.race;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RaceManager {

    private static HashMap<UUID, Race> RACES = new HashMap<>();
    private static Set<ChatColor> USED_COLORS = new HashSet<>();
    @Getter private static Set<ChatColor> AVAILABLE_COLORS = new HashSet<>(initAvailableColors());
    private static Race DEFAULT_RACE;
    private static HashMap<UUID, String> RACE_ID_NAME = new HashMap<>();
    private static DatabaseManager DATABASE_MANAGER = EllirionCore.getINSTANCE().getDbManager();

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
        setColorToUsed(ChatColor.DARK_GRAY);
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
        if (USED_COLORS.contains(color) || raceExists(name)) {
            return false;
        }

        name = normalCasing(name);

        Race race = new Race(name, color, homePlot);
        RACES.putIfAbsent(race.getRaceUUID(), race);
        setColorToUsed(color);
        homePlot.setOwner(race);
        RACE_ID_NAME.put(race.getRaceUUID(), race.getName());
        return createRaceInDB(race);
    }

    private static boolean createRaceInDB(Race race) {
        return DATABASE_MANAGER.createRace(race);
    }

    /**
     * @param raceName The name of the race.
     * @return Return true if the race exists else false.
     */
    public static boolean raceExists(String raceName) {
        raceName = normalCasing(raceName);
        return RACE_ID_NAME.containsValue(raceName);
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
        if (raceExists(newName)) {
            return false;
        }
        UUID raceID = race.getRaceUUID();
        RACE_ID_NAME.put(raceID, newName);
        race.setName(newName);
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
    @Deprecated
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
        return StringHelper.normalNameCasing(string);
    }

    /**
     * This function deletes the specified race.
     * @param raceName The name of the race to remove.
     * @return Return true if successfully removed.
     */
    public static boolean deleteRace(String raceName) {
        raceName = normalCasing(raceName);
        UUID raceID = getUUIDbyName(raceName);
        return deleteRace(raceID);
    }

    /**
     * This function deletes the specified race.
     * @param raceID The UUID of the race to delete.
     * @return Return if the deletion was successful or not.
     */
    public static boolean deleteRace(UUID raceID) {
        Race race = getRaceByID(raceID);
        Set<UUID> players = race.getPlayers();

        for (UUID id : players) {
            PlayerManager.setPlayerRace(id, null);
        }

        setColorToAvailable(race.getTeamColor());
        RACES.remove(raceID);

        for (Plot plot : race.getPlots()) {
            plot.setOwner(Wilderness.getInstance());
        }

        RACE_ID_NAME.remove(raceID);
        return true;
    }

    private static void setColorToUsed(ChatColor color) {
        USED_COLORS.add(color);
        AVAILABLE_COLORS.remove(color);
    }

    private static void setColorToAvailable(ChatColor color) {
        USED_COLORS.remove(color);
        AVAILABLE_COLORS.add(color);
    }

    private static List<ChatColor> initAvailableColors() {
        List<ChatColor> result = new ArrayList<>();
        List<String> forbidden = Arrays.asList("§k", "§l", "§m", "§n", "§o", "§r");
        for (ChatColor color : ChatColor.values()) {
            if (forbidden.contains(color.toString())) {
                continue;
            }
            result.add(color);
        }

        return result;
    }
}
