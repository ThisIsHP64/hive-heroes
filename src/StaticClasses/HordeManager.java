package StaticClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Enemies.Spider;
import Level.Map;
import Players.Bee;
import Utils.Point;

public final class HordeManager {
    // --- Tuning knobs (v1) ---
    private static final int HORDE_INITIAL = 10;
    private static final int HORDE_REINFORCE = 4;
    private static final int HORDE_MAX = 18;
    private static final long REINFORCE_AT_MS = 25_000;
    private static final long TIMEOUT_MS = 90_000;
    private static final float SPEED_MULT = 1.6f;

    private static final int ROAM_NEAR_TARGET = 6;
    private static final int ROAM_GLOBAL_CAP = 12;

    // --- State ---
    private static boolean running = false;
    private static long startedAt = 0L;
    private static final ArrayList<Spider> horde = new ArrayList<>();
    private static final Random rng = new Random();

    public static boolean isRunning() {
        return running;
    }

    public static void startHorde(Map map, Bee bee) {
        if (running)
            return;
        running = true;
        startedAt = System.currentTimeMillis();
        spawnWave(map, bee, HORDE_INITIAL);
        setHordeMode(true);
    }

    public static void stopHorde(Map map) {
        setHordeMode(false);
        // mark horde spiders as dead so they fade out naturally
        for (Spider s : horde) {
            s.takeDamage(999);
        }
        horde.clear();
        running = false;
    }

    public static void update(Map map, Bee bee) {
        if (map == null || bee == null)
            return;

        // Clean up any dead/removed spiders in our list
        for (Iterator<Spider> it = horde.iterator(); it.hasNext();) {
            Spider s = it.next();
            if (s == null || s.canBeRemoved())
                it.remove();
        }

        if (running) {
            long now = System.currentTimeMillis();
            // Reinforce once at 25s (up to HORDE_MAX)
            if ((now - startedAt) >= REINFORCE_AT_MS && countAlive() < HORDE_MAX) {
                int need = Math.min(HORDE_REINFORCE, HORDE_MAX - countAlive());
                spawnWave(map, bee, need);
            }
            // Timeout failsafe
            if ((now - startedAt) >= TIMEOUT_MS) {
                UnleashMayhem.cease(map);
            }
        } else {
            // Maintain a small roaming population near the player
            int globalSpiders = countMapSpiders(map);
            if (globalSpiders < ROAM_GLOBAL_CAP) {
                int near = countSpidersNear(map, bee, 25 * 64);
                if (near < ROAM_NEAR_TARGET) {
                    spawnWave(map, bee, 1);
                }
            }
        }
    }

    private static void spawnWave(Map map, Bee bee, int count) {
        System.out.println("[HordeManager] spawnWave called - spawning " + count + " spiders");
        System.out.println("[HordeManager] Bee position: " + bee.getX() + ", " + bee.getY());
        System.out.println("[HordeManager] Current NPC count: " + map.getNPCs().size());
        
        for (int i = 0; i < count; i++) {
            Point spawn = pickSpawnOutsideCamera(map, bee);
            Spider s = new Spider(2000 + i + (int) (System.currentTimeMillis() % 10000), spawn);
            s.setHordeAggression(SPEED_MULT, running);
            map.getNPCs().add(s);
            s.setMap(map);
            horde.add(s);
            System.out.println("[HordeManager] Spawned horde spider #" + i + " at: " + spawn.x + ", " + spawn.y);
        }
        
        System.out.println("[HordeManager] After spawn, NPC count: " + map.getNPCs().size());
        System.out.println("[HordeManager] Horde list size: " + horde.size());
    }

    private static void setHordeMode(boolean on) {
        for (Spider s : horde) {
            if (s != null)
                s.setHordeAggression(SPEED_MULT, on);
        }
    }

    private static int countAlive() {
        return horde.size();
    }

    private static int countMapSpiders(Map map) {
        int c = 0;
        for (var npc : map.getNPCs())
            if (npc instanceof Spider)
                c++;
        return c;
    }

    private static int countSpidersNear(Map map, Bee bee, int radiusPx) {
        int c = 0;
        int bx = (int) bee.getX(), by = (int) bee.getY();
        int r2 = radiusPx * radiusPx;
        for (var npc : map.getNPCs()) {
            if (npc instanceof Spider) {
                int dx = (int) npc.getX() - bx;
                int dy = (int) npc.getY() - by;
                if (dx * dx + dy * dy <= r2)
                    c++;
            }
        }
        return c;
    }

private static Point pickSpawnOutsideCamera(Map map, Bee bee) {
    int tile = 64;
    int minTiles = 3, maxTiles = 5; // spawn very close - 3-5 tiles (192-320 pixels)
    int dist = (rng.nextInt(maxTiles - minTiles + 1) + minTiles) * tile;
    double ang = rng.nextDouble() * Math.PI * 2;
    int x = (int) (bee.getX() + Math.cos(ang) * dist);
    int y = (int) (bee.getY() + Math.sin(ang) * dist);
    return new Point(x, y);
}
    public static void onDeposit(Map map) {
        if (running)
            UnleashMayhem.cease(map);
    }
}