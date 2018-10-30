package com.ellirion.core.gamemanager;

import lombok.Getter;
import com.ellirion.core.gamemanager.setup.Step;

public class GameManager {
    public enum GameState {
        NOT_STARTED,
        SETUP,
        IN_PROGRESS,
        SAVING,
        LOADING,
        FINISHED
    }

    private static GameManager INSTANCE;
    @Getter private GameState state;
    private Step[] setupSteps;
    private int currentStep;

    private GameManager() {
        state = GameState.NOT_STARTED;
        init();
    }

    /**
     * Get the GameManager instance.
     * @return the gamemanager instance
     */
    public static GameManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }

    private void init() {
        createSteps();
    }

    private void createSteps() {
        setupSteps = new Step[4];
        setupSteps[0] = new Step("Create plots");
        setupSteps[1] = new Step("Assign trading center plots");
        setupSteps[2] = new Step("Create races");
        setupSteps[3] = new Step("Confirm setup");
        currentStep = 0;
    }

    /**
     * Get the current setup step.
     * @return the current setup step
     */
    public String currentStep() {
        return setupSteps[currentStep].getMessage();
    }

    /**
     * Change the state of the game.
     * @param newState the new state of the game
     */
    public void changeState(GameState newState) {
        state = newState;
    }

    /**
     * Create a string of the current game state.
     * @return the current game state
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("Current game state: ").append(state.name());
        if (state == GameState.SETUP) {
            sb.append("\nCurrent step:\n-").append(currentStep());
        }
        return sb.toString();
    }
}
