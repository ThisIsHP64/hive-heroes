package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected BufferedImage background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont returnInstructionsLabel;
    protected SpriteFont originalCreatorLabel;
    protected boolean hasInitialized = false;


    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        hasInitialized = true;
        // setup graphics on screen (background, spritefont text)
        try {
            background = ImageIO.read(new File("Resources/titleBg.jpg"));
        } catch (Exception e) {
            background = null;
        }
        creditsLabel = new SpriteFont("Credits", 15, 7, "Times New Roman", 30, Color.white);
        createdByLabel = new SpriteFont("Created by the Chaos Emeralds", 130, 121, "Times New Roman", 20, Color.white);
        originalCreatorLabel = new SpriteFont("Base Game made by Alex Thimineur", 130, 141, "Times New Roman", 20,
                Color.white);

        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 532, "Times New Roman", 30,
                Color.white);
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if (background != null) {
            graphicsHandler.drawImage(background, 0, 0, 800, 605);
        }
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        originalCreatorLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }
}
