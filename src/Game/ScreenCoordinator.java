package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Maps.DemoMap;
import Screens.*;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */
public class ScreenCoordinator extends Screen {
    // currently shown Screen
    protected Screen currentScreen = new DefaultScreen();

    protected MenuScreen menuScreen;
    protected DemoLevelScreen demoLevelScreen;
    protected GameOverScreen gameOverScreen;
    protected OptionsScreen optionsScreen;
    protected CreditsScreen creditsScreen;
    protected SprintOneLevelScreen grassScreen;
    protected WinScreen winScreen;

    // keep track of gameState so ScreenCoordinator knows which Screen to show
    protected GameState gameState;
    protected GameState previousGameState;

    public GameState getGameState() {
        return gameState;
    }

    // Other Screens can set the gameState of this class to force it to change the currentScreen
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void initialize() {

        menuScreen = new MenuScreen(this);
        demoLevelScreen = new DemoLevelScreen(this);
        gameOverScreen = new GameOverScreen(this);
        optionsScreen = new OptionsScreen(this);
        creditsScreen = new CreditsScreen(this);
        grassScreen = new SprintOneLevelScreen(this);

        // start game off with Menu Screen
        gameState = GameState.MENU;
    }

    @Override
    public void update() {
        do {
            // if previousGameState does not equal gameState, it means there was a change in gameState
            // this triggers ScreenCoordinator to bring up a new Screen based on what the gameState is
            if (previousGameState != gameState) {
                switch(gameState) {
                    case MENU:
                        currentScreen = menuScreen;
                        break;
                    case LEVEL:
                        currentScreen = grassScreen;
                        break;
                    case OPTIONS:
                        currentScreen = optionsScreen;
                        break;
                    case CREDITS:
                        currentScreen = creditsScreen;
                        break;
                    case GAME_OVER:
                        currentScreen = gameOverScreen;
                        break;
                }
                currentScreen.initialize();
            }
            previousGameState = gameState;
            
            // call the update method for the currentScreen
            currentScreen.update();
        } while (previousGameState != gameState);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // call the draw method for the currentScreen
        currentScreen.draw(graphicsHandler);
    }
}