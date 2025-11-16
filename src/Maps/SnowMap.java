package Maps;

import Enemies.Crab;
import Enemies.Goblin;
import Enemies.FrostDragon;
import Flowers.SnowFlower;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Portals.GrassPortal;
import Tilesets.SnowTileset;
import java.util.ArrayList;

public class SnowMap extends Map {

    public SnowMap() {
        super("snow_map.txt", new SnowTileset());
        this.playerStartPosition = getMapTile(0, 0).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        // portal out
        GrassPortal grassPortal = new GrassPortal(1, getMapTile(47, 13).getLocation().addX(10).addY(15));
        npcs.add(grassPortal);
        
        // a little flower flavor
        SnowFlower snowFlower = new SnowFlower(4, getMapTile(15, 15).getLocation());
        npcs.add(snowFlower);
        
        // ===== GOBLINS REMOVED - EnemySpawner handles them dynamically now! =====
        
        // ===== CRABS REMOVED - EnemySpawner handles them dynamically now! =====
        
        // ===== FROST DRAGONS REMOVED - EnemySpawner handles them dynamically now! =====
        
        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        return triggers;
    }

    @Override
    public void loadScripts() {
        // no scripts yet
    }
}