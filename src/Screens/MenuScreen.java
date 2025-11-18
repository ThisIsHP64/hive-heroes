package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;
import StaticClasses.TeleportManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected Font orbitronLarge, orbitronMed;
    protected SpriteFont titleA;
    protected SpriteFont titleB;
    protected SpriteFont playGame;
    protected SpriteFont options;
    protected SpriteFont credits;
    protected BufferedImage background;
    protected int keyPressTimer;
    protected KeyLocker keyLocker = new KeyLocker();


    protected boolean hasInitialized = false;

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        hasInitialized = true;

        TeleportManager.call(screenCoordinator);

        try {
            orbitronLarge = Font.createFont(Font.TRUETYPE_FONT, new File("Resources/fonts/orbitron.ttf")).deriveFont(60f);
            orbitronMed = Font.createFont(Font.TRUETYPE_FONT, new File("Resources/fonts/orbitron.ttf")).deriveFont(30f);
        } catch (Exception e) {
            orbitronLarge = new Font("Arial", Font.PLAIN, 60);
            orbitronMed = new Font("Arial", Font.PLAIN, 30);
        }

        titleA = new SpriteFont("HIVE", 10, 0, orbitronLarge, new Color(255, 255, 0));
        titleB = new SpriteFont("HEROES", 10, 60, orbitronLarge, new Color(255, 255, 0));
        titleA.setOutlineColor(Color.black);
        titleA.setOutlineThickness(5);
        titleB.setOutlineColor(Color.black);
        titleB.setOutlineThickness(5);
        playGame = new SpriteFont("PLAY GAME", 580, 440, orbitronMed, new Color(255, 255, 0));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        credits = new SpriteFont("CREDITS", 580, 510, orbitronMed, new Color(255, 255, 0));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);
        try {
            background = ImageIO.read(new File("Resources/titleBg.jpg"));
        } catch (Exception e) {
            background = null;
        }
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if (Keyboard.isKeyDown(Key.S) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.W) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 1) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 2;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(Color.cyan);
            credits.setColor(Color.yellow);
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(Color.yellow);
            credits.setColor(Color.cyan);
        } 

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {


                screenCoordinator.setGameState(GameState.HIVELEVEL);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if(background != null) {
            graphicsHandler.drawImage(background, 0, 0, 800, 605);
        }
        titleA.draw(graphicsHandler);
        titleB.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        credits.draw(graphicsHandler);
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }
}
