package Engine;

import Effects.RainParticleSystem;
import GameObject.Rectangle;
import SpriteFont.SpriteFont;
import SpriteImage.ResourceHUD;
import Utils.Colors;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
 * This is where the game loop process and render back buffer is setup
 */
public class GamePanel extends JPanel implements ActionListener{
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;

	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;

	private boolean isGamePaused = false;
	private SpriteFont pauseLabel;
	private KeyLocker keyLocker = new KeyLocker();
	private final Key pauseKey = Key.P;
	private Thread gameLoopProcess;

	private Key showFPSKey = Key.T;
	private SpriteFont fpsDisplayLabel;
	private boolean showFPS = false;
	private int currentFPS;
	private boolean doPaint;

	private ResourceHUD resourceBars;
	private Key resourcesKey = Key.G;
	private boolean isShowingResources = false;

	private final int SCREEN_WIDTH = 800;
	private final int SCREEN_HEIGHT = 600;

	private RainParticleSystem rainSystem;
	private Timer gameLoopTimer;
	private Timer rainTimer;

	private boolean isRaining = false;


	// The JPanel and various important class instances are setup here
	public GamePanel() {
		super();
		this.setDoubleBuffered(true);

		// attaches Keyboard class's keyListener to this JPanel
		this.addKeyListener(Keyboard.getKeyListener());

		graphicsHandler = new GraphicsHandler();

		screenManager = new ScreenManager();

		pauseLabel = new SpriteFont("PAUSE", 365, 280, "Arial", 24, Color.white);
		pauseLabel.setOutlineColor(Color.black);
		pauseLabel.setOutlineThickness(2.0f);

		fpsDisplayLabel = new SpriteFont("FPS", 725, 20, "Arial", 12, Color.black);

		// resourceBars = new ResourceHUD();

		currentFPS = Config.TARGET_FPS;

		// this game loop code will run in a separate thread from the rest of the program
		// will continually update the game's logic and repaint the game's graphics
		GameLoop gameLoop = new GameLoop(this);
		gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

		//initializing the rain system
		rainSystem = new RainParticleSystem(SCREEN_WIDTH, SCREEN_HEIGHT);

		//timer for the game updates
		gameLoopTimer = new Timer(16, (ActionListener) this);
		gameLoopTimer.start();

		isRaining = true;

		//demo purpose for the rain to stop raining after 3 minutes
		new Timer(180000, e -> {
            isRaining = false;
            rainSystem.clear();
            ((Timer) e.getSource()).stop();
        }).start();

		// Timer for starting rain every 5 minutes (300000 ms)
		rainTimer = new Timer(300000, e -> startRainCycle());
		rainTimer.start();
	}

	// this is called later after instantiation, and will initialize screenManager
	public void setupGame() {
		setBackground(Colors.CORNFLOWER_BLUE);
		screenManager.initialize(new Rectangle(getX(), getY(), getWidth(), getHeight()));
	}

	// this starts the timer (the game loop is started here)
	public void startGame() {
		gameLoopProcess.start();
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void setCurrentFPS(int currentFPS) {
		this.currentFPS = currentFPS;
	}

	public void setDoPaint(boolean doPaint) {
		this.doPaint = doPaint;
	}

	public void update() {
		updatePauseState();
		updateShowFPSState();
		// resourceBars.update();
		// add 3 methods to update the stamina, health, and nectar by pressing a key

		if (!isGamePaused) {
			screenManager.update();
		}
	}

	private void startRainCycle() {
        // Start raining again
        isRaining = true;
	
        // Stop raining after 1 minute
        new Timer(60000, e -> {
            isRaining = false;
            rainSystem.clear();
            ((Timer) e.getSource()).stop();
        }).start();
	}
	private void updatePauseState() {
		if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
			isGamePaused = !isGamePaused;
			keyLocker.lockKey(pauseKey);
		}

		if (Keyboard.isKeyUp(pauseKey)) {
			keyLocker.unlockKey(pauseKey);
		}
	}

	private void updateShowFPSState() {
		if (Keyboard.isKeyDown(showFPSKey) && !keyLocker.isKeyLocked(showFPSKey)) {
			showFPS = !showFPS;
			keyLocker.lockKey(showFPSKey);
		}

		if (Keyboard.isKeyUp(showFPSKey)) {
			keyLocker.unlockKey(showFPSKey);
		}

		fpsDisplayLabel.setText("FPS: " + currentFPS);
	}

	public void draw() {			
		// draw current game state
		screenManager.draw(graphicsHandler);
		
		
		if (Keyboard.isKeyDown(resourcesKey)) {
			isShowingResources = true;
		}

		if (isShowingResources) {
			// resourceBars.draw(graphicsHandler);
		}

		// if game is paused, draw pause gfx over Screen gfx
		if (isGamePaused) {
			pauseLabel.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
		}

		if (showFPS) {
			fpsDisplayLabel.draw(graphicsHandler);
		}
	}

	public void actionPerformed(ActionEvent e){
		if (isRaining) {
			rainSystem.update();
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (doPaint) {
			// every repaint call will schedule this method to be called
			// when called, it will setup the graphics handler and then call this class's draw method
			graphicsHandler.setGraphics((Graphics2D) g);
			draw();
			Graphics2D g2d = (Graphics2D) g;
			if (isRaining) {
				rainSystem.draw(g2d);
			}
		}
	}
}
