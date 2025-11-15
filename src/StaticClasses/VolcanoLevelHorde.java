package StaticClasses;

import Level.Map;
import Players.Bee;
import Enemies.Spider;
import Enemies.Bat;
import Level.NPC;
import Utils.Point;
import Effects.SmokeParticle;
import java.util.ArrayList;
import java.util.Random;

/**
 * VolcanoLevelHorde - Spawns BAT + SPIDER horde (50/50 mix)
 * Use this during boss fights when you want a volcano-themed enemy wave
 */
public final class VolcanoLevelHorde {
    
    private static final int WAVE_SIZE = 5;
    private static final float SPEED_MULT = 4.0f;
    private static final Random rng = new Random();
    private static final ArrayList<SmokeParticle> particles = new ArrayList<>();
    
    /**
     * Spawn a wave of bats and spiders (50/50 mix) around the player
     * @param map Current map
     * @param bee Player
     * @param count Number of enemies to spawn
     */
    public static void spawnWave(Map map, Bee bee, int count) {
        if (map == null || bee == null) return;
        
        System.out.println("[VolcanoLevelHorde] Spawning " + count + " volcanic enemies");
        
        for (int i = 0; i < count; i++) {
            Point spawn = pickSpawnOutsideCamera(bee);
            
            NPC enemy;
            // 50/50 mix of bats and spiders
            if (rng.nextBoolean()) {
                Spider spider = new Spider(4000 + i + (int)(System.currentTimeMillis() % 10000), spawn);
                spider.setHordeAggression(SPEED_MULT, true);
                spider.setMap(map);
                enemy = spider;
                System.out.println("[VolcanoLevelHorde] Spawned spider");
            } else {
                Bat bat = new Bat(spawn);
                bat.setHordeAggression(SPEED_MULT, true);
                bat.setMap(map);
                enemy = bat;
                System.out.println("[VolcanoLevelHorde] Spawned bat");
            }
            
            map.getNPCs().add(enemy);
            
            // Spawn particles for dramatic effect
            for (int p = 0; p < 8; p++) {
                particles.add(new SmokeParticle(spawn.x, spawn.y));
            }
        }
        
        System.out.println("[VolcanoLevelHorde] Wave spawned!");
    }
    
    /**
     * Spawn a default-sized wave (5 enemies)
     */
    public static void spawnWave(Map map, Bee bee) {
        spawnWave(map, bee, WAVE_SIZE);
    }
    
    /**
     * Update and clean up particles
     */
    public static void updateParticles() {
        particles.removeIf(p -> {
            p.update();
            return p.isDead();
        });
    }
    
    /**
     * Draw spawn particles
     */
    public static void drawParticles(Engine.GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        for (SmokeParticle p : particles) {
            p.draw(graphicsHandler, cameraX, cameraY);
        }
    }
    
    private static Point pickSpawnOutsideCamera(Bee bee) {
        int tile = 64;
        int minTiles = 3, maxTiles = 5;
        int dist = (rng.nextInt(maxTiles - minTiles + 1) + minTiles) * tile;
        double ang = rng.nextDouble() * Math.PI * 2;
        int x = (int) (bee.getX() + Math.cos(ang) * dist);
        int y = (int) (bee.getY() + Math.sin(ang) * dist);
        return new Point(x, y);
    }
}