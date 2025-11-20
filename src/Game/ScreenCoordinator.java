package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Screens.*;
import Sound.Music;
import Sound.MusicManager;
import StaticClasses.TeleportManager;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */
public class ScreenCoordinator extends Screen {
    // currently shown Screen
    protected Screen currentScreen = new DefaultScreen();

    // default screens
    protected MenuScreen menuScreen;
    protected CreditsScreen creditsScreen;
    protected WinScreen winScreen;
    protected GameOverScreen gameOverScreen;

    // level screens    
    protected GrassLevelScreen grassLevelScreen;
    protected VolcanoLevelScreen volcanoLevelScreen;
    protected DungeonLevelScreen dungeonLevelScreen;
    protected MazeLevelScreen mazeLevelScreen;
    protected SnowLevelScreen snowLevelScreen;
    protected HiveLevelScreen hiveLevelScreen;

    // demo map
    protected DemoLevelScreen demoLevelScreen;

    protected boolean isBossActive = false;

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

    public void setCurrentGameState(GameState newGameState) {
        gameState = newGameState;
    }

    @Override
    public void initialize() {
        
        // default screens
        menuScreen = new MenuScreen(this);
        gameOverScreen = new GameOverScreen(this);
        creditsScreen = new CreditsScreen(this);

        // demo level screen
        demoLevelScreen = new DemoLevelScreen(this);

        // screens for the regions
        grassLevelScreen = new GrassLevelScreen(this);
        volcanoLevelScreen = new VolcanoLevelScreen(this);
        dungeonLevelScreen = new DungeonLevelScreen(this);
        mazeLevelScreen = new MazeLevelScreen(this);
        snowLevelScreen = new SnowLevelScreen(this);
        hiveLevelScreen = new HiveLevelScreen(this);

        // start game off with Menu Screen
        gameState = GameState.MENU;
    }

    public void resetAll() {
        // screens for the regions
        grassLevelScreen = new GrassLevelScreen(this);
        volcanoLevelScreen = new VolcanoLevelScreen(this);
        dungeonLevelScreen = new DungeonLevelScreen(this);
        mazeLevelScreen = new MazeLevelScreen(this);
        snowLevelScreen = new SnowLevelScreen(this);
        hiveLevelScreen = new HiveLevelScreen(this);
    }

    @Override
    public void update() {
        do {
            // if previousGameState does not equal gameState, it means there was a change in gameState
            // this triggers ScreenCoordinator to bring up a new Screen based on what the gameState is
            if (previousGameState != gameState) {
                switch(gameState) {
                    case MENU:
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.MENU);
                        currentScreen = menuScreen;
                        break;

                    case CREDITS:
                        currentScreen = creditsScreen;
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.CREDITS);
                        break;

                    case GAME_OVER:
                        currentScreen = gameOverScreen;
                        break;

                    case GRASSLEVEL:
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.GRASS);
                        currentScreen = grassLevelScreen;
                        break;

                    case VOLCANOLEVEL:
                        if (!TeleportManager.isBossActive()) {
                            MusicManager.stopAll();
                            MusicManager.playLoop(Music.VOLCANO);
                        } else {
                            MusicManager.stopAll();
                            MusicManager.playLoop(Music.BOSS);
                        }
                        currentScreen = volcanoLevelScreen;
                        break;

                    case DUNGEONLEVEL:
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.DUNGEON);
                        currentScreen = dungeonLevelScreen;
                        break;

                    case SNOWLEVEL:
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.SNOW);
                        currentScreen = snowLevelScreen;
                        break;
                        
                    case MAZELEVEL:
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.MAZE);
                        currentScreen = mazeLevelScreen;
                        break;

                    case HIVELEVEL:
                        MusicManager.stopAll();
                        MusicManager.playLoop(Music.HIVE);
                        currentScreen = hiveLevelScreen;
                        break;

                    // case BOSSLEVEL:
                    //     MusicManager.stopAll();
                    //     MusicManager.playLoop(Music.BOSS);
                    //     currentScreen = volcanoLevelScreen;
                    //     break;

                    case OPTIONS:
                        break;
                    default:
                        break;
                }

                if (currentScreen.hasInitialized()) {
                    currentScreen.update();
                } else {
                    currentScreen.initialize();
                }
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


    public boolean isBossActive() {
        return isBossActive;
    }

    public void setBossActive(boolean isBossActive) {
        this.isBossActive = isBossActive;
    }

    @Override
    public boolean hasInitialized() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasInitialized'");
    }
}