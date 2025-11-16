package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.Keyboard;
import Engine.Key;
import Engine.KeyLocker;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.MazeMap;
import Players.Bee;
import StaticClasses.BeeStats;
import StaticClasses.TeleportManager;
import Utils.Direction;
import Portals.GrassPortal;
import Portals.Portal;
import NPCs.OneRing;
import NPCs.FireTunic;

public class MazeLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;
    
    // key locker for item collection
    private KeyLocker keyLocker = new KeyLocker();
    
    // track if items have been collected
    private boolean oneRingCollected = false;
    private boolean fireTunicCollected = false;
    
    // track textbox state for teleporting after dismissal
    private boolean wasTextboxActive = false;
    private boolean pendingTeleportToGrass = false;
    private int teleportTimer = 0;
    private static final int TELEPORT_DELAY = 120; // 2 seconds at 60fps

    public MazeLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        hasInitialized = true;
        flagManager = new FlagManager();

        map = new MazeMap();
        map.setFlagManager(flagManager);

        flagManager.addFlag("collectedRedTunic", false);

        // player (Bee) spawn at X:1, Y:1
        player = new Bee(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);
        map.setPlayer(player);

        map.getTextbox().setInteractKey(player.getInteractKey());
        map.addListener(this);

        // let the map finish its own loading (avoids our NPC being overwritten)
        map.preloadScripts();
        
        // reset collection flags
        oneRingCollected = false;
        fireTunicCollected = false;
        wasTextboxActive = false;
        pendingTeleportToGrass = false;
        teleportTimer = 0;
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

                    // Check for item collection with E key (before textbox locks it)
                    if (Keyboard.isKeyDown(Key.E) && !keyLocker.isKeyLocked(Key.E)) {
                        for (NPC npc : map.getNPCs()) {
                            // Check for OneRing collection
                            if (npc instanceof NPCs.OneRing) {
                                NPCs.OneRing ring = (NPCs.OneRing) npc;
                                
                                float distance = (float) Math.sqrt(
                                    Math.pow(bee.getX() - ring.getX(), 2) + 
                                    Math.pow(bee.getY() - ring.getY(), 2)
                                );
                                
                                System.out.println("Ring distance: " + distance + ", collected: " + ring.isCollected());
                                
                                if (!ring.isCollected() && distance < 80) {
                                    ring.collect();
                                    oneRingCollected = true;
                                    // Add ring to inventory
                                    BeeStats.setHasRing(true);
                                    // Show ring icon in HUD
                                    bee.showRingIcon();
                                    map.getTextbox().addText("Hmm random shiny ring.");
                                    map.getTextbox().addText("I'll grab it");
                                    map.getTextbox().setIsActive(true);
                                    keyLocker.lockKey(Key.E);
                                }
                            }
                            
                            // Check for FireTunic collection
//                            if (npc instanceof NPCs.FireTunic) {
//                                NPCs.FireTunic tunic = (NPCs.FireTunic) npc;
//
//                                float distance = (float) Math.sqrt(
//                                    Math.pow(bee.getX() - tunic.getX(), 2) +
//                                    Math.pow(bee.getY() - tunic.getY(), 2)
//                                );
//
//                                System.out.println("Tunic distance: " + distance + ", collected: " + tunic.isCollected());
//
//                                if (!tunic.isCollected() && distance < 120) {
//                                    tunic.collect();
//                                    fireTunicCollected = true;
//                                    map.getTextbox().addText("The Volcanic Tunic!");
//                                    map.getTextbox().addText("This will protect me from the flames.");
//                                    map.getTextbox().addText("Returning to Grass Level...");
//                                    map.getTextbox().setIsActive(true);
//                                    pendingTeleportToGrass = true;
//                                    teleportTimer = 0;
//                                    keyLocker.lockKey(Key.E);
//                                }
//                            }
                        }
                    }

                    if (bee.isAttacking()) {
                        System.out.println("Bee is attacking!");
                        java.awt.Rectangle sting = bee.getAttackHitbox();

                        for (NPC npc : map.getNPCs()) {
                            if (npc instanceof Portal) {
                                Portal portal = (Portal) npc;

                                if (sting.intersects(portal.getHitbox())) {
                                    TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
                                }
                            }

                            if (npc instanceof GrassPortal) {
                                GrassPortal grassPortal = (GrassPortal) npc;

                                if (sting.intersects(grassPortal.getHitbox())) {
                                    TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
                                }
                            }
                        }
                    }
                }
                
                // Remove fully faded OneRing and dismiss textbox (only if queue is empty)
                map.getNPCs().removeIf(npc -> {
                    if (npc instanceof NPCs.OneRing) {
                        NPCs.OneRing ring = (NPCs.OneRing) npc;
                        if (ring.isCollected() && ring.hasFadedOut()) {
                            // Only close textbox if all messages have been read
                            if (map.getTextbox().isActive() && map.getTextbox().isTextQueueEmpty()) {
                                map.getTextbox().setIsActive(false);
                                keyLocker.unlockKey(Key.E);
                                System.out.println("Ring faded - textbox closed and E unlocked");
                            }
                            // Remove ring even if textbox still active (player still reading)
                            if (map.getTextbox().isTextQueueEmpty()) {
                                return true;
                            }
                        }
                    }
                    return false;
                });
                
                // Handle pending teleport with timer
                // if (pendingTeleportToGrass) {
                //     teleportTimer++;
                //     if (teleportTimer >= TELEPORT_DELAY) {
                //         // Remove FireTunic before teleporting
                //         map.getNPCs().removeIf(npc -> npc instanceof NPCs.FireTunic && ((NPCs.FireTunic) npc).isCollected());
                        
                //         System.out.println("Teleporting to GRASSLEVEL...");
                //         TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
                //         pendingTeleportToGrass = false;
                //         teleportTimer = 0;
                //     }
                // }

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