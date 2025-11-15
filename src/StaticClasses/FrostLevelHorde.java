package StaticClasses;

import Level.Map;
import Players.Bee;
import Enemies.FrostDragon;
import Enemies.Crab;
import Enemies.Goblin;
import Level.NPC;
import Utils.Point;
import Effects.SmokeParticle;
import java.util.ArrayList;
import java.util.Random;

/**
 * FrostLevelHorde - Spawns FROSTDRAGON + CRAB + GOBLIN horde
 * Use this during boss fights when you want a frost-themed enemy wave
 */
public final class FrostLevelHorde {
    
    private static final int WAVE_SIZE = 5;
    private static final float SPEED_MULT = 4.0f;
    private static final Random rng = new Random();
    private static final ArrayList<SmokeParticle> particles = new ArrayList<>();
    
    /**
     * Spawn a wave of frost enemies (FrostDragon, Crab, Goblin) around the player
     * Distribution: 20% FrostDragon, 40% Crab, 40% Goblin
     * @param map Current map
     * @param bee Player
     * @param count Number of enemies to spawn
     */
    public static void spawnWave(Map map, Bee bee, int count) {
        if (map == null || bee == null) return;
        
        System.out.println("[FrostLevelHorde] Spawning " + count + " frost enemies");
        
        for (int i = 0; i < count; i++) {
            Point spawn = pickSpawnOutsideCamera(bee);
            
            NPC enemy;
            // Random distribution: 20% dragon, 40% crab, 40% goblin
            int roll = rng.nextInt(100);
            
            if (roll < 20) {
                // 20% chance: FrostDragon (rare, powerful)
                FrostDragon dragon = new FrostDragon(spawn);
                dragon.setHordeAggression(SPEED_MULT, true);
                dragon.setMap(map);
                enemy = dragon;
                System.out.println("[FrostLevelHorde] Spawned FrostDragon");
            } else if (roll < 60) {
                // 40% chance: Crab
                Crab crab = new Crab(spawn);
                crab.setHordeAggression(SPEED_MULT, true);
                crab.setMap(map);
                enemy = crab;
                System.out.println("[FrostLevelHorde] Spawned Crab");
            } else {
                // 40% chance: Goblin
                Goblin goblin = new Goblin(spawn);
                goblin.setHordeAggression(SPEED_MULT, true);
                goblin.setMap(map);
                enemy = goblin;
                System.out.println("[FrostLevelHorde] Spawned Goblin");
            }
            
            map.getNPCs().add(enemy);
            
            // Spawn particles for dramatic effect
            for (int p = 0; p < 8; p++) {
                particles.add(new SmokeParticle(spawn.x, spawn.y));
            }
        }
        
        System.out.println("[FrostLevelHorde] Wave spawned!");
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