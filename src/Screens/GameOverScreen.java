package Screens;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import StaticClasses.BeeStats;
import StaticClasses.UnleashMayhem;

import java.awt.image.BufferedImage;

public class GameOverScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected BufferedImage gameOverImage;
    protected boolean hasInitialized = false;

    public GameOverScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        hasInitialized = true;
        // load the game over image (just filename, no path needed)
        gameOverImage = ImageLoader.load("gameover1.png");
    }

    @Override
    public void update() {
        // press R to retry (restart level)
        if (Keyboard.isKeyDown(Key.R)) {
            UnleashMayhem.reset(); // stop horde and reset music
            BeeStats.setDead(false);
            BeeStats.respawn();
            BeeStats.setWalkSpeed(6f);
            screenCoordinator.setGameState(GameState.HIVELEVEL);
        }
       
        // press E to exit (back to main menu)
        if (Keyboard.isKeyDown(Key.E)) {
            UnleashMayhem.reset(); // stop horde and reset music
            BeeStats.setDead(false);
            BeeStats.resetToDefault();
            BeeStats.setWalkSpeed(BeeStats.getMaxWalkSpeed());
            screenCoordinator.resetAll(); // reset all levels to default state
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // draw game over image scaled to full screen (800x600)
        graphicsHandler.drawImage(gameOverImage, 0, 0, 800, 600);
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }
}