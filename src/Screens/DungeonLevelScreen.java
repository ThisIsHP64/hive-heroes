package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Flowers.RareSunflowerwithFlowers;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.DungeonMap;

import Players.Bee;
import StaticClasses.BeeStats;
import StaticClasses.TeleportManager;
import Utils.Direction;
import Portals.GrassPortal;
import Portals.Portal;
import Enemies.Spider;

import Engine.ImageLoader;
import GameObject.SpriteSheet;

public class DungeonLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;

    // sting FX resource - single static image shown when spider is hit
    private SpriteSheet stingFxSheet;

    public DungeonLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        hasInitialized = true;
        flagManager = new FlagManager();

        map = new DungeonMap();
        map.setFlagManager(flagManager);

        flagManager.addFlag("collectedFrostTunic", false);

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

                        for (NPC npc : map.getNPCs()) {
                            if (npc instanceof Spider) {
                                Spider sp = (Spider) npc;

                                // only deal damage if spider isn't already dead
                                if (!sp.isDead() && sting.intersects(sp.getHitbox())) {
                                    sp.takeDamage(1);
                                    System.out.println("Bee stung spider!");
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

                // remove dead spiders after death animation lingers
                map.getNPCs().removeIf(npc -> npc instanceof Spider && ((Spider) npc).canBeRemoved());

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

                // draw attack FX on spiders that were just hit
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
                                        fxX, fxY, fxSize, fxSize);
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