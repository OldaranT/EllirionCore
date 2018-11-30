package com.ellirion.core.gamemanager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.database.model.GameDBModel;
import com.ellirion.core.database.model.PlotCoordDBModel;
import com.ellirion.core.database.model.RaceDBModel;
import com.ellirion.core.database.model.TradingCenterDBModel;
import com.ellirion.core.gamemanager.model.Game;
import com.ellirion.core.gamemanager.setup.Step;
import com.ellirion.core.plotsystem.PlotManager;
import com.ellirion.core.plotsystem.model.PlotCoord;
import com.ellirion.core.plotsystem.model.plotowner.TradingCenter;
import com.ellirion.core.race.RaceManager;
import com.ellirion.core.race.model.Race;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatch.*;

public class GameManager {

    private static GameManager INSTANCE;

    //Games
    @Getter private static HashMap<UUID, Game> GAMES = new HashMap<>();

    //Setup
    private Step[] setupSteps;
    private int currentStep;
    @Getter private GameState state;

    //Game Data
    @Getter @Setter private Game game;
    @Getter @Setter private UUID gameID;
    @Getter @Setter private String uName;
    @Getter @Setter private int plotSize;
    @Getter @Setter private int xOffset;
    @Getter @Setter private int zOffset;

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

    /**
     * adds game to GAMES.
     * @param gameDBModel the game to add.
     */
    public static void addGame(GameDBModel gameDBModel) {
        GAMES.put(gameDBModel.getGameID(), new Game(gameDBModel));
    }

    private void init() {
        createSteps();
        state = GameState.NOT_STARTED;
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
        DatabaseManager db = EllirionCore.getINSTANCE().getDbManager();

        //Save game
        game = new Game(UUID.randomUUID(), uName, xOffset, zOffset, plotSize);
        GAMES.put(game.getGameID(), game);
        db.createGame(game);

        //save plots
        for (PlotCoord plotCoord : PlotManager.getSavedPlots().keySet()) {
            EllirionCore.getINSTANCE().getLogger().info(plotCoord.toString());
            db.savePlotCoord(game.getGameID(), plotCoord);
        }

        //save races
        for (Race race : RaceManager.getRaces()) {
            db.createRace(race);
        }

        //save tradingcenter
        db.saveTradingCenter(TradingCenter.getInstance());

        changeState(GameState.IN_PROGRESS);
    }

    /**
     * Cancel the gamemode setup.
     */
    public void cancelSetup() {
        init();
        PlotManager.removeAllPlots();
        RaceManager.removeAllRaces();
    }

    /**
     * Load a gamemode from a name.
     * @param uName Unique name of a game.
     * @return returns true if game has been loaded correctly.
     */
    public boolean loadGame(String uName) {
        unloadGame(this.uName);

        return tryCatch(() -> {
            state = GameState.LOADING;

            DatabaseManager db = EllirionCore.getINSTANCE().getDbManager();
            //Load game
            GameDBModel gameDBModel = db.getSpecificGameByName(uName);
            Game game = new Game(gameDBModel);
            this.game = game;

            //Load plots
            List<PlotCoordDBModel> plotCoordDBModelList = db.getPlotCoordsByGameID(game.getGameID());
            PlotManager.createPlotsFromDatabase(plotCoordDBModelList);
            plotSize = game.getPlotSize();

            //Load races
            List<RaceDBModel> racesModel = db.getAllGameRaces(game.getGameID());
            RaceManager.loadRaces(racesModel);

            //Load TradingCenter
            TradingCenterDBModel tcModel = db.getTradingCenterFromGame(game.getGameID());
            TradingCenter tc = TradingCenter.getInstance();
            for (PlotCoord plotCoord : tcModel.getOwnedPlots()) {
                tc.addPlot(plotCoord);
                PlotManager.getPlotByCoordinate(plotCoord).setOwner(tc);
            }

            state = GameState.IN_PROGRESS;
        });
    }

    /**
     * Unload a gamemode from name.
     * @param uName the unique name of the game.
     * @return returns true if game has been loaded correctly.
     */
    public boolean unloadGame(String uName) {
        return tryCatch(() -> {
            state = GameState.UNLOADING;

            game = null;
            PlotManager.removeAllPlots();
            RaceManager.removeAllRaces();

            //Reset all game info in gamemanger.
            gameID = null;
            this.uName = "";
            plotSize = 0;
            xOffset = 0;
            zOffset = 0;

            state = GameState.NOT_STARTED;
        });
    }

    /**
     * Create a string of the current game state.
     * @return the current game state
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("Current game state: ").append(state.name());
        if (state == GameState.SETUP) {
            if (currentStep == setupSteps.length - 1) {
                sb.append(getReport());
            }
            sb.append("\nCurrent step:\n-").append(currentStepMessage());
        }
        return sb.toString();
    }

    /**
     * Returns a report of the game.
     * @return string with the report of the game.
     */
    public String getReport() {
        String newLine = "\n";
        String spacer = "   ";
        StringBuilder stringBuilder = new StringBuilder(175);
        stringBuilder.append(newLine)
                .append("===============GAME REPORT===============").append(newLine)
                .append("Game: ").append(uName).append(newLine)
                .append("Plot data:").append(newLine)
                .append(spacer).append("X/Z offset: ").append(getXOffset()).append(" / ")
                .append(getZOffset()).append(newLine)
                .append(spacer).append("Plot size: ").append(getPlotSize()).append(newLine)
                .append("Race data:").append(newLine);
        for (Race race : RaceManager.getRaces()) {
            stringBuilder.append(spacer).append(race.getTeamColor()).append(race.getName())
                    .append(ChatColor.RESET).append(": ").append(newLine)
                    .append(spacer).append(spacer).append("Homeplot: ")
                    .append(race.getHomePlot().getName()).append(newLine);
        }
        stringBuilder.append("Trading center Data: ");
        for (PlotCoord plotCoord : TradingCenter.getInstance().getPlotCoords()) {
            stringBuilder.append(newLine).append(spacer).append("Name: ").append(plotCoord.toString());
        }
        stringBuilder.append(newLine)
                .append("===================END===================")
                .append(newLine);
        return stringBuilder.toString();
    }

    public enum GameState {
        NOT_STARTED,
        SETUP,
        IN_PROGRESS,
        SAVING,
        LOADING,
        UNLOADING,
        FINISHED
    }
}
