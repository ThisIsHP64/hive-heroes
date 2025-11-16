package StaticClasses;

import java.util.ArrayList;
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

/**
 * Handles ambient enemy spawning in a radius around the player.
 * AGGRESSIVE VERSION - More spawns, faster rate, closer to player + SMOKE EFFECTS!
 */
public final class EnemySpawner {

    // ===== CONFIGURATION - AGGRESSIVE SETTINGS =====
    // Spawn CLOSER to player so you see them more
    private static final int MIN_SPAWN_DISTANCE_TILES = 3;  // ~192 pixels (was 4)
    private static final int MAX_SPAWN_DISTANCE_TILES = 6;  // ~384 pixels (was 8)
    private static final int TILE_SIZE = 64;

    // Spawn MORE OFTEN
    private static final long SPAWN_INTERVAL_MS = 4000;  // Every 4 seconds (was 8)
    private static long lastSpawnAttempt = 0;

    // HIGHER enemy caps for more action
    private static final int MAX_GRASS_ENEMIES = 25;      // Was 15
    private static final int MAX_VOLCANO_ENEMIES = 30;    // Was 20
    private static final int MAX_FROST_ENEMIES = 30;      // Was 18

    // Spawn MORE at once
    private static final int ENEMIES_PER_WAVE = 4;  // Spawn 4 at a time

    private static final Random rng = new Random();

    // ===== STATE =====
    private static boolean enabled = true;

    // ===== SMOKE PARTICLES =====
    private static final ArrayList<SmokeParticle> particles = new ArrayList<>();

    /**
     * Main update loop - call this every frame from your level screen
     */
    public static void update(Map map, Bee bee) {
        if (!enabled || map == null || bee == null) {
            return;
        }

        // Don't spawn during horde mode
        if (HordeManager.isRunning()) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastSpawnAttempt < SPAWN_INTERVAL_MS) {
            return;  // Not time to spawn yet
        }

        lastSpawnAttempt = now;

        // Check current enemy count and spawn if below cap
        String mapName = map.getClass().getSimpleName();
        int currentEnemies = countEnemiesOnMap(map);
        int maxEnemies = getMaxEnemiesForMap(mapName);

        if (currentEnemies < maxEnemies) {
            int toSpawn = Math.min(ENEMIES_PER_WAVE, maxEnemies - currentEnemies);
            spawnEnemiesAroundPlayer(map, bee, mapName, toSpawn);
        }
    }

    /**
     * Update smoke particles - call this every frame from your level screen
     */
    public static void updateParticles() {
        particles.removeIf(p -> {
            p.update();
            return p.isDead();
        });
    }

    /**
     * Draw smoke particles - call this in your level screen's draw method
     */
    public static void drawParticles(Engine.GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        for (SmokeParticle p : particles) {
            p.draw(graphicsHandler, cameraX, cameraY);
        }
    }

    /**
     * Spawn enemies in a radius around the player
     */
    private static void spawnEnemiesAroundPlayer(Map map, Bee bee, String mapName, int count) {
        System.out.println("[EnemySpawner] Spawning " + count + " enemies for " + mapName);

        for (int i = 0; i < count; i++) {
            Point spawnPos = pickSpawnPositionAroundPlayer(bee);
            NPC enemy = createEnemyForMap(mapName, spawnPos);

            if (enemy != null) {
                enemy.setMap(map);
                map.getNPCs().add(enemy);
                System.out.println("[EnemySpawner] Spawned " + enemy.getClass().getSimpleName() +
                                   " at (" + spawnPos.x + ", " + spawnPos.y + ")");

                // CREATE SMOKE POOF EFFECT! 💨
                for (int p = 0; p < 8; p++) {
                    particles.add(new SmokeParticle(spawnPos.x, spawnPos.y));
                }
            }
        }
    }

    /**
     * Pick a random spawn position around the player
     * Returns a Point 3-6 tiles away in a random direction
     */
    private static Point pickSpawnPositionAroundPlayer(Bee bee) {
        // Random distance between min and max tiles
        int distanceInTiles = MIN_SPAWN_DISTANCE_TILES +
                             rng.nextInt(MAX_SPAWN_DISTANCE_TILES - MIN_SPAWN_DISTANCE_TILES + 1);
        int distanceInPixels = distanceInTiles * TILE_SIZE;

        // Random angle (0 to 360 degrees)
        double angle = rng.nextDouble() * Math.PI * 2;

        // Calculate spawn position
        int spawnX = (int) (bee.getX() + Math.cos(angle) * distanceInPixels);
        int spawnY = (int) (bee.getY() + Math.sin(angle) * distanceInPixels);

        return new Point(spawnX, spawnY);
    }

    /**
     * Create the appropriate enemy type based on the map
     */
    private static NPC createEnemyForMap(String mapName, Point position) {
        if (mapName.equals("GrassMap")) {
            // Grass level: ONLY spiders
            int id = 3000 + rng.nextInt(10000);  // Random ID for tracking
            return new Spider(id, position);

        } else if (mapName.equals("VolcanoMap")) {
            // Volcano level: 50% spiders, 50% bats
            if (rng.nextBoolean()) {
                int id = 3000 + rng.nextInt(10000);
                return new Spider(id, position);
            } else {
                return new Bat(position);
            }

        } else if (mapName.equals("SnowMap") || mapName.equals("FrostMap") || mapName.equals("DungeonMap")) {
            // Frost level: 50% goblins, 30% crabs, 20% dragons
            int roll = rng.nextInt(100);

            if (roll < 50) {
                // 50% chance: Goblin (common)
                return new Goblin(position);
            } else if (roll < 80) {
                // 30% chance: Crab (uncommon)
                return new Crab(position);
            } else {
                // 20% chance: FrostDragon (rare)
                return new FrostDragon(position);
            }
        }

        // Default fallback
        return null;
    }

    /**
     * Count how many enemies are currently on the map
     */
    private static int countEnemiesOnMap(Map map) {
        int count = 0;
        for (NPC npc : map.getNPCs()) {
            if (npc instanceof Spider || npc instanceof Bat ||
                npc instanceof Goblin || npc instanceof Crab || npc instanceof FrostDragon) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get max enemy cap based on map type
     */
    private static int getMaxEnemiesForMap(String mapName) {
        if (mapName.equals("GrassMap")) {
            return MAX_GRASS_ENEMIES;
        } else if (mapName.equals("VolcanoMap")) {
            return MAX_VOLCANO_ENEMIES;
        } else if (mapName.equals("SnowMap") || mapName.equals("FrostMap") || mapName.equals("DungeonMap")) {
            return MAX_FROST_ENEMIES;
        }
        return 10;  // Default
    }

    /**
     * Enable or disable ambient spawning
     */
    public static void setEnabled(boolean enabled) {
        EnemySpawner.enabled = enabled;
        System.out.println("[EnemySpawner] Spawning " + (enabled ? "enabled" : "disabled"));
    }

    /**
     * Check if spawning is enabled
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * Reset the spawn timer (useful when changing maps)
     */
    public static void resetTimer() {
        lastSpawnAttempt = 0;
    }
}