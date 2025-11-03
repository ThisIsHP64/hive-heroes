package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Flowers.RareSunflowerwithFlowers;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.DemoMap;
import Players.Bee;
import StaticClasses.BeeStats;
import Utils.Direction;
import NPCs.Spider;

// main level screen; we add a spider and check Bee's sting vs Spider's body box
public class DemoLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;

    public DemoLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        hasInitialized = true;
        flagManager = new FlagManager();

        map = new DemoMap();
        map.setFlagManager(flagManager);

        // player (Bee) spawn
        player = new Bee(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);
        map.setPlayer(player);

        map.getTextbox().setInteractKey(player.getInteractKey());
        map.addListener(this);

        // let the map finish its own loading (avoids our NPC being overwritten)
        map.preloadScripts();

        // === spawn one spider near Bee start (32px tiles on this sheet) ===
        // Point start = map.getPlayerStartPosition();
        // Point spiderSpot = new Point(start.x + (2 * 32), start.y + (2 * 32)); // 2 right, 2 down
        // Spider spider = new Spider(1001, spiderSpot);
        // map.getNPCs().add(spider);
        // ==================================================================
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                // === simple "hit" interaction: Space sting box vs Spider body box ===
                if (player instanceof Bee) {
                    Bee bee = (Bee) player;

                    if (bee.isAttacking()) {
                        java.awt.Rectangle sting = bee.getAttackHitbox();

                        for (NPC npc : map.getNPCs()) {
                            if (npc instanceof Spider) {
                                Spider sp = (Spider) npc;

                                if (sting.intersects(sp.getHitbox())) {
                                    System.out.println("Spider hit!");
                                    BeeStats.setStamina(BeeStats.getStamina() + 1);
                                }
                            }

                            if (npc instanceof RareSunflowerwithFlowers) {
                                RareSunflowerwithFlowers rareSunflower = (RareSunflowerwithFlowers) npc;
                                
                                if (sting.intersects(rareSunflower.getHitbox())) {
                                    System.out.println("Sunflower hit!");
                                    BeeStats.setNectar(BeeStats.getNectar() + 1);
                                }
                            }
                        }
                    }
                }
                // ===================================================================

                break;

            case LEVEL_COMPLETED:
                winScreen.update();
                break;
        }
    }

    @Override
    public void onWin() {
        playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if (map == null || player == null || playLevelScreenState == null) {
            return; // wait until initialize() runs
        }

        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                break;
            case LEVEL_COMPLETED:
                winScreen.draw(graphicsHandler);
                break;
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() { 
        initialize(); 
    }

    public void goBackToMenu() { 
        screenCoordinator.setGameState(GameState.MENU); 
    }

    private enum PlayLevelScreenState { 
        RUNNING, LEVEL_COMPLETED 
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }
}