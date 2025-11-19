package Screens;

import Effects.FloatingText;
import Effects.ScreenFX; // For screen darkness effects
import Enemies.Bat;
import Enemies.Skull;
import Enemies.Spider;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Engine.Keyboard;
import Engine.Key;
import Flowers.Flower; // <-- ADDED: generic Flower handling
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.SpriteSheet;
import Level.*;
import Maps.VolcanoMap;
import Players.Bee;
import Portals.GrassPortal;
import Portals.Portal;
import StaticClasses.BeeStats;
import StaticClasses.EnemySpawner;
import StaticClasses.TeleportManager;
import Utils.Direction;
import NPCs.Volcano;
import NPCs.SauronEye;
import java.awt.Color;
import java.util.ArrayList;

public class VolcanoLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;

    private SpriteSheet stingFxSheet;
    
    // floating text for nectar collection
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();
    
    // Key locker for volcano interaction
    private Engine.KeyLocker eKeyLocker = new Engine.KeyLocker();

    // LOTR Easter Egg - Ring timer
    private boolean ringTimerStarted = false;
    private long ringTimerStart = 0;
    private static final long RING_TIMER_DURATION_MS = 10000; // 10 seconds
    private boolean ringHordeTriggered = false;
    
    // Ring textbox auto-dismiss
    private boolean ringTextboxShown = false;
    private int ringTextboxTimer = 0;
    private static final int RING_TEXTBOX_DURATION = 180; // 3 seconds at 60fps
    
    // Ring continuous shake
    private int ringShakeTimer = 0;
    private static final int RING_SHAKE_INTERVAL = 90; // shake every 1.5 seconds
    
    // Volcano destruction sequence
    private boolean destroyingRing = false;
    private int volcanoShakeTimer = 0;
    private static final int VOLCANO_SHAKE_DURATION = 180; // 3 seconds at 60fps

    public VolcanoLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        hasInitialized = true;
        flagManager = new FlagManager();
        flagManager.addFlag("hasRedEmerald", false);
        flagManager.addFlag("hasBlueEmerald", false);
        flagManager.addFlag("hasBothEmeralds", false);

        flagManager.addFlag("bossActive", false);

        map = new VolcanoMap();
        map.setFlagManager(flagManager);
        

        player = new Bee(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);
        map.setPlayer(player);

        map.getTextbox().setInteractKey(player.getInteractKey());
        map.addListener(this);

        map.preloadScripts();

        stingFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), 32, 32);
        
        // Enable ambient enemy spawning for this level
        EnemySpawner.setEnabled(true);
        EnemySpawner.resetTimer();
        
        // DEBUG: Check if volcano exists on map
        boolean volcanoFound = false;
        for (NPC npc : map.getNPCs()) {
            if (npc instanceof Volcano) {
                volcanoFound = true;
                System.out.println("[VolcanoLevel] ✓ Volcano found at: (" + npc.getX() + ", " + npc.getY() + ")");
            }
        }
        if (!volcanoFound) {
            System.out.println("[VolcanoLevel] ✗✗✗ WARNING: NO VOLCANO FOUND ON MAP!");
        }
        
        // LOTR Easter Egg - Check if player has the ring when entering
        if (BeeStats.hasRing() && !ringTimerStarted) {
            ringTimerStarted = true;
            ringTimerStart = System.currentTimeMillis();
            System.out.println("[VolcanoLevel] Player entered with the One Ring! Timer started...");
        }
        
        // check if bee has max nectar when entering level - trigger horde if so
        if (player instanceof Bee) {
            Bee bee = (Bee) player;
            if (bee.getNectar() >= bee.getNectarCap()) {
                System.out.println("VolcanoLevel: Bee entered with full nectar! Starting horde...");
                StaticClasses.UnleashMayhem.fire(map, bee);
            } else if (!StaticClasses.UnleashMayhem.isActive()) {
                // if horde is not active, clean up any leftover enemies from previous horde
                System.out.println("VolcanoLevel: Horde not active, cleaning up leftover enemies");
                map.getNPCs().removeIf(npc -> npc instanceof Spider || npc instanceof Bat);
            }
        }
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                // update floating texts
                floatingTexts.removeIf(text -> {
                    text.update();
                    return text.isDead();
                });

                // Update ambient enemy spawning
                if (player instanceof Bee) {
                    EnemySpawner.update(map, (Bee) player);
                    EnemySpawner.updateParticles();
                }

                if (player instanceof Bee) {
                    StaticClasses.HordeManager.update(map, (Bee) player);
                    StaticClasses.HordeManager.updateParticles();
                }

                // LOTR Easter Egg - Check ring timer
                if (ringTimerStarted && !ringHordeTriggered && BeeStats.hasRing()) {
                    long elapsed = System.currentTimeMillis() - ringTimerStart;
                    if (elapsed >= RING_TIMER_DURATION_MS) {
                        // Trigger the ring horde!
                        ringHordeTriggered = true;
                        map.getCamera().hordeShake();
                        
                        // Start pulsing red vignette
                        ScreenFX.start(ScreenFX.Effect.RED_VIGNETTE_PULSE, Integer.MAX_VALUE, 0.5f);
                        
                        // Show ominous textbox message
                        map.getTextbox().addText("The ring...it calls to me...");
                        map.getTextbox().addText("Dark whispers echo through the flames...");
                        map.getTextbox().addText("I must find a place to destroy this");
                        map.getTextbox().setIsActive(true);
                        ringTextboxShown = true;
                        ringTextboxTimer = 0;
                        
                        if (player instanceof Bee) {
                            StaticClasses.UnleashMayhem.fire(map, (Bee) player);
                        }
                        
                        System.out.println("[VolcanoLevel] Ring timer expired! The One Ring has summoned the horde!");
                    }
                }
                
                // Close textbox when player has read all messages
                if (ringTextboxShown && map.getTextbox().isActive() && map.getTextbox().isTextQueueEmpty()) {
                    map.getTextbox().setIsActive(false);
                    ringTextboxShown = false;
                    System.out.println("[VolcanoLevel] Ring textbox closed - all messages read");
                }
                
                // Prevent melee attack spam while textbox is active
                if (map.getTextbox().isActive()) {
                    if (player instanceof Bee) {
                        Bee bee = (Bee) player;
                        // Bee class should handle locking attacks if needed
                    }
                }
                
                // Continuous shake while ring horde is active
                if (ringHordeTriggered && BeeStats.hasRing() && !destroyingRing) {
                    ringShakeTimer++;
                    if (ringShakeTimer >= RING_SHAKE_INTERVAL) {
                        map.getCamera().shake();
                        ringShakeTimer = 0;
                    }
                }
                
                // Volcano destruction sequence with visual effects
                if (destroyingRing) {
                    volcanoShakeTimer++;
                    
                    // Shake continuously during destruction
                    if (volcanoShakeTimer % 20 == 0) {
                        map.getCamera().shake();
                    }
                    
                    // After shake duration, complete the destruction
                    if (volcanoShakeTimer >= VOLCANO_SHAKE_DURATION) {
                        System.out.println("[VolcanoLevel] Ring destruction complete! Restoring peace...");
                        
                        // Destroy the ring
                        BeeStats.setHasRing(false);
                        
                        // Remove ring icon from HUD
                        if (player instanceof Bee) {
                            Bee bee = (Bee) player;
                            if (bee.getPowerupHUD() != null) {
                                bee.getPowerupHUD().removeIcon("onering.png");
                            }
                        }
                        
                        // Clear screen darkness effect
                        ScreenFX.start(ScreenFX.Effect.NONE, 0, 0f);
                        
                        // Reset horde
                        StaticClasses.UnleashMayhem.reset();
                        System.out.println("[VolcanoLevel] The ring is destroyed! Peace returns...");
                        
                        // Remove all horde enemies from the map
                        map.getNPCs().removeIf(npc ->
                            npc instanceof Spider ||
                            npc instanceof Bat ||
                            npc instanceof Skull
                        );
                        
                        // Reset all ring state
                        ringTimerStarted = false;
                        ringHordeTriggered = false;
                        destroyingRing = false;
                        volcanoShakeTimer = 0;
                        ringShakeTimer = 0;
                        
                        // TODO: Award XP here when ready
                        // BeeStats.addXP(5000);
                    }
                }

                if (player instanceof Bee) {
                    Bee bee = (Bee) player;
                    
                    // Clear screen effects if bee just died
                    if (bee.isDead()) {
                        ScreenFX.start(ScreenFX.Effect.NONE, 0, 0f);
                    }
                    
                    if (bee.isDead() && bee.isDeathAnimationComplete()) {
                        screenCoordinator.setGameState(GameState.GAME_OVER);
                        return;
                    }

                    // Check for Volcano interaction (destroy the ring)
                    if (BeeStats.hasRing() && !destroyingRing) {
                        // Check for E key press with key locker
                        if (Keyboard.isKeyDown(Key.E) && !eKeyLocker.isKeyLocked(Key.E)) {
                            System.out.println("[VolcanoLevel] E key pressed! Checking volcano...");
                            eKeyLocker.lockKey(Key.E);
                            
                            for (NPC npc : map.getNPCs()) {
                                if (npc instanceof Volcano) {
                                    Volcano volcano = (Volcano) npc;
                                    
                                    // Volcano is scaled 8x from 32x32, so it's 256x256 pixels
                                    float volcanoCenterX = volcano.getX() + 128; // half of 256
                                    float volcanoCenterY = volcano.getY() + 128;
                                    
                                    float beeCenterX = bee.getX() + bee.getWidth() / 2f;
                                    float beeCenterY = bee.getY() + bee.getHeight() / 2f;
                                    
                                    float distance = (float) Math.sqrt(
                                        Math.pow(beeCenterX - volcanoCenterX, 2) +
                                        Math.pow(beeCenterY - volcanoCenterY, 2)
                                    );
                                    
                                    System.out.println("[VolcanoLevel] Bee position: (" + beeCenterX + ", " + beeCenterY + ")");
                                    System.out.println("[VolcanoLevel] Volcano position: (" + volcanoCenterX + ", " + volcanoCenterY + ")");
                                    System.out.println("[VolcanoLevel] Distance: " + distance + " (need < 350)");
                                    
                                    if (distance < 350) {
                                        // Player is close enough to the volcano - start destruction!
                                        System.out.println("[VolcanoLevel] ✓✓✓ SUCCESS! Throwing the One Ring into Mount Doom!");
                                        
                                        // Trigger epic volcano visual effects
                                        volcano.triggerRingDestroyFX();

                                        // Crumble all Sauron towers on this map
                                        for (NPC otherNpc : map.getNPCs()) {
                                            if (otherNpc instanceof SauronEye) {
                                                ((SauronEye) otherNpc).destroyEye();
                                                System.out.println("[VolcanoLevel] SauronEye crumbled!");
                                            }
                                        }
                                        
                                        // Stop horde from attacking immediately
                                        if (ringHordeTriggered || StaticClasses.UnleashMayhem.isActive()) {
                                            StaticClasses.UnleashMayhem.cease(map);
                                            System.out.println("[VolcanoLevel] Horde ceased! Enemies stop attacking.");
                                        }
                                        
                                        destroyingRing = true;
                                        volcanoShakeTimer = 0;
                                        
                                        break;
                                    } else {
                                        System.out.println("[VolcanoLevel] ✗✗✗ TOO FAR! Get closer!");
                                    }
                                }
                            }
                        }
                        
                        // Unlock E key when released
                        if (Keyboard.isKeyUp(Key.E)) {
                            eKeyLocker.unlockKey(Key.E);
                        }
                    }

                    // --- MELEE ATTACK COLLISIONS ---
                    if (bee.isAttacking()) {
                        java.awt.Rectangle sting = bee.getAttackHitbox();

                        java.util.ArrayList<NPC> npcsCopy = new java.util.ArrayList<>(map.getNPCs());

                        for (NPC npc : npcsCopy) {
                            if (npc instanceof Spider) {
                                Spider sp = (Spider) npc;

                                if (!sp.isDead() && sting.intersects(sp.getHitbox())) {
                                    sp.takeDamage(BeeStats.getAttackDamage());
                                    System.out.println("Bee stung spider!");
                                }
                            }

                            if (npc instanceof Bat) {
                                Bat bat = (Bat) npc;

                                if (!bat.isDead() && sting.intersects(bat.getHitbox())) {
                                    bat.takeDamage(BeeStats.getAttackDamage());
                                    System.out.println("Bee stung bat!");
                                }
                            }

                            if (npc instanceof Skull) {
                                Skull skull = (Skull) npc;

                                if (!skull.isDead() && sting.intersects(skull.getHitbox())) {
                                    skull.takeDamage(BeeStats.getAttackDamage());
                                    System.out.println("Bee stung skull!");
                                }
                            }

                            // --- FLOWER NECTAR + FLOATING TEXT (same as Grass) ---
                            if (npc instanceof Flower) {
                                Flower flower = (Flower) npc;

                                if (sting.intersects(flower.getHitbox())) {
                                    System.out.println("Sunflower hit!");

                                    int added = bee.tryAddNectar(1);
                                    if (added > 0) {
                                        System.out.println("Nectar collected: " +
                                            bee.getNectar() + "/" + bee.getNectarCap());

                                        // spawn yellow +1 floating text at flower
                                        float textX = flower.getX() + 24;
                                        float textY = flower.getY();
                                        floatingTexts.add(new FloatingText(
                                            textX,
                                            textY,
                                            "+1",
                                            new Color(255, 215, 0)
                                        ));
                                    } else {
                                        System.out.println("Pouch full! Deposit at the hive.");
                                    }
                                }
                            }

                            // if (npc instanceof Portal) {
                            //     Portal portal = (Portal) npc;

                            //     if (sting.intersects(portal.getHitbox())) {
                            //         TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
                            //     }
                            // }

                            // if (npc instanceof GrassPortal) {
                            //     GrassPortal grassPortal = (GrassPortal) npc;

                            //     if (sting.intersects(grassPortal.getHitbox())) {
                            //         TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
                            //     }
                            // }
                        }
                    }
                }
                
                map.getNPCs().removeIf(npc -> npc instanceof Spider && ((Spider) npc).canBeRemoved());
                map.getNPCs().removeIf(npc -> npc instanceof Bat && ((Bat) npc).shouldRemove());
                map.getNPCs().removeIf(npc -> npc instanceof Skull && ((Skull) npc).shouldRemove());

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
            return;
        }

        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                
                StaticClasses.HordeManager.drawParticles(graphicsHandler,
                    map.getCamera().getX(),
                    map.getCamera().getY());
                
                EnemySpawner.drawParticles(graphicsHandler,
                    map.getCamera().getX(),
                    map.getCamera().getY());
                
                if (stingFxSheet != null) {
                    float cameraX = map.getCamera().getX();
                    float cameraY = map.getCamera().getY();

                    for (NPC npc : map.getNPCs()) {
                        if (npc instanceof Spider) {
                            Spider sp = (Spider) npc;
                            if (sp.isShowingAttackFx()) {
                                int fxSize = 64;
                                
                                int fxX = Math.round(sp.getX() - cameraX);
                                int fxY = Math.round(sp.getY() - cameraY);
                                
                                fxX -= 10;
                                fxY += 15;

                                graphicsHandler.drawImage(
                                    stingFxSheet.getSprite(0, 0),
                                    fxX, fxY, fxSize, fxSize
                                );
                            }
                            
                            // draw floating damage numbers for spiders
                            sp.drawFloatingTexts(graphicsHandler, cameraX, cameraY);
                        }
                        
                        if (npc instanceof Bat) {
                            Bat bat = (Bat) npc;
                            if (bat.isShowingAttackFx()) {
                                int fxSize = 64;
                                
                                int fxX = Math.round(bat.getX() - cameraX);
                                int fxY = Math.round(bat.getY() - cameraY);
                                
                                fxX += 32;
                                fxY += 32;

                                graphicsHandler.drawImage(
                                    stingFxSheet.getSprite(0, 0),
                                    fxX, fxY, fxSize, fxSize
                                );
                            }
                            
                            // draw floating damage numbers for bats
                            bat.drawFloatingTexts(graphicsHandler, cameraX, cameraY);
                        }
                    }
                }
                
                // draw floating texts for nectar collection
                for (FloatingText text : new ArrayList<>(floatingTexts)) {
                    text.draw(graphicsHandler, map.getCamera().getX(), map.getCamera().getY());
                }
                
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
        // Clear any lingering screen effects
        ScreenFX.start(ScreenFX.Effect.NONE, 0, 0f);
        
        StaticClasses.UnleashMayhem.reset();
        EnemySpawner.resetTimer();
        ringTimerStarted = false;
        ringHordeTriggered = false;
        ringTextboxShown = false;
        ringTextboxTimer = 0;
        destroyingRing = false;
        volcanoShakeTimer = 0;
        ringShakeTimer = 0;
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
