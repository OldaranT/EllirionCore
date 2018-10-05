package com.ellirion.core.playerdata;

import org.junit.Test;
import com.ellirion.core.playerdata.model.PlayerModel;

import java.util.UUID;

import static org.junit.Assert.*;

public class PlayerModelTest {

    private static UUID R_UUID = UUID.randomUUID();
    private static String NAME = "john";
    private static String RANK = "outsider";

    @Test
    public void changeCash_whenNotEnoughCash_shouldReturnFalseAndNotChangeCash() {
        PlayerModel model = new PlayerModel(R_UUID, NAME, RANK, 0);

        boolean result = model.changeCash(-1);
        int cash = model.getCash();

        assertFalse(result);
        assertEquals(0, cash);
    }

    @Test
    public void changeCash_whenEnoughCash_shouldReturnTrueAndChangeCash() {
        PlayerModel model = new PlayerModel(R_UUID, NAME, RANK, 5);

        boolean result = model.changeCash(-1);
        int cash = model.getCash();

        assertTrue(result);
        assertEquals(4, cash);
    }

    @Test
    public void changeCash_whenAddingCash_shouldIncreaseCash() {
        PlayerModel model = new PlayerModel(R_UUID, NAME, RANK, 0);

        boolean result = model.changeCash(1);
        int cash = model.getCash();

        assertTrue(result);
        assertEquals(1, cash);
    }
}
