package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

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

    protected SpriteFont manager, member1, member2, member3, member4, member5;

    ArrayList<SpriteFont> labels;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        hasInitialized = true;

        labels = new ArrayList<>();
        // setup graphics on screen (background, spritefont text)

        try {
            background = ImageIO.read(new File("Resources/titleBg.jpg"));
        } catch (Exception e) {
            background = null;
        }

        creditsLabel = new SpriteFont("Credits", 15, 7, "FELIX TITLING", 30, Color.black);
        labels.add(creditsLabel);

        createdByLabel = new SpriteFont("Created by the Chaos Emeralds", 
                                        50, 121, "FELIX TITLING", 20, Color.red);
        labels.add(createdByLabel);

        originalCreatorLabel = new SpriteFont("Base Game made by Alex Thimineur", 
                                        50, 141, "FELIX TITLING", 20, Color.red);
        labels.add(originalCreatorLabel);

        manager = new SpriteFont("Manager: Ryan Seely",
                                        50, 181, "FELIX TITLING", 20, Color.red);
        labels.add(manager);

        member1 = new SpriteFont("Members:\nWilson Chen",
                                        50, 201, "FELIX TITLING", 20, Color.red);
        labels.add(member1);

        member2 = new SpriteFont("Phil Kwiatkowski",
                                        50, 221, "FELIX TITLING", 20, Color.red);
        labels.add(member2);

        member3 = new SpriteFont("MD Fayed Salim",
                                        50, 241, "FELIX TITLING", 20, Color.red);
        labels.add(member3);

        member4 = new SpriteFont("Hunter Pageau",
                                        50, 261, "FELIX TITLING", 20, Color.red);
        labels.add(member4);

        member5 = new SpriteFont("Aditi Baghel", 
                                        50, 281, "FELIX TITLING", 20, Color.red);
        labels.add(member5);

        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 
                                        20, 545, "FELIX TITLING", 30, Color.black);
        labels.add(returnInstructionsLabel);
        
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

        for (SpriteFont labels : labels) {
            labels.draw(graphicsHandler);
        }
        
    }   

    public boolean hasInitialized() {
        return hasInitialized;
    }
}
