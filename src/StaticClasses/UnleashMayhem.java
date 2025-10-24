package StaticClasses;

import Level.Map;
import Players.Bee;

public final class UnleashMayhem {
    private static boolean active = false;

    public static boolean isActive() { return active; }

    public static void fire(Map map, Bee bee) {
        if (active || map == null || bee == null) return;
        active = true;
        HordeManager.startHorde(map, bee);
        // (Optional FX) bee.showPowerupIcon("mayhem_icon.png", 1200);
        System.out.println("[Mayhem] HORDE STARTED");
    }

    public static void cease(Map map) {
        if (!active) return;
        active = false;
        HordeManager.stopHorde(map);
        System.out.println("[Mayhem] HORDE STOPPED");
    }

    /** For safety if player dies or map unloads. */
    public static void reset() {
        active = false;
    }
}
