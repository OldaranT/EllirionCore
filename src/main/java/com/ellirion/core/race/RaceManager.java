package com.ellirion.core.race;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.playerdata.PlayerManager;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.Plot;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.Wilderness;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.util.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatch.*;

public class RaceManager {

    private static HashMap<UUID, Race> RACES = new HashMap<>();
    private static Set<ChatColor> USED_COLORS = new HashSet<>();
    @Getter private static Set<ChatColor> AVAILABLE_COLORS = new HashSet<>(initAvailableColors());
    private static HashMap<UUID, String> RACE_ID_NAME = new HashMap<>();
    private static DatabaseManager DATABASE_MANAGER = EllirionCore.getINSTANCE().getDbManager();

    /**
     * @param name The name of the race.
     * @param alias The alias of the race.
     * @param color The color of the race.
     * @param homePlot The homeplot of the race.
     * @return Return true if successfully added the race.
     */
    public static boolean addRace(String name, String alias, ChatColor color, Plot homePlot) {
        if (USED_COLORS.contains(color) || raceExists(name)) {
            return false;
        }

        name = normalCasing(name);

        Race race = new Race(name, alias, color, homePlot);
        RACES.putIfAbsent(race.getRaceUUID(), race);
        setColorToUsed(color);
        homePlot.setOwner(race);
        // This should return null because if it returns a value then it has replaced something.
        return RACE_ID_NAME.putIfAbsent(race.getRaceUUID(), race.getName()) == null;
    }

    /**
     * Add a race from the database.
     * @param databaseRace The database object to be converted into a race.
     * @return return true if the operation is successful.
     */
    public static boolean addRace(RaceDBModel databaseRace) {
        return tryCatch(() -> {
            Race race = new Race(databaseRace);
            RACES.putIfAbsent(race.getRaceUUID(), race);
            setColorToUsed(race.getTeamColor());
            Plot homePlot = race.getHomePlot();
            homePlot.setOwner(race);
            race.setAlias(databaseRace.getRaceAlias());
            RACE_ID_NAME.put(race.getRaceUUID(), race.getName());
        });
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
     * @param color The chatcolor to check.
     * @return Return true if the color is in use.
     */
    public static boolean isColerInUse(ChatColor color) {
        return USED_COLORS.contains(color);
    }

    /**
     * Gets the UUID of a race by name.
     * @param name name of the race to get UUID for.
     * @return UUID of the race.
     */
    public static UUID getRaceUUID(String name) {
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
        UUID raceID = getRaceUUID(raceName);
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

        for (final Iterator<PlotCoord> iterator = race.getPlotCoords().iterator(); iterator.hasNext();/**/) {
            Plot plot = PlotManager.getPlotByCoordinate(iterator.next());
            plot.setOwner(Wilderness.getInstance());
        }

        RACE_ID_NAME.remove(raceID);
        DATABASE_MANAGER.deleteRace(raceID);
        return true;
    }

    /**
     * Get the amount of races.
     * @return the amount of races.
     */
    public static int getRaceCount() {
        return RACES.size();
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

    public static List<String> getAllRaceNames() {
        return new ArrayList<>(RACE_ID_NAME.values());
    }

    /**
     * Remove all races.
     */
    public static void removeAllRaces() {
        RACES.clear();
        RACE_ID_NAME.clear();
        USED_COLORS.clear();
        AVAILABLE_COLORS = new HashSet<>(initAvailableColors());
    }

    public static Collection<Race> getRaces() {
        return RACES.values();
    }

    /**
     * Load races from database models.
     * @param races the database models of the races
     */
    public static void loadRaces(List<RaceDBModel> races) {
        for (RaceDBModel race : races) {
            addRace(race);
        }
    }
}
