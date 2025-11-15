package StaticClasses;

import Level.Map;
import Players.Bee;
import Sound.Music;

public final class UnleashMayhem {
    private static boolean active = false;

    public static boolean isActive() { 
        return active; 
    }

    public static void fire(Map map, Bee bee) {
        if (map == null || bee == null) 
            return;
        
        // if already active, respawn a wave instead of restarting
        if (active) {
            System.out.println("[Mayhem] Already active - spawning reinforcements for new level");
            HordeManager.respawnWaveForNewLevel(map, bee);
            return;
        }
        
        active = true;
        HordeManager.startHorde(map, bee);
        map.getCamera().hordeShake();
        Sound.MusicManager.stopAll();
        Sound.MusicManager.playLoop(Music.SIEGE);
        System.out.println("[Mayhem] HORDE STARTED");
    }

    public static void cease(Map map) {
        if (!active) 
            return;
        active = false;
        HordeManager.stopHorde(map);
        Sound.MusicManager.stopAll();
        Sound.MusicManager.playLoop(Music.GRASS);
        System.out.println("[Mayhem] HORDE STOPPED");
    }

    public static void reset() {
        if (!active) {
            HordeManager.stopHorde(null);
            return;
        }
        active = false;
        HordeManager.stopHorde(null);
        Sound.MusicManager.stopAll();
        Sound.MusicManager.playLoop(Music.GRASS);
        System.out.println("[Mayhem] HORDE RESET");
    }
}