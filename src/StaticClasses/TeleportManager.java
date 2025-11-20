package StaticClasses;

import Game.GameState;
import Game.ScreenCoordinator;
import Sound.SFX;
import Sound.SFXManager;

public class TeleportManager {
    private static ScreenCoordinator teleportManager;

    private static boolean isBossActive = false;

    public static boolean isBossActive() {
        return isBossActive;
    }

    public static void setBossActive(boolean isBossActive) {
        TeleportManager.isBossActive = isBossActive;
    }

    public static void call(ScreenCoordinator screenCoordinator) {
        teleportManager = screenCoordinator;
    }

    public static void setCurrentScreen(GameState gameState) {
        if (teleportManager != null) {
            SFXManager.playSFX(SFX.MOVE);
            teleportManager.setGameState(gameState);
        }
    }

    public static GameState getCurrentGameState() {
        return teleportManager.getGameState();
    }

    public static void setCurrentGameState(GameState newGameState) {
        
        teleportManager.setCurrentGameState(newGameState);


    }
}
