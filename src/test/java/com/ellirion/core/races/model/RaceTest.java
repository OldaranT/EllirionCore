package com.ellirion.core.races.model;

import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class RaceTest {

    private static UUID PLAYER_ID = UUID.randomUUID();
    private static String RACE_NAME = "Slime";
    private static ChatColor TEAMCOLOR = ChatColor.AQUA;

    @Test
    public void addPlayer_whenAddingAPlayerToTeam_shouldReturnTrueAndAddPlayerToSet() {
        Race race = new Race(RACE_NAME, TEAMCOLOR);
        Set<UUID> expected = new HashSet<>();
        expected.add(PLAYER_ID);

        boolean result = race.addPlayer(PLAYER_ID);
        Set<UUID> actual = race.getPlayers();

        assertTrue(result);
        assertEquals(expected, actual);
    }

    @Test
    public void addPlayer_whenAddingAnExistingPlayer_shouldReturnFalseAndNotAddThePlayer() {
        Race race = new Race(RACE_NAME, TEAMCOLOR);
        Set<UUID> expected = new HashSet<>();
        expected.add(PLAYER_ID);
        race.addPlayer(PLAYER_ID);

        boolean result = race.addPlayer(PLAYER_ID);
        Set<UUID> actual = race.getPlayers();

        assertFalse(result);
        assertEquals(expected, actual);
    }

    @Test
    public void hasPlayer_whenPlayerAdded_shouldReturnTrue() {
        Race race = new Race(RACE_NAME, TEAMCOLOR);
        race.addPlayer(PLAYER_ID);

        boolean result = race.hasPlayer(PLAYER_ID);

        assertTrue(result);
    }

    @Test
    public void hasPlayer_whenPlayerNotAdded_shouldReturnFalse() {
        Race race = new Race(RACE_NAME, TEAMCOLOR);
        race.addPlayer(UUID.randomUUID());

        boolean result = race.hasPlayer(PLAYER_ID);

        assertFalse(result);
    }
}
