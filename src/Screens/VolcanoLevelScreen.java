package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.VolcanoMap;
import Players.Bee;
import StaticClasses.BeeStats;
import StaticClasses.TeleportManager;
import Utils.Direction;
import Portals.GrassPortal;
import Portals.Portal;
import NPCs.RareSunflowerwithFlowers;
import Enemies.Spider;
import Enemies.Bat;

import Engine.ImageLoader;
import GameObject.SpriteSheet;

public class VolcanoLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;

    // sting FX resource - single static image shown when spider is hit
    private SpriteSheet stingFxSheet;

    public VolcanoLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        hasInitialized = true;
        flagManager = new FlagManager();

        map = new VolcanoMap();
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

        // spiders are now spawned in SprintOneMap.loadNPCs() instead of here

        // load the sting FX - just one static sprite
        stingFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), 32, 32);
    }

    public void update() {
        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);

                // update horde manager every frame
                if (player instanceof Bee) {
                    StaticClasses.HordeManager.update(map, (Bee) player);
                    StaticClasses.HordeManager.updateParticles();
                }

                // check if bee died and death animation finished
                if (player instanceof Bee) {
                    Bee bee = (Bee) player;
                    
                    // transition to game over after death animation completes
                    if (bee.isDead() && bee.isDeathAnimationComplete()) {
                        screenCoordinator.setGameState(GameState.GAME_OVER);
                        return;
                    }

                    if (bee.isAttacking()) {
                        java.awt.Rectangle sting = bee.getAttackHitbox();

                        // create a copy to avoid concurrent modification when enemies spawn during horde
                        java.util.ArrayList<NPC> npcsCopy = new java.util.ArrayList<>(map.getNPCs());

                        for (NPC npc : npcsCopy) {
                            if (npc instanceof Spider) {
                                Spider sp = (Spider) npc;

                                // only deal damage if spider isn't already dead
                                if (!sp.isDead() && sting.intersects(sp.getHitbox())) {
                                    sp.takeDamage(1);
                                    System.out.println("Bee stung spider!");
                                }
                            }

                            // handle bat collisions
                            if (npc instanceof Bat) {
                                Bat bat = (Bat) npc;

                                // only deal damage if bat isn't already dead
                                if (!bat.isDead() && sting.intersects(bat.getHitbox())) {
                                    bat.takeDamage(1);
                                    System.out.println("Bee stung bat!");
                                }
                            }

                            // handle sunflower nectar collection
                            if (npc instanceof RareSunflowerwithFlowers) {
                                RareSunflowerwithFlowers rareSunflower = (RareSunflowerwithFlowers) npc;
                                
                                if (sting.intersects(rareSunflower.getHitbox())) {
                                    System.out.println("Sunflower hit!");
                                    
                                    // use Bee's nectar API so cap logic + mayhem trigger run properly
                                    int added = bee.tryAddNectar(1);
                                    if (added > 0) {
                                        System.out.println("Nectar collected: " + bee.getNectar() + "/" + bee.getNectarCap());
                                    } else {
                                        System.out.println("Pouch full! Deposit at the hive.");
                                    }
                                }
                            }

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
                
                // remove dead spiders after death animation lingers
                map.getNPCs().removeIf(npc -> npc instanceof Spider && ((Spider) npc).canBeRemoved());

                // remove dead bats after death animation lingers
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
            return; // wait until initialize() runs
        }

        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                
                // draw smoke particles from horde spawns
                StaticClasses.HordeManager.drawParticles(graphicsHandler, 
                    map.getCamera().getX(), 
                    map.getCamera().getY());
                
                // draw attack FX on spiders and bats that were just hit
                if (stingFxSheet != null) {
                    float cameraX = map.getCamera().getX();
                    float cameraY = map.getCamera().getY();

                    for (NPC npc : map.getNPCs()) {
                        if (npc instanceof Spider) {
                            Spider sp = (Spider) npc;
                            if (sp.isShowingAttackFx()) {
                                // position FX directly on spider sprite
                                int fxSize = 64;
                                
                                // start at spider's sprite position
                                int fxX = Math.round(sp.getX() - cameraX);
                                int fxY = Math.round(sp.getY() - cameraY);
                                
                                // shift down and left to center on spider body
                                fxX -= 10;
                                fxY += 15;

                                graphicsHandler.drawImage(
                                    stingFxSheet.getSprite(0, 0),
                                    fxX, fxY, fxSize, fxSize
                                );
                            }
                        }
                        
                        // draw attack FX on bats
                        if (npc instanceof Bat) {
                            Bat bat = (Bat) npc;
                            if (bat.isShowingAttackFx()) {
                                // position FX directly on bat sprite
                                int fxSize = 64;
                                
                                // start at bat's sprite position
                                int fxX = Math.round(bat.getX() - cameraX);
                                int fxY = Math.round(bat.getY() - cameraY);
                                
                                // center on bat body - adjust these to position on bat
                                fxX += 32; // shift right to center on scaled bat
                                fxY += 32; // shift down to center on scaled bat

                                graphicsHandler.drawImage(
                                    stingFxSheet.getSprite(0, 0),
                                    fxX, fxY, fxSize, fxSize
                                );
                            }
                        }
                    }
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
        StaticClasses.UnleashMayhem.reset(); // stop horde on retry
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