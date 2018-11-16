package com.ellirion.core.gamemanager;

import lombok.Getter;
import lombok.Setter;
import com.ellirion.core.gamemanager.setup.Step;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.race.RaceManager;

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
    @Getter private int gameID;
    @Getter private GameState state;
    private Step[] setupSteps;
    private int currentStep;
    @Getter @Setter private int plotSize;

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
        setupSteps[0] = new Step("Create plots", f -> PlotManager.getSavedPlots().size() > 0);
        setupSteps[1] = new Step("Assign trading center plots");
        setupSteps[2] = new Step("Create races", f -> RaceManager.getRaceCount() >= 2);
        setupSteps[3] = new Step("Confirm setup");
        currentStep = 0;
    }

    /**
     * Get the current setup step.
     * @return the current setup step
     */
    public String currentStepMessage() {
        return getCurrentStep().getMessage();
    }

    /**
     * Get the current setup step of the gamemode.
     * @return the current setup step of the gamemode
     */
    public Step getCurrentStep() {
        return setupSteps[currentStep];
    }

    /**
     * Change the state of the game.
     * @param newState the new state of the game
     */
    public void changeState(GameState newState) {
        state = newState;
    }

    /**
     * Advance to the next step of the setup.
     * @return whether the currentStep was advanced.
     */
    public Boolean nextStep() {
        //setupSteps.length - 1 because last step is confirm
        if (currentStep >= setupSteps.length - 1) {
            return false;
        }
        Step step = setupSteps[currentStep];
        if (step.requirementComplete(new Object())) {
            currentStep++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Confirm the gamemode.
     */
    public void confirmGamemode() {
        //Validate gamemode?

        changeState(GameState.SAVING);

        //TODO Save the game

        changeState(GameState.IN_PROGRESS);
    }

    /**
     * Create a string of the current game state.
     * @return the current game state
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("Current game state: ").append(state.name());
        if (state == GameState.SETUP) {
            sb.append("\nCurrent step:\n-").append(currentStepMessage());
        }
        return sb.toString();
    }
}
