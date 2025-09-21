package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import Sound.Music;
import Sound.MusicManager;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont titleA;
    protected SpriteFont titleB;
    protected SpriteFont playGame;
    protected SpriteFont options;
    protected SpriteFont credits;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        titleA = new SpriteFont("HIVE", 20, 20, "Arial", 60, new Color(255, 255, 0));
        titleB = new SpriteFont("HEROES", 20, 80, "Arial", 60, new Color(255, 255, 0));
        titleA.setOutlineColor(Color.black);
        titleA.setOutlineThickness(5);
        titleB.setOutlineColor(Color.black);
        titleB.setOutlineThickness(5);
        playGame = new SpriteFont("PLAY GAME", 600, 405, "Arial", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        options = new SpriteFont("OPTIONS", 600, 455, "Arial", 30, new Color(49, 207, 240));
        options.setOutlineColor(Color.black);
        options.setOutlineThickness(3);
        credits = new SpriteFont("CREDITS", 600, 505, "Arial", 30, new Color(49, 207, 240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
        MusicManager.playMenuLoop();
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 2) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 2;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(new Color(255, 215, 0));
            options.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            pointerLocationX = (int) (playGame.getX() - 40);
            pointerLocationY = (int) (playGame.getY() + 7);
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            options.setColor(new Color(255, 215, 0));
            pointerLocationX = (int) (options.getX() - 40);;
            pointerLocationY = (int) (options.getY() + 7);
        } else if (currentMenuItemHovered == 2) {
            playGame.setColor(new Color(49, 207, 240));
            options.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(255, 215, 0));
            pointerLocationX = (int) (credits.getX() - 40);
            ;
            pointerLocationY = (int) (credits.getY() + 7);
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                MusicManager.stopMenuLoop();
                screenCoordinator.setGameState(GameState.LEVEL);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.OPTIONS);
            } else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        titleA.draw(graphicsHandler);
        titleB.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        options.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }
}
