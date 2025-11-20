package Screens;

import Effects.FloatingText;
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
import NPCs.QueenBee;
import Players.Bee;
import StaticClasses.BeeStats;
import StaticClasses.HiveManager;
import Utils.Direction;
import java.awt.Color;
import java.util.ArrayList;

public class HiveLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    protected boolean hasInitialized = false;

    private SpriteSheet stingFxSheet;
    
    // floating text for nectar deposits
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    public HiveLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }
    
    public void initialize() {
        
        hasInitialized = true;
        flagManager = new FlagManager();
        flagManager.addFlag("hasTalkedToQueen", false);
        flagManager.addFlag("introductionAdministered", false);
        flagManager.addFlag("controlsReviewed", false);

        flagManager.addFlag("hasGreenEmerald", false);
        flagManager.addFlag("hasBlueEmerald", false);
        flagManager.addFlag("hasRedEmerald", false);

        flagManager.addFlag("hasBothEmeralds", false);

        map = new HiveMap();
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

                if (player instanceof Bee) {
                    Bee bee = (Bee) player;

                    if (bee.isDead() && bee.isDeathAnimationComplete()) {
                        screenCoordinator.setGameState(GameState.GAME_OVER);
                        return;
                    }

                    for (NPC npc : map.getNPCs()) {
                        java.awt.Rectangle sting = bee.getAttackHitbox();

                        if (npc instanceof QueenBee) {
                            QueenBee queenBee = (QueenBee) npc;
                            if (sting.intersects(queenBee.getHitbox()) && BeeStats.getNectar() > 0) {
                                HiveManager.depositNectar(map);
                                BeeStats.setNectar(BeeStats.getNectar() - 1);
                                
                                // spawn green -1 floating text at queen (showing deposit)
                                float textX = queenBee.getX() + 24;
                                float textY = queenBee.getY();
                                floatingTexts.add(new FloatingText(textX, textY, "+1", new Color(0, 255, 0)));
                            }
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
            return;
        }

        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                
                // draw floating texts for nectar deposits
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