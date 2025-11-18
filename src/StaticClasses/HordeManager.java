package StaticClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Enemies.Spider;
import Enemies.Bat;
import Enemies.FrostDragon;
import Enemies.Crab;
import Enemies.Goblin;
import Level.Map;
import Level.NPC;
import Players.Bee;
import Utils.Point;
import Effects.SmokeParticle;

public final class HordeManager {

    // --- Horde tuning knobs ---
    // trying to hit: "oh crap this is scary" but not "insta-wipe"
    private static final int HORDE_INITIAL    = 6;       // first spike when mayhem starts
    private static final int HORDE_REINFORCE  = 2;       // how many join later
    private static final int HORDE_MAX        = 15;      // hard cap of horde bodies on map

    private static final long REINFORCE_AT_MS = 20_000;  // when reinforcements show up
    private static final long TIMEOUT_MS      = 75_000;  // how long mayhem can run before auto-stop

    // how juiced enemies get when in horde mode (applied inside enemies)
    private static final float SPEED_MULT     = 5.0f;    // Horde speed multiplier

    // roaming spawn tuning when horde is NOT active
    private static final int ROAM_NEAR_TARGET = 5;       // how many enemies we want "near" the bee
    private static final int ROAM_GLOBAL_CAP  = 10;      // overall cap of enemies we allow on map

    // --- State ---
    private static boolean running = false;
    private static long startedAt = 0L;
    private static final ArrayList<NPC> horde = new ArrayList<>();
    private static final ArrayList<SmokeParticle> particles = new ArrayList<>();
    private static final Random rng = new Random();

    public static boolean isRunning() {
        return running;
    }

    public static void setIsRunning(boolean value) {
        running = value;
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
        
        // hard-delete all enemies we tracked as "horde"
        for (NPC npc : horde) {
            if (npc instanceof Spider) {
                ((Spider) npc).takeDamage(999);
            } else if (npc instanceof Bat) {
                ((Bat) npc).takeDamage(999);
            } else if (npc instanceof FrostDragon) {
                ((FrostDragon) npc).takeDamage(999);
            } else if (npc instanceof Crab) {
                ((Crab) npc).takeDamage(999);
            } else if (npc instanceof Goblin) {
                ((Goblin) npc).takeDamage(999);
            }
        }
        
        // also clean up any horde enemies that might still be in the map's NPC list
        if (map != null) {
            map.getNPCs().removeIf(npc -> {
                if (npc instanceof Spider) {
                    Spider sp = (Spider) npc;
                    if (!sp.isDead()) {
                        sp.takeDamage(999);
                    }
                    return sp.canBeRemoved();
                } else if (npc instanceof Bat) {
                    Bat bat = (Bat) npc;
                    if (!bat.isDead()) {
                        bat.takeDamage(999);
                    }
                    return bat.shouldRemove();
                } else if (npc instanceof FrostDragon) {
                    FrostDragon dragon = (FrostDragon) npc;
                    if (!dragon.isDead()) {
                        dragon.takeDamage(999);
                    }
                    return dragon.canBeRemoved();
                } else if (npc instanceof Crab) {
                    Crab crab = (Crab) npc;
                    if (!crab.isDead()) {
                        crab.takeDamage(999);
                    }
                    return crab.canBeRemoved();
                } else if (npc instanceof Goblin) {
                    Goblin goblin = (Goblin) npc;
                    if (!goblin.isDead()) {
                        goblin.takeDamage(999);
                    }
                    return goblin.canBeRemoved();
                }
                return false;
            });
        }
        
        horde.clear();
        running = false;
    }

    // spawns a fresh wave when entering a new level while horde is active
    public static void respawnWaveForNewLevel(Map map, Bee bee) {
        if (!running || map == null || bee == null) 
            return;
        
        System.out.println("[HordeManager] Respawning wave for new level");
        
        // clear old horde enemies from previous level
        horde.clear();
        
        // spawn new wave appropriate for this level
        spawnWave(map, bee, HORDE_INITIAL);
    }

    public static void update(Map map, Bee bee) {
        if (map == null || bee == null)
            return;

        // clean up dead/removed horde members from the list
        for (Iterator<NPC> it = horde.iterator(); it.hasNext();) {
            NPC npc = it.next();
            if (npc == null) {
                it.remove();
                continue;
            }
            
            boolean shouldRemove = false;
            if (npc instanceof Spider) {
                shouldRemove = ((Spider) npc).canBeRemoved();
            } else if (npc instanceof Bat) {
                shouldRemove = ((Bat) npc).shouldRemove();
            } else if (npc instanceof FrostDragon) {
                shouldRemove = ((FrostDragon) npc).canBeRemoved();
            } else if (npc instanceof Crab) {
                shouldRemove = ((Crab) npc).canBeRemoved();
            } else if (npc instanceof Goblin) {
                shouldRemove = ((Goblin) npc).canBeRemoved();
            }
            
            if (shouldRemove) {
                it.remove();
            }
        }

        if (running) {
            long now = System.currentTimeMillis();

            // if we're mid-mayhem and under cap, drip in reinforcements once
            if ((now - startedAt) >= REINFORCE_AT_MS && countAlive() < HORDE_MAX) {
                int need = Math.min(HORDE_REINFORCE, HORDE_MAX - countAlive());
                spawnWave(map, bee, need);
            }

            // if we've been in full chaos too long, auto shut off
            if ((now - startedAt) >= TIMEOUT_MS) {
                UnleashMayhem.cease(map);
            }
        } else {
            // "background" roaming logic when mayhem isn't active
            int globalEnemies = countMapEnemies(map);
            if (globalEnemies < ROAM_GLOBAL_CAP) {
                int near = countEnemiesNear(map, bee, 25 * 64);
                if (near < ROAM_NEAR_TARGET) {
                    // just drip in a single guy to keep pressure up
                    spawnWave(map, bee, 1);
                }
            }
        }
    }

    public static void updateParticles() {
        particles.removeIf(p -> {
            p.update();
            return p.isDead();
        });
    }

    public static void drawParticles(Engine.GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        for (SmokeParticle p : particles) {
            p.draw(graphicsHandler, cameraX, cameraY);
        }
    }

    private static void spawnWave(Map map, Bee bee, int count) {
        System.out.println("[HordeManager] spawnWave called - spawning " + count + " enemies");
        System.out.println("[HordeManager] Bee position: " + bee.getX() + ", " + bee.getY());
        System.out.println("[HordeManager] Current NPC count: " + map.getNPCs().size());
        
        String mapName = map.getClass().getSimpleName();
        boolean isGrassMap = mapName.equals("GrassMap");
        boolean isFrostMap = mapName.equals("FrostMap") || mapName.equals("DungeonMap");
        
        for (int i = 0; i < count; i++) {
            Point spawn = pickSpawnOutsideCamera(map, bee);
            NPC enemy;

            if (isGrassMap) {
                // grass level: only spiders, classic swarm
                Spider s = new Spider(2000 + i + (int) (System.currentTimeMillis() % 10000), spawn);
                s.setHordeAggression(SPEED_MULT, running);
                s.setMap(map);
                enemy = s;
                System.out.println("[HordeManager] Spawned horde Spider #" + i + " at: " + spawn.x + ", " + spawn.y);

            } else if (isFrostMap) {
                // frost level: mix of FrostDragon, Crab, Goblin
                // lighter on dragon so it's not a dragon parade
                int roll = rng.nextInt(100);
                
                if (roll < 15) {
                    // 15% FrostDragon (mini-boss vibe)
                    FrostDragon dragon = new FrostDragon(spawn);
                    dragon.setHordeAggression(2.0f, running); // slightly less cracked than others
                    dragon.setMap(map);
                    enemy = dragon;
                    System.out.println("[HordeManager] Spawned horde FrostDragon #" + i + " at: " + spawn.x + ", " + spawn.y);
                } else if (roll < 55) {
                    // 40% Crab
                    Crab crab = new Crab(spawn);
                    crab.setHordeAggression(SPEED_MULT, running);
                    crab.setMap(map);
                    enemy = crab;
                    System.out.println("[HordeManager] Spawned horde Crab #" + i + " at: " + spawn.x + ", " + spawn.y);
                } else {
                    // 45% Goblin
                    Goblin goblin = new Goblin(spawn);
                    goblin.setHordeAggression(SPEED_MULT, running);
                    goblin.setMap(map);
                    enemy = goblin;
                    System.out.println("[HordeManager] Spawned horde Goblin #" + i + " at: " + spawn.x + ", " + spawn.y);
                }

            } else {
                // volcano / other: 50/50 spiders and bats
                if (rng.nextBoolean()) {
                    Spider s = new Spider(2000 + i + (int) (System.currentTimeMillis() % 10000), spawn);
                    s.setHordeAggression(SPEED_MULT, running);
                    s.setMap(map);
                    enemy = s;
                    System.out.println("[HordeManager] Spawned horde Spider #" + i + " at: " + spawn.x + ", " + spawn.y);
                } else {
                    Bat b = new Bat(spawn);
                    b.setHordeAggression(SPEED_MULT, running);
                    b.setMap(map);
                    enemy = b;
                    System.out.println("[HordeManager] Spawned horde Bat #" + i + " at: " + spawn.x + ", " + spawn.y);
                }
            }
            
            map.getNPCs().add(enemy);
            horde.add(enemy);
            
            // little smoke poof where they warp in
            for (int p = 0; p < 8; p++) {
                particles.add(new SmokeParticle(spawn.x, spawn.y));
            }
        }
        
        System.out.println("[HordeManager] After spawn, NPC count: " + map.getNPCs().size());
        System.out.println("[HordeManager] Horde list size: " + horde.size());
    }

    private static void setHordeMode(boolean on) {
        for (NPC npc : horde) {
            if (npc instanceof Spider) {
                ((Spider) npc).setHordeAggression(SPEED_MULT, on);
            } else if (npc instanceof Bat) {
                ((Bat) npc).setHordeAggression(SPEED_MULT, on);
            } else if (npc instanceof FrostDragon) {
                // dragon uses a slightly lower internal mult when we spawned him,
                // but we still want the "on/off" flag to be correct
                ((FrostDragon) npc).setHordeAggression(SPEED_MULT, on);
            } else if (npc instanceof Crab) {
                ((Crab) npc).setHordeAggression(SPEED_MULT, on);
            } else if (npc instanceof Goblin) {
                ((Goblin) npc).setHordeAggression(SPEED_MULT, on);
            }
        }
    }

    private static int countAlive() {
        return horde.size();
    }

    private static int countMapEnemies(Map map) {
        int c = 0;
        for (var npc : map.getNPCs()) {
            if (npc instanceof Spider || npc instanceof Bat || 
                npc instanceof FrostDragon || npc instanceof Crab || npc instanceof Goblin) {
                c++;
            }
        }
        return c;
    }

    private static int countEnemiesNear(Map map, Bee bee, int radiusPx) {
        int c = 0;
        int bx = (int) bee.getX(), by = (int) bee.getY();
        int r2 = radiusPx * radiusPx;
        for (var npc : map.getNPCs()) {
            if (npc instanceof Spider || npc instanceof Bat || 
                npc instanceof FrostDragon || npc instanceof Crab || npc instanceof Goblin) {
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
        int minTiles = 3, maxTiles = 5;
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
