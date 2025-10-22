package Screens;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.SpriteSheet;
import Level.FlagManager;
import Level.GameListener;
import Level.Map;
import Level.NPC;
import Level.Player;
import Maps.HiveMap;
import NPCs.Portal;
import NPCs.QueenBee;
import NPCs.RareSunflowerwithFlowers;
import Players.Bee;
import StaticClasses.BeeStats;
import StaticClasses.TeleportManager;
import Utils.Direction;

public class HiveLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;

    // sting FX resource - single static image shown when spider is hit
    private SpriteSheet stingFxSheet;

    public HiveLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    
    public void initialize() {
        
        hasInitialized = true;
        flagManager = new FlagManager();
        flagManager.addFlag("hasTalkedToQueen", false);


        map = new HiveMap();
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

        // load the sting FX - just one static sprite
        stingFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), 32, 32);
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                // check if bee died and death animation finished
                if (player instanceof Bee) {
                    Bee bee = (Bee) player;

                    // transition to game over after death animation completes
                    if (bee.isDead() && bee.isDeathAnimationComplete()) {
                        screenCoordinator.setGameState(GameState.GAME_OVER);
                        return;
                    }

                    for (NPC npc : map.getNPCs()) {
                        java.awt.Rectangle sting = bee.getAttackHitbox();

                        if (npc instanceof Portal) {
                            Portal portal = (Portal) npc;

                            if (sting.intersects(portal.getHitbox())) {
                                TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
                            }
                        }

                        if (npc instanceof QueenBee) {
                            QueenBee queenBee = (QueenBee) npc;

                        }

                    }
                    
                }
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