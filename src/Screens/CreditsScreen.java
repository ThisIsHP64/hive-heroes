package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;
import StaticClasses.TeleportManager;
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
    protected SpriteFont alexLabel;
    protected SpriteFont ryan;
    protected SpriteFont chaosEmeraldsLabel;
    protected SpriteFont returnInstructionsLabel;
    protected SpriteFont originalCreatorLabel;
    protected Font orbitronLarge, orbitronMed;
    protected boolean hasInitialized = false;

    protected SpriteFont manager, members, member1, member2, member3, member4, member5;

    ArrayList<SpriteFont> labels;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        hasInitialized = true;

        TeleportManager.call(screenCoordinator);

        try {
            orbitronLarge = Font.createFont(Font.TRUETYPE_FONT, new File("Resources/fonts/orbitron.ttf")).deriveFont(50f);
            orbitronMed = Font.createFont(Font.TRUETYPE_FONT, new File("Resources/fonts/orbitron.ttf")).deriveFont(20f);
        } catch (Exception e) {
            orbitronLarge = new Font("Arial", Font.PLAIN, 60);
            orbitronMed = new Font("Arial", Font.PLAIN, 30);
        }

        labels = new ArrayList<>();
        // setup graphics on screen (background, spritefont text)

        try {
            background = ImageIO.read(new File("Resources/credits.png"));
        }
        catch (Exception e){
            background = null;
        }

        creditsLabel = new SpriteFont("CREDITS", 255, 60, orbitronLarge, new Color(255, 255, 0));
        creditsLabel.setOutlineColor(Color.black);
        creditsLabel.setOutlineThickness(5);
        createdByLabel = new SpriteFont("CREATED BY", 180, 160, orbitronMed, new Color(255, 255, 0));
        createdByLabel.setOutlineColor(Color.black);
        createdByLabel.setOutlineThickness(2);
        chaosEmeraldsLabel = new SpriteFont("CHAOS EMERALDS", 335, 160, orbitronMed, new Color(0, 103, 79));
        chaosEmeraldsLabel.setOutlineColor(Color.black);
        chaosEmeraldsLabel.setOutlineThickness(2);
        originalCreatorLabel = new SpriteFont("BASE GAME MADE BY", 140, 215, orbitronMed, new Color(255, 255, 0));
        originalCreatorLabel.setOutlineColor(Color.black);
        originalCreatorLabel.setOutlineThickness(2);
        alexLabel = new SpriteFont("ALEX THIMINEUR", 395, 215, orbitronMed, new Color(80,200,204));
        alexLabel.setOutlineColor(Color.black);
        alexLabel.setOutlineThickness(2);
        manager = new SpriteFont("MANAGER: ", 230, 270, orbitronMed, new Color(255, 255, 0));
        manager.setOutlineColor(Color.black);
        manager.setOutlineThickness(2);
        ryan = new SpriteFont("RYAN SEELY", 355, 270, orbitronMed, new Color(110, 76, 158));
        ryan.setOutlineColor(Color.black);
        ryan.setOutlineThickness(2);
        members = new SpriteFont("MEMBERS:", 180, 325, orbitronMed, new Color(255, 255, 0));
        members.setOutlineColor(Color.black);
        members.setOutlineThickness(2);
        member1 = new SpriteFont("WILSON CHEN", 320, 325, orbitronMed, new Color(204, 38, 48));
        member1.setOutlineColor(Color.black);
        member1.setOutlineThickness(2);
        member2 = new SpriteFont("PHIL KWIATKOWSKI", 320, 355, orbitronMed, new Color(204, 38, 48));
        member2.setOutlineColor(Color.black);
        member2.setOutlineThickness(2);
        member3 = new SpriteFont("MD FAYED SALIM", 320, 385, orbitronMed, new Color(204, 38, 48));
        member3.setOutlineColor(Color.black);
        member3.setOutlineThickness(2);
        member4 = new SpriteFont("HUNTER PAGEAU", 320, 415, orbitronMed, new Color(204, 38, 48));
        member4.setOutlineColor(Color.black);
        member4.setOutlineThickness(2);
        member5 = new SpriteFont("ADITI BAGHEL", 320, 445, orbitronMed, new Color(204, 38, 48));
        member5.setOutlineColor(Color.black);
        member5.setOutlineThickness(2);
        returnInstructionsLabel = new SpriteFont("PRESS SPACE TO RETURN TO THE MAIN MENU", 130, 500, orbitronMed, new Color(255, 255, 0));
        returnInstructionsLabel.setOutlineColor(Color.black);
        returnInstructionsLabel.setOutlineThickness(2);

        
        
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
        alexLabel.draw(graphicsHandler);
        chaosEmeraldsLabel.draw(graphicsHandler);
        originalCreatorLabel.draw(graphicsHandler);
        manager.draw(graphicsHandler);
        ryan.draw(graphicsHandler);
        members.draw(graphicsHandler);
        member1.draw(graphicsHandler);
        member2.draw(graphicsHandler);
        member3.draw(graphicsHandler);
        member4.draw(graphicsHandler);
        member5.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        //*/
    }   

    public boolean hasInitialized() {
        return hasInitialized;
    }
}
