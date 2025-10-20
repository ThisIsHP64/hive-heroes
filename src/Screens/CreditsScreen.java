package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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

    protected SpriteFont art1, art2, art3, art4, art5, art6, art7;

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

        creditsLabel = new SpriteFont("Credits", 15, 7, "Times New Roman", 30, Color.white);
        labels.add(creditsLabel);

        createdByLabel = new SpriteFont("Created by the Chaos Emeralds", 
                                        50, 121, "Times New Roman", 20, Color.white);
        labels.add(createdByLabel);

        originalCreatorLabel = new SpriteFont("Base Game made by Alex Thimineur", 
                                        50, 141, "Times New Roman", 20, Color.white);
        labels.add(originalCreatorLabel);

        art1 = new SpriteFont("Heart Icon: https://temok.itch.io/heart-container-animated-in-pixel-art",
                                        50, 161, "Times New Roman", 20, Color.white);
        labels.add(art1);

        art2 = new SpriteFont("Volcano Tileset: https://schwarnhild.itch.io/volcanoe-tileset-and-asset-pack-32x32-pixels",
                                        50, 181, "Times New Roman", 20, Color.white);
        labels.add(art2);

        art3 = new SpriteFont("Grass Tileset: https://schwarnhild.itch.io/basic-tileset-and-asset-pack-32x32-pixels",
                                        50, 201, "Times New Roman", 20, Color.white);
        labels.add(art3);

        art4 = new SpriteFont("Dungeon Tileset: https://0x72.itch.io/16x16-dungeon-tileset",
                                        50, 221, "Times New Roman", 20, Color.white);
        labels.add(art4);

        art5 = new SpriteFont("Zelda Maze/Dungeon Tileset: https://kuroren.itch.io/dungeon-tileset",
                                        50, 241, "Times New Roman", 20, Color.white);
        labels.add(art5);

        art6 = new SpriteFont("Cemetery Tileset: https://dreamir.itch.io/tilesets-cemetery", 
                                        50, 261, "Times New Roman", 20, Color.white);
        labels.add(art6);

        art7 = new SpriteFont("Snowy Tileset: https://maytch.itch.io/pixel", 
                                        50, 281, "Times New Roman", 20, Color.white);
        labels.add(art7);

        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 
                                        20, 532, "Times New Roman", 30, Color.white);
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
