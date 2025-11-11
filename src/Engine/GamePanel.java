package Engine;

import Effects.RainParticleSystemGrassLevel;
import Effects.RedRainParticleSystemVolcanoLevel;
import Effects.SnowParticleSystemSnowLevel;
import Effects.WindSystemGrassLevel;
import Effects.ScreenFX; // <<< NEW
import Game.GameState;
import GameObject.Rectangle;
import SpriteFont.SpriteFont;
import StaticClasses.TeleportManager;
import Utils.Colors;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
 * This is where the game loop process and render back buffer is setup
 */
public class GamePanel extends JPanel implements ActionListener {
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;

	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;

	private KeyLocker keyLocker = new KeyLocker();
	private Thread gameLoopProcess;
	private boolean doPaint;

	private boolean isGamePaused = false;
	private SpriteFont pauseLabel;
	private final Key pauseKey = Key.P;

	private Key showFPSKey = Key.T;
	private SpriteFont fpsDisplayLabel;
	private boolean showFPS = false;
	private int currentFPS;

	private final int SCREEN_WIDTH = 800;
	private final int SCREEN_HEIGHT = 600;

	private RainParticleSystemGrassLevel rainSystemgrassLevel;
    private WindSystemGrassLevel windSystemgrassLevel;
	private RedRainParticleSystemVolcanoLevel redRainSystemvolcanoLevel;
	private SnowParticleSystemSnowLevel snowParticleSystemsnowLevel;
	private int grassLeveltimer = 0;
	private int volcanoLeveltimer = 0;
	private int snowLeveltimer = 0;
	private static boolean isRedRaining = false;
	private static boolean isRaining = false;
    private static boolean isWindActive = false;
	private static boolean isSnowing = false;

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

		currentFPS = Config.TARGET_FPS;

		// game loop thread
		GameLoop gameLoop = new GameLoop(this);
		gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

		// initializing particle systems
		rainSystemgrassLevel = new RainParticleSystemGrassLevel(SCREEN_WIDTH, SCREEN_HEIGHT); 
		windSystemgrassLevel = new WindSystemGrassLevel(SCREEN_WIDTH, SCREEN_HEIGHT);
		redRainSystemvolcanoLevel = new RedRainParticleSystemVolcanoLevel(SCREEN_WIDTH, SCREEN_HEIGHT);
		snowParticleSystemsnowLevel = new SnowParticleSystemSnowLevel(SCREEN_WIDTH, SCREEN_HEIGHT);
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

		if (!isGamePaused) {
            screenManager.update();

            grassLeveltimer++;
			int grassLevelseconds = grassLeveltimer / 60;
			int grassLevelcycleTime = grassLevelseconds % 750;

			volcanoLeveltimer++;
			int volcanoLevelseconds = volcanoLeveltimer / 60;
			int volcanoLevelcycleTime = volcanoLevelseconds % 750;

			snowLeveltimer++;
			int snowLevelseconds = snowLeveltimer / 60;
			int snowLevelcycleTime = snowLevelseconds % 750;

			boolean onGrassLevel = TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL;
			boolean onVolcanoLevel = TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL;
			boolean onSnowLevel = TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL;

			// --- RAIN CYCLE ---
			if (grassLevelcycleTime >= 30 && grassLevelcycleTime < 90) {
				if (onGrassLevel) {
					if (!isRaining) {
						isRaining = true;
						rainSystemgrassLevel.clear();
					}
					rainSystemgrassLevel.update();
				}
			} else if (isRaining) {
				isRaining = false;
				rainSystemgrassLevel.clear();
			}

			// --- WIND CYCLE ---
			if (grassLevelcycleTime >= 150 && grassLevelcycleTime < 210) {
				if (onGrassLevel) {
					if (!isWindActive) {
						isWindActive = true;
						windSystemgrassLevel.clear();
					}
					windSystemgrassLevel.update();
				}
			} else if (isWindActive) {
				isWindActive = false;
				windSystemgrassLevel.clear();
			}

			// --- VOLCANO RED RAIN ---
			if (volcanoLevelcycleTime >= 30 && volcanoLevelcycleTime < 90) {
				if (onVolcanoLevel) {
					if (!isRedRaining) {
						isRedRaining = true;
						redRainSystemvolcanoLevel.clear();
					}
					redRainSystemvolcanoLevel.update();
				}
			} else if (isRedRaining) {
				isRedRaining = false;
				redRainSystemvolcanoLevel.clear();
			}

			// --- SNOW ---
			if (snowLevelcycleTime >= 30 && snowLevelcycleTime < 90) {
				if (onSnowLevel) {
					if (!isSnowing) {
						isSnowing = true;
						snowParticleSystemsnowLevel.clear();
					}
					snowParticleSystemsnowLevel.update();
				}
			} else if (onSnowLevel) {
				isSnowing = false;
				snowParticleSystemsnowLevel.clear();
			}
		}
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
			rainSystemgrassLevel.update();
		}
		repaint();

		if (isWindActive) {
			windSystemgrassLevel.update();
		}
		repaint();

		if (isRedRaining) {
			redRainSystemvolcanoLevel.update();
		}
		repaint();

		if (isSnowing) {
			snowParticleSystemsnowLevel.update();
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (doPaint) {
			// setup GH and draw frame
			graphicsHandler.setGraphics((Graphics2D) g);
			draw();

			Graphics2D g2d = (Graphics2D) g;

			boolean onGrassLevel = TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL;
			if (onGrassLevel && isRaining) {
				rainSystemgrassLevel.draw(g2d);
			}
			if (onGrassLevel && isWindActive) {
				windSystemgrassLevel.draw(g2d);
			}

			boolean onVolcanoLevel = TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL;
			if (onVolcanoLevel && isRedRaining) {
				redRainSystemvolcanoLevel.draw(g2d);
			}

			boolean onSnowLevel = TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL;
			if (onSnowLevel && isSnowing) {
				snowParticleSystemsnowLevel.draw(g2d);
			}

			// <<< NEW: apply full-screen epic effect over the final frame >>>
			ScreenFX.apply((Graphics2D) g, getWidth(), getHeight());
		}
	}

	public static Boolean getisRaining(){
		return isRaining;
	}

	public static Boolean getisRedRaining(){
		return isRedRaining;
	}

	public static Boolean getisWindActive() {
        return isWindActive;
    }

	public static Boolean getisSnowing() {
        return isSnowing;
    }
}
