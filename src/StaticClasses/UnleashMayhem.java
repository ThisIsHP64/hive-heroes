package StaticClasses;

import Level.Map;
import Players.Bee;

public final class UnleashMayhem {
    private static boolean active = false;

    public static boolean isActive() { 
        return active; 
    }

    public static void fire(Map map, Bee bee) {
        if (active || map == null || bee == null) 
            return;
        active = true;
        HordeManager.startHorde(map, bee);
        map.getCamera().hordeShake();
        Sound.MusicManager.stopGpLoop();
        Sound.MusicManager.playSiegeLoop();
        System.out.println("[Mayhem] HORDE STARTED");
    }

    public static void cease(Map map) {
        if (!active) 
            return;
        active = false;
        HordeManager.stopHorde(map);
        Sound.MusicManager.stopSiegeLoop();
        Sound.MusicManager.playGpLoop();
        System.out.println("[Mayhem] HORDE STOPPED");
    }

    public static void reset() {
        if (!active) {
            // even if not active, force clear any lingering horde
            HordeManager.stopHorde(null);
            return;
        }
        active = false;
        HordeManager.stopHorde(null);
        Sound.MusicManager.stopSiegeLoop();
        Sound.MusicManager.playGpLoop();
        System.out.println("[Mayhem] HORDE RESET");
    }
}