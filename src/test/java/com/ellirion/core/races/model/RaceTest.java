package com.ellirion.core.races.model;

import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class RaceModelTest {

    private static UUID PLAYER_ID = UUID.randomUUID();
    private static String RACE_NAME = "Slime";
    private static ChatColor TEAMCOLOR = ChatColor.AQUA;

    @Test
    public void addPlayer_whenAddingAPlayerToTeam_shouldReturnTrueAndAddPlayerToSet() {
        RaceModel race = new RaceModel(RACE_NAME, TEAMCOLOR);
        Set<UUID> expected = new HashSet<>();
        expected.add(PLAYER_ID);

        boolean result = race.addPlayer(PLAYER_ID);
        Set<UUID> actual = race.getPlayers();

        assertTrue(result);
        assertEquals(expected, actual);
    }

    @Test
    public void addPlayer_whenAddingAnExistingPlayer_shouldReturnFalseAndNotAddThePlayer() {
        RaceModel race = new RaceModel(RACE_NAME, TEAMCOLOR);
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
        RaceModel race = new RaceModel(RACE_NAME, TEAMCOLOR);
        race.addPlayer(PLAYER_ID);

        boolean result = race.hasPlayer(PLAYER_ID);

        assertTrue(result);
    }

    @Test
    public void hasPlayer_whenPlayerNotAdded_shouldReturnFalse() {
        RaceModel race = new RaceModel(RACE_NAME, TEAMCOLOR);
        race.addPlayer(UUID.randomUUID());

        boolean result = race.hasPlayer(PLAYER_ID);

        assertFalse(result);
    }
}
