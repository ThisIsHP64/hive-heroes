package Screens;

import Effects.FloatingText;
import Enemies.Bat;
import Enemies.Spider;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Flowers.Flower;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.SpriteSheet;
import Level.*;
import Maps.GrassMap;
import NPCs.BigHive;
import Players.Bee;
import StaticClasses.BeeStats;
import StaticClasses.EnemySpawner;
import StaticClasses.TeleportManager;
import StaticClasses.UnleashMayhem;
import Utils.Direction;
import java.awt.Color;
import java.util.ArrayList;

public class GrassLevelScreen extends Screen implements GameListener {
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

    public GrassLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        hasInitialized = true;
        flagManager = new FlagManager();
        // flagManager.addFlag("isLevel2", true);
        // flagManager.addFlag("isLevel3", true);
        // flagManager.addFlag("isLevel4", true);

        // flagManager.addFlag("hasCollected15Nectar", true);
        // flagManager.addFlag("hasCollected50Nectar", true);
        // flagManager.addFlag("hasCollected100Nectar", true);
        // flagManager.addFlag("hasCollected150Nectar", true);

        flagManager.addFlag("hasCollected15Nectar", false);
        flagManager.addFlag("hasCollected50Nectar", false);
        flagManager.addFlag("hasCollected100Nectar", false);
        flagManager.addFlag("hasCollected150Nectar", false);

        flagManager.addFlag("isLevel2", false);
        flagManager.addFlag("isLevel3", false);
        flagManager.addFlag("isLevel4", false);

        map = new GrassMap();
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
        
        // check if bee has max nectar when entering level - trigger horde if so
        if (player instanceof Bee) {
            Bee bee = (Bee) player;
            if (bee.getNectar() >= bee.getNectarCap()) {
                System.out.println("GrassLevel: Bee entered with full nectar! Starting horde...");
                UnleashMayhem.fire(map, bee);
            } else if (!StaticClasses.UnleashMayhem.isActive()) {
                // if horde is not active, clean up any leftover enemies from previous horde
                System.out.println("GrassLevel: Horde not active, cleaning up leftover enemies");
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
                
                if (player instanceof Bee) {
                    Bee bee = (Bee) player;
                    if (bee.isDead() && bee.isDeathAnimationComplete()) {
                        screenCoordinator.setGameState(GameState.GAME_OVER);
                        return;
                    }
                    if (bee.isAttacking()) {
                        java.awt.Rectangle sting = bee.getAttackHitbox();
                        ArrayList<NPC> npcsCopy = new ArrayList<>(map.getNPCs());
                        for (NPC npc : npcsCopy) {
                            if (npc instanceof Spider) {
                                Spider sp = (Spider) npc;

                                if (!sp.isDead() && sting.intersects(sp.getHitbox())) {
                                    sp.takeDamage(BeeStats.getAttackDamage());
                                    BeeStats.setExperience(BeeStats.getExperience() + 1);
                                    
                                    System.out.println("Bee stung spider!");
                                }
                            }
                            
                            if (npc instanceof Bat) {
                                Bat bat = (Bat) npc;

                                if (!bat.isDead() && sting.intersects(bat.getHitbox())) {
                                    bat.takeDamage(BeeStats.getAttackDamage());
                                    BeeStats.setExperience(BeeStats.getExperience() + 1);
                                    System.out.println("Bee stung bat!");
                                }
                            }


                            if (npc instanceof Flower) {
                                Flower flower = (Flower) npc;

                                if (sting.intersects(flower.getHitbox())) {
                                    System.out.println("Sunflower hit!");

                                    int added = bee.tryAddNectar(1);
                                    if (added > 0) {
                                        System.out.println(
                                                "Nectar collected: " + bee.getNectar() + "/" + bee.getNectarCap());
                                        
                                        // spawn yellow +1 floating text at sunflower
                                        float textX = flower.getX() + 24;
                                        float textY = flower.getY();
                                        floatingTexts.add(new FloatingText(textX, textY, "+1", new Color(255, 215, 0)));
                                    } else {
                                        System.out.println("Pouch full! Deposit at the hive.");
                                    }
                                }
                            }

                            if (npc instanceof BigHive) {
                                BigHive bigHive = (BigHive) npc;

                                if (sting.intersects(bigHive.getHitbox())) {
                                    TeleportManager.setCurrentScreen(GameState.HIVELEVEL);
                                    BeeStats.restoreAllStats();
                                }
                            }
                        }
                    }
                }

                map.getNPCs().removeIf(npc -> npc instanceof Spider && ((Spider) npc).canBeRemoved());
                map.getNPCs().removeIf(npc -> npc instanceof Bat && ((Bat) npc).shouldRemove());

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
                                        fxX, fxY, fxSize, fxSize);
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
                                        fxX, fxY, fxSize, fxSize);
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
        StaticClasses.UnleashMayhem.reset();
        EnemySpawner.resetTimer();
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